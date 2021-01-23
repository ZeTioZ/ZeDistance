package fr.opsycraft.opsydistance;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.opsycraft.opsydistance.eventhandler.OpsyDistanceDamageHandler;
import fr.opsycraft.opsydistance.utils.FilesManagerUtils;
import fr.opsycraft.opsydistance.utils.LagUtils;

public class OpsyDistanceMain extends JavaPlugin implements Listener{
	private Plugin plugin;
	private FilesManagerUtils filesManager;
	private OpsyDistanceCommand distanceCommand;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LagUtils(), 100L, 1L);
		
		filesManager = new FilesManagerUtils(this);
		filesManager.createConfigFile();
		filesManager.createMessagesFile();
		filesManager.createDatabaseFile();
		
		distanceCommand = new OpsyDistanceCommand(this);
		
		filesManager.loadDatabase();
		
		registerEvents(this, new OpsyDistanceDamageHandler(this));
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
	
	public FilesManagerUtils getFilesManger()
	{
		return this.filesManager;
	}
	
	public OpsyDistanceCommand getDistanceCommand()
	{
		return this.distanceCommand;
	}
	
}
