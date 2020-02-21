package fr.opsycraft.opsydistance;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FilesManager
{
	//region Class Constructor
	private Main main;
	private Plugin plugin;
	
	public FilesManager(Main main)
	{
		this.main = main;
		this.plugin = main.getPlugin();
	}
	//endregion
	
	private YamlConfiguration messagesFilesConfig;
	
	//region Create Messages File
	public void createMessagesFile()
	{
		File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if(!messagesFile.exists())
		{
			messagesFile.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
		
		messagesFilesConfig = new YamlConfiguration();
		try
		{
			messagesFilesConfig.load(messagesFile);
		}
		catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	//endregion
	
	//region Get Messages File
	public YamlConfiguration getMessagesFile()
	{
		return this.messagesFilesConfig;
	}
	//endregion
	
	private YamlConfiguration configsFileConfig;
	
	//region Create Configs File
	public void createConfigsFile()
	{
		File configsFile = new File(plugin.getDataFolder(), "configs.yml");
		if(!configsFile.exists())
		{
			configsFile.getParentFile().mkdirs();
			plugin.saveResource("configs.yml", false);
		}
		
		configsFileConfig = new YamlConfiguration();
		try
		{
			configsFileConfig.load(configsFile);
		}
		catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	//endregion
	
	//region Get Configs File
	public YamlConfiguration getConfigsFile()
	{
		return this.configsFileConfig;
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