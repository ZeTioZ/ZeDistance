
package fr.zetioz.zedistance.commands;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import fr.zetioz.coreutils.ColorUtils;
import fr.zetioz.coreutils.FilesManagerUtils;
import fr.zetioz.zedistance.ZeDistanceMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import static fr.zetioz.coreutils.ColorUtils.sendMessage;

public class ZeDistanceCommand implements TabExecutor, FilesManagerUtils.ReloadableFiles
{
	private final ZeDistanceMain instance;
	private YamlConfiguration messages;
	private YamlConfiguration database;
	private String prefix;

	private final List<UUID> playersActivated;

	@Override
	public void reloadFiles() throws FileNotFoundException
	{
		this.messages = this.instance.getFilesManger().getSimpleYaml("messages");
		this.database = this.instance.getFilesManger().getSimpleYaml("database");
		this.prefix = ColorUtils.color(messages.getString("prefix", "&8[&cZeDistance&8] &r"));
	}
	
	public ZeDistanceCommand(ZeDistanceMain instance) throws FileNotFoundException
	{
		this.instance = instance;
		this.instance.getFilesManger().addReloadable(this);
		this.reloadFiles();
		this.playersActivated = this.instance.getPlayersActivated();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("pvpdistance"))
		{
			if(sender instanceof final Player player)
			{
				if(args.length == 0)
				{
					if(player.hasPermission("pvpdistance.use"))
					{
						if(!playersActivated.contains(player.getUniqueId()))
						{	
							playersActivated.add(player.getUniqueId());
							sendMessage(player, messages.getStringList("hit-checker-enabled"), prefix);
						}
						else
						{
							playersActivated.remove(player.getUniqueId());
							sendMessage(player, messages.getStringList("hit-checker-disabled"), prefix);
						}
						this.database.set("players-activated", playersActivated.stream().map(UUID::toString).toList());
						this.instance.getFilesManger().saveSimpleYaml("database");
					}
					else
					{
						sendMessage(player, messages.getStringList("errors.not-enought-permissions"), prefix);
					}
				}
				else if(args.length == 1)
				{
					if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on"))
					{
						if(player.hasPermission("pvpdistance.use"))
						{
							if(!playersActivated.contains(player.getUniqueId()))
							{	
								playersActivated.add(player.getUniqueId());
								sendMessage(player, messages.getStringList("hit-checker-enabled"), prefix);
								this.database.set("players-activated", playersActivated.stream().map(UUID::toString).toList());
								this.instance.getFilesManger().saveSimpleYaml("database");
							}
							else
							{
								sendMessage(player, messages.getStringList("errors.already-enabled"), prefix);
							}
						}
						else
						{
							sendMessage(player, messages.getStringList("errors.not-enought-permissions"), prefix);
						}
					}
					else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off"))
					{
						if(player.hasPermission("pvpdistance.use"))
						{
							if(playersActivated.contains(player.getUniqueId()))
							{	
								playersActivated.remove(player.getUniqueId());
								sendMessage(player, messages.getStringList("hit-checker-disabled"), prefix);
								this.database.set("players-activated", playersActivated.stream().map(UUID::toString).toList());
								this.instance.getFilesManger().saveSimpleYaml("database");
							}
							else
							{
								sendMessage(player, messages.getStringList("errors.already-disabled"), prefix);
							}
						}
						else
						{
							sendMessage(player, messages.getStringList("errors.not-enough-permissions"), prefix);
						}
					}
					else if(args[0].equalsIgnoreCase("help"))
					{
						sendMessage(sender, messages.getStringList("plugin-help"), prefix);
					}
				}
			}
			else
			{
				if(args[0].equalsIgnoreCase("reload"))
				{
					instance.getFilesManger().reloadAllSimpleYaml();
					sendMessage(sender, messages.getStringList("plugin-reloaded"), prefix);
				}
				else
				{
					sendMessage(sender, messages.getStringList("errors.not-a-player"), prefix);
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String msg, String[] args)
	{
		if (command.getName().equalsIgnoreCase("zedistance"))
		{
			final List<String> firstArgList = new ArrayList<>(List.of("enable", "on", "disable", "off", "help"));
			final List<String> completions = new ArrayList<>();

			if(sender.hasPermission("zedistance.reload")) firstArgList.add("reload");

			if (args.length == 1)
			{
				StringUtil.copyPartialMatches(args[0], firstArgList, completions);
			}
			Collections.sort(completions);
			return completions;
		}
		return new ArrayList<>();
	}
}
