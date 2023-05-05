package fr.zetioz.zedistance;

import fr.zetioz.coreutils.FilesManagerUtils;
import fr.zetioz.zedistance.commands.ZeDistanceCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.zetioz.zedistance.listeners.ZeDistanceDamageHandler;
import fr.zetioz.zedistance.utils.TPSUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZeDistanceMain extends JavaPlugin implements Listener{
	private Plugin plugin;
	private FilesManagerUtils filesManager;

	@Getter
	private List<UUID> playersActivated;

	@Override
	public void onEnable()
	{
		plugin = this;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TPSUtils(), 100L, 1L);
		
		filesManager = new FilesManagerUtils(this);
		filesManager.createSimpleYaml("config");
		filesManager.createSimpleYaml("messages");
		filesManager.createSimpleYaml("database");

		playersActivated = new ArrayList<>();

		try
		{
			playersActivated.addAll(filesManager.getSimpleYaml("database").getStringList("playersActivated").stream().map(UUID::fromString).toList());
			getCommand("pvpdistance").setExecutor(new ZeDistanceCommand(this));
			registerEvents(this, new ZeDistanceDamageHandler(this));
		}
		catch(FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
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

}
