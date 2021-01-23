
package fr.opsycraft.opsydistance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.opsycraft.opsydistance.utils.ColorUtils;
import fr.opsycraft.opsydistance.utils.FilesManagerUtils;

public class OpsyDistanceCommand implements CommandExecutor, Listener {

	private OpsyDistanceMain main;
	private FilesManagerUtils filesManager;
	private YamlConfiguration messagesFile;
	private String prefix;

	private List<String> playerActivated = new ArrayList<>();
	
	public OpsyDistanceCommand(OpsyDistanceMain main)
	{
		this.main = main;
		this.filesManager = main.getFilesManger();
		this.messagesFile = this.filesManager.getMessagesFile();
		this.prefix = ColorUtils.color(messagesFile.getString("prefix"));
	}
	
	public List<String> getPlayerActivated()
	{
		return this.playerActivated;
	}
	
	public void setPlayerActivated(List<String> playerActivated)
	{
		this.playerActivated = playerActivated;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("pvpdistance"))
		{
			if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(args.length == 0)
				{
					if(player.hasPermission("pvpdistance.use"))
					{
						if(!playerActivated.contains(player.getUniqueId().toString()))
						{	
							playerActivated.add(player.getUniqueId().toString());
							filesManager.saveDatabase();
							for(String line : ColorUtils.color(messagesFile.getStringList("hit-checker-enabled")))
							{
								player.sendMessage(prefix + line);
							}
						}
						else
						{
							playerActivated.remove(player.getUniqueId().toString());
							filesManager.saveDatabase();
							for(String line : ColorUtils.color(messagesFile.getStringList("hit-checker-disabled")))
							{
								player.sendMessage(prefix + line);
							}
						}
					}
					else
					{
						for(String line : ColorUtils.color(messagesFile.getStringList("errors.not-enought-permissions")))
						{
							player.sendMessage(prefix + line);
						}
					}
				}
				else if(args.length == 1)
				{
					if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on"))
					{
						if(player.hasPermission("pvpdistance.use"))
						{
							if(!playerActivated.contains(player.getUniqueId().toString()))
							{	
								playerActivated.add(player.getUniqueId().toString());
								filesManager.saveDatabase();
								for(String line : ColorUtils.color(messagesFile.getStringList("hit-checker-enabled")))
								{
									player.sendMessage(prefix + line);
								}
							}
							else
							{
								for(String line : ColorUtils.color(messagesFile.getStringList("errors.already-enabled")))
								{
									player.sendMessage(prefix + line);
								}
							}
						}
						else
						{
							for(String line : ColorUtils.color(messagesFile.getStringList("errors.not-enought-permissions")))
							{
								player.sendMessage(prefix + line);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off"))
					{
						if(player.hasPermission("pvpdistance.use"))
						{
							if(playerActivated.contains(player.getUniqueId().toString()))
							{	
								playerActivated.remove(player.getUniqueId().toString());
								filesManager.saveDatabase();
								for(String line : ColorUtils.color(messagesFile.getStringList("hit-checker-disabled")))
								{
									player.sendMessage(prefix + line);
								}
							}
							else
							{
								for(String line : ColorUtils.color(messagesFile.getStringList("errors.already-disabled")))
								{
									player.sendMessage(prefix + line);
								}
							}
						}
						else
						{
							for(String line : ColorUtils.color(messagesFile.getStringList("errors.not-enought-permissions")))
							{
								player.sendMessage(prefix + line);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("help"))
					{
						for(String line : ColorUtils.color(messagesFile.getStringList("plugin-help")))
						{
							player.sendMessage(prefix + line);
						}
					}
					else if(args[0].equalsIgnoreCase("reload"))
					{
						if(player.hasPermission("pvpdistance.reload"))
						{
							Bukkit.getPluginManager().disablePlugin(main);
							Bukkit.getPluginManager().enablePlugin(main);
							for(String line : ColorUtils.color(messagesFile.getStringList("plugin-reloaded")))
							{
								player.sendMessage(prefix + line);
							}
						}
						else
						{
							for(String line : ColorUtils.color(messagesFile.getStringList("errors.not-enought-permissions")))
							{
								player.sendMessage(prefix + line);
							}
						}
					}
				}
			}
			else
			{
				for(String line : ColorUtils.color(messagesFile.getStringList("errors.not-a-player")))
				{
					sender.sendMessage(prefix + line);
				}
			}
		}
		return false;
	}
}
