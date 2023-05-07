package fr.zetioz.zedistance.listeners;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import fr.zetioz.coreutils.ColorUtils;
import fr.zetioz.coreutils.FilesManagerUtils;
import fr.zetioz.zedistance.ZeDistanceMain;
import fr.zetioz.zedistance.utils.TPSUtils;
import fr.zetioz.zedistance.utils.PingUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import static fr.zetioz.coreutils.ColorUtils.sendMessage;

public class ZeDistanceDamageHandler implements Listener, FilesManagerUtils.ReloadableFiles
{
	private final ZeDistanceMain instance;
	private YamlConfiguration config;
	private YamlConfiguration messages;
	private String prefix;
	
	public ZeDistanceDamageHandler(ZeDistanceMain instance) throws FileNotFoundException
	{
		this.instance = instance;
		this.instance.getFilesManger().addReloadable(this);
		this.reloadFiles();
	}

	@Override
	public void reloadFiles() throws FileNotFoundException
	{
		this.config = instance.getFilesManger().getSimpleYaml("config");
		this.messages = instance.getFilesManger().getSimpleYaml("messages");
		this.prefix = ColorUtils.color(messages.getString("prefix", "&8[&cZeDistance&8] &r"));
	}

	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e)
	{
		if(e.getCause() != DamageCause.PROJECTILE
			&& e.getDamager() instanceof final Player damager
			&& e.getEntity() instanceof final Player victim)
		{
			double distance = damager.getLocation().distance(victim.getLocation());
			if(distance >= config.getDouble("max-distance"))
			{
				e.setCancelled(config.getBoolean("cancel-damage"));
				sendMessage(victim, messages.getStringList("alert.over-max"), prefix, "{attacker}", damager.getName(), "{distance}", new DecimalFormat("#.##").format(distance));
				for(Player staff : Bukkit.getServer().getOnlinePlayers())
				{
					if(staff.hasPermission("pvpdistance.alert"))
					{
						sendMessage(staff, messages.getStringList("alert.staff-alert"), prefix, "{attacker}", damager.getName(), "{victim}", victim.getName(), "{distance}", new DecimalFormat("#.##").format(distance));
					}
				}

				if(config.getBoolean("kick.enabled")
					&& config.getDouble("kick.distance", 6d) >= config.getDouble("max-distance",5.5)
					&& distance >= config.getDouble("kick.distance", 6d))
				{
					double tps = TPSUtils.getTPS();
					int ping = PingUtils.getPing(damager);
					if(tps >= config.getDouble("kick.tps-threshold", 19d)
						&& ping <= config.getInt("kick.ping-threshold", 100))
					{
						damager.kickPlayer(prefix + ColorUtils.color(config.getString("kick.message", "&cYou have been kicked for attacking a player too far away.")));
					}
				}
				else if(config.getBoolean("kick.enabled")
						&& config.getDouble("kick.distance", 6d) < config.getDouble("max-distance", 5.5))
				{
					for(String line : messages.getStringList("errors.kick-distance-under-max"))
					{
						line = ColorUtils.color(line);
						instance.getLogger().severe(line);
					}
				}

				if(config.getBoolean("command.enabled")
					&& config.getDouble("command.distance", 6d) >= config.getDouble("max-distance", 5.5)
					&& distance >= config.getDouble("command.distance", 6d))
				{
					double tps = TPSUtils.getTPS();
					int ping = PingUtils.getPing(damager);
					if(tps >= config.getDouble("command.tps-threshold", 19d) && ping <= config.getInt("command.ping-threshold", 100))
					{
						if(!config.getStringList("command.execute").isEmpty())
						{
							for(String commandToExecute : config.getStringList("command.execute"))
							{
								commandToExecute = commandToExecute.replace("{attacker}", damager.getName())
																   .replace("{victim}", victim.getName())
																   .replace("{distance}", new DecimalFormat("#.##").format(distance));
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), commandToExecute);
							}
						}
						else
						{
							instance.getLogger().severe("Please, make sure you set the commands to execute in the configs file!");
						}
					}
				}
				else if(config.getBoolean("command.enabled")
						&& config.getDouble("command.distance", 6d) < config.getDouble("max-distance", 5.5))
				{
					for(String line : messages.getStringList("errors.command-distance-under-max"))
					{
						line = ColorUtils.color(line);
						instance.getLogger().severe(line);
					}
				}
			}
			else
			{
				if(instance.getPlayersActivated().contains(victim.getUniqueId()))
				{
					sendMessage(victim, messages.getStringList("alert.under-max"), prefix, "{attacker}", damager.getName(), "{distance}", new DecimalFormat("#.##").format(distance));
				}
			}
		}
	}
}
