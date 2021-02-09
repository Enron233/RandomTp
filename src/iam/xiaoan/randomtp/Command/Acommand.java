package iam.xiaoan.randomtp.Command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

import static iam.xiaoan.randomtp.main.Main.getIns;

public class Acommand implements CommandExecutor {
    private Map<Player,Long> playerLongMap = new HashMap<>();
    private Random num = new Random();
    private Long cd = getIns().getConfig().getInt("usecd") * 1000L;
    private boolean isNumber(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("你必须是一个玩家！");
        }
        Player p = (Player)sender;
        if(args.length == 0) {
            if(p.hasPermission("anrtp.set")){
                p.sendMessage("§a======================RandomTp======================");
                p.sendMessage("§9/anrtp set [range]           设置玩家每次随机传送的半径");
                p.sendMessage("§9/anrtp set place [name]      设置脚下为定点随机传送的路径点，名字为[name]");
                p.sendMessage("§9/anrtp set place list        查看已设置的路径点");
                p.sendMessage("§9/anrtp set place del [name]  删除某个已经设置的路径点");
                p.sendMessage("§9/anrtp t                     在一定范围内随机传送");
                p.sendMessage("§9/anrtp p                     在路径点之间随机传送");
                p.sendMessage("§a====================================================");
            }else {
                p.sendMessage("§a======================RandomTp======================");
                p.sendMessage("§9/anrtp t                   在一定范围内随机传送");
                p.sendMessage("§9/anrtp p                  在路径点之间随机传送");
                p.sendMessage("§a====================================================");
            }
        }else if (args[0].equals("t")) {
            if (playerLongMap.getOrDefault(p,-1L) + cd < System.currentTimeMillis()) {
                int ran = num.nextInt(getIns().getConfig().getInt("range"));
                int x = p.getLocation().getBlockX() + ran;
                int z = p.getLocation().getBlockZ() + num.nextInt(ran);
                Location aim = new Location(p.getWorld(), x, p.getWorld().getHighestBlockYAt(x, z), z);
                p.getLocation().getBlockX();
                p.teleport(aim);
                p.sendMessage("§a您已被传送到:" + x + " " + p.getWorld().getHighestBlockYAt(x, z) + " " + z);
                playerLongMap.put(p, System.currentTimeMillis());
            }else{
                Long needCd = (cd -(System.currentTimeMillis() - playerLongMap.get(p)))/1000;
                p.sendMessage("§a冷却中: §c"+ needCd + "秒");
            }
        } else if (args[0].equals("p")){
            if (playerLongMap.getOrDefault(p,-1L) + cd < System.currentTimeMillis()) {
                Set<String> listFirst = getIns().getConfig().getConfigurationSection("place").getKeys(true);
                int ran2 = num.nextInt(listFirst.size());
                String[] willTpPlace = listFirst.toArray(new String[listFirst.size()]);
                Location a = (Location) getIns().getConfig().get("place."+ willTpPlace[ran2]);
                p.teleport(a);
                p.sendMessage("§a你已经被随机传送到路径点： §6" + willTpPlace[ran2]);
            }else{
                Long needCd = (cd - (System.currentTimeMillis() - playerLongMap.get(p)))/1000;
                p.sendMessage("§a冷却中: §c"+ needCd + "秒");
            }


        }else if (args[0].equals("set")){
            if (args.length == 1) {
                p.sendMessage("§a请检查指令输入是否有误！");
                return true;
            }
            if (p.hasPermission("anrtp.set")){
                if (isNumber(args[1])){
                    getIns().getConfig().set("range",Integer.parseInt(args[1]));
                    getIns().saveConfig();
                    getIns().reloadConfig();
                    p.sendMessage("§a修改成功！当前随机传送范围是: §6" + args[1]);
                    return true;
                }else if(args[1].equals("place")){
                    if (args.length == 2) {
                        p.sendMessage("§a请检查指令输入是否有误！");
                        return true;
                    }
                    if (args[2].equals("list")) {
                        Set<String> list =getIns().getConfig().getConfigurationSection("place").getKeys(true);
                        String[] str = list.toArray(new String[list.size()]);
                        p.sendMessage("§a当前路径点共有§c "+ list.size() +" §a个，分别是：");
                        for (String kk : str){
                            p.sendMessage(kk);
                        }return true;
                    }else if (args[2].equals("del")) {
                        getIns().getConfig().set("place."+ args[3],null);
                        getIns().saveConfig();
                        getIns().reloadConfig();
                        p.sendMessage("§a删除成功！");
                        return true;
                    }
                    getIns().getConfig().set("place."+ args[2],p.getPlayer().getLocation());
                    getIns().saveConfig();
                    p.sendMessage("§a成功创建名字为：§6 " + args[2] + " §a的路径点");

                }else{
                    p.sendMessage("§a请输入一个数字！");
                }
            }else{
                p.sendMessage("§a你没有权限这么做！");
            }
            return true;
        }else{
            p.sendMessage("§a请检查指令输入是否有误！");
        }
        return true;
    }
}
