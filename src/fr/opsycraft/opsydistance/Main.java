package fr.opsycraft.opsydistance;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	private Plugin plugin;
	private FilesManager filesManager;
	private DistanceCommand distanceCommand;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
		
		filesManager = new FilesManager(this);
		filesManager.createConfigsFile();
		filesManager.createMessagesFile();
		filesManager.createDatabaseFile();
		
		distanceCommand = new DistanceCommand(this);
		
		filesManager.loadDatabase();
		
		registerEvents(this, distanceCommand);
		getCommand("pvpdistance").setExecutor(distanceCommand);
	}
	
	@Override
	public void onDisable()
	{
		plugin =  null;
	}
	
	public static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners)
        {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
	
	public Plugin getPlugin()
	{
		return this.plugin;
	}
	
	public FilesManager getFilesManger()
	{
		return this.filesManager;
	}
	
	public DistanceCommand getDistanceCommand()
	{
		return this.distanceCommand;
	}
	
}
