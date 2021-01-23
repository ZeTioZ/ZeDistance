package fr.opsycraft.opsydistance.utils;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.opsycraft.opsydistance.OpsyDistanceMain;

public class FilesManagerUtils
{
	//region Class Constructor
	private OpsyDistanceMain main;
	private Plugin plugin;
	
	public FilesManagerUtils(OpsyDistanceMain main)
	{
		this.main = main;
		this.plugin = main.getPlugin();
	}
	//endregion
	
	private YamlConfiguration messagesFileConfig;
	
	//region Create Messages File
	public void createMessagesFile()
	{
		File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if(!messagesFile.exists())
		{
			messagesFile.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
		
		messagesFileConfig = new YamlConfiguration();
		try
		{
			messagesFileConfig.load(messagesFile);
		}
		catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	//endregion
	
	//region Get Messages File
	public YamlConfiguration getMessagesFile()
	{
		return this.messagesFileConfig;
	}
	//endregion
	
	private YamlConfiguration configFileConfig;
	
	//region Create Configs File
	public void createConfigFile()
	{
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists())
		{
			configFile.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
		}
		
		configFileConfig = new YamlConfiguration();
		try
		{
			configFileConfig.load(configFile);
		}
		catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	//endregion
	
	//region Get Configs File
	public YamlConfiguration getConfigFile()
	{
		return this.configFileConfig;
	}
	//endregion
	
	private File databaseFile;
	private YamlConfiguration databaseFileConfig;
	
	//region Create Database File
	public void createDatabaseFile()
	{
		databaseFile = new File(plugin.getDataFolder(), "database.yml");
		if(!databaseFile.exists())
		{
			try
			{
				databaseFile.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		databaseFileConfig = new YamlConfiguration();
		try
		{
			databaseFileConfig.load(databaseFile);
		}
		catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	//endregion
	
	//region Get Database File
	public YamlConfiguration getDatabaseFile()
	{
		return this.databaseFileConfig;
	}
	//endregion
	
	//region Load Database
	public void loadDatabase()
	{
		List<String> loadedList = this.databaseFileConfig.getStringList("players-active-uuid");
		if(!loadedList.isEmpty())
		{
			main.getDistanceCommand().setPlayerActivated(loadedList);
		}
	}
	//endregion

	//region Save Database
	public void saveDatabase()
	{
		List<String> playerActivated = main.getDistanceCommand().getPlayerActivated();
		this.databaseFileConfig.set("players-active-uuid", playerActivated);
		try {
			this.databaseFileConfig.save(databaseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion
}