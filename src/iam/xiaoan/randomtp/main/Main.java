package iam.xiaoan.randomtp.main;

import iam.xiaoan.randomtp.Command.Acommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        ins = this;
        saveDefaultConfig();
        Bukkit.getPluginCommand("anrtp").setExecutor(new Acommand());
        this.getLogger().info("§a我是小安，感谢你能使用这款插件！");
        this.getLogger().info("§a插件已经运行了！");
    }
    @Override
    public void onDisable() {
        this.getLogger().info("插件已成功卸载！");
    }
    private static Main ins;
    public static Main getIns(){
        return ins;
    }
}
