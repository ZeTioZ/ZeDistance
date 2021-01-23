package fr.opsycraft.opsydistance.eventhandler;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.opsycraft.opsydistance.OpsyDistanceMain;
import fr.opsycraft.opsydistance.utils.ColorUtils;
import fr.opsycraft.opsydistance.utils.LagUtils;
import fr.opsycraft.opsydistance.utils.PingUtils;

public class OpsyDistanceDamageHandler implements Listener
{
	
	private OpsyDistanceMain opsyDistanceMain;
	private YamlConfiguration configFile;
	private YamlConfiguration messagesFile;
	private String prefix;
	
	public OpsyDistanceDamageHandler(OpsyDistanceMain opsyDistanceMain)
	{
		this.opsyDistanceMain = opsyDistanceMain;
		this.configFile = opsyDistanceMain.getFilesManger().getConfigFile();
		this.messagesFile = opsyDistanceMain.getFilesManger().getMessagesFile();
		this.prefix = ColorUtils.color(messagesFile.getString("prefix"));
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e)
	{
		if(e.getCause() != null && e.getCause() != DamageCause.PROJECTILE
			&& e.getDamager() instanceof Player
			&& e.getEntity() instanceof Player)
		{
			Player damager = (Player) e.getDamager();
			Player victim = (Player) e.getEntity();
			double distance = damager.getLocation().distance(victim.getLocation());
			if(distance >= configFile.getDouble("max-distance"))
			{
				for(String line : ColorUtils.color(messagesFile.getStringList("alert.over-max")))
				{
					line = line.replace("{attacker}", damager.getName());
					line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
					victim.sendMessage(prefix + line);
				}
				for(Player staff : Bukkit.getServer().getOnlinePlayers())
				{
					if(staff.hasPermission("pvpdistance.alert"))
					{
						for(String line : ColorUtils.color(messagesFile.getStringList("alert.staff-alert")))
						{
							line = line.replace("{attacker}", damager.getName());
							line = line.replace("{victim}", victim.getName());
							line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
							victim.sendMessage(prefix + line);
						}
					}
				}
				if(configFile.getBoolean("cancel-damage"))
				{
					e.setCancelled(true);
				}
				
				if(configFile.getBoolean("kick.enabled")
					&& configFile.getDouble("kick.distance") >= configFile.getDouble("max-distance")
					&& distance >= configFile.getDouble("kick.distance"))
				{
					double tps = LagUtils.getTPS();
					int ping = PingUtils.getPing(damager);
					if(!(tps < configFile.getDouble("kick.tps-threshold") || ping > configFile.getInt("kick.ping-threshold")))
					{
						if(configFile.getString("kick.message") != null)
						{
							damager.kickPlayer(prefix + ColorUtils.color(configFile.getString("kick.message")));
						}
						else
						{
							opsyDistanceMain.getLogger().severe("Please, make sure you set the kick message in the configs file!");
						}
					}
				}
				else if(configFile.getBoolean("kick.enabled")
						&& configFile.getDouble("kick.distance") < configFile.getDouble("max-distance"))
				{
					for(String line : messagesFile.getStringList("errors.kick-distance-under-max"))
					{
						line = ColorUtils.color(line);
						opsyDistanceMain.getLogger().severe(line);
					}
				}
				
				if(configFile.getBoolean("command.enabled")
					&& configFile.getDouble("command.distance") >= configFile.getDouble("max-distance")
					&& distance >= configFile.getDouble("command.distance"))
				{
					double tps = LagUtils.getTPS();
					int ping = PingUtils.getPing(damager);
					if(!(tps < configFile.getDouble("command.tps-threshold") || ping > configFile.getInt("command.ping-threshold")))
					{
						if(configFile.getStringList("command.execute") != null)
						{
							for(String commandToExecute : configFile.getStringList("command.execute"))
							{
								commandToExecute = commandToExecute.replace("{attacker}", damager.getName())
																   .replace("{victim}", victim.getName())
																   .replace("{distance}", new DecimalFormat("#.##").format(distance));
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), commandToExecute);
							}
						}
						else
						{
							opsyDistanceMain.getLogger().severe("Please, make sure you set the commands to execute in the configs file!");
						}
					}
				}
				else if(configFile.getBoolean("command.enabled")
						&& configFile.getDouble("command.distance") < configFile.getDouble("max-distance"))
				{
					for(String line : messagesFile.getStringList("errors.command-distance-under-max"))
					{
						line = ColorUtils.color(line);
						opsyDistanceMain.getLogger().severe(line);
					}
				}
			}
			else
			{
				if(opsyDistanceMain.getDistanceCommand().getPlayerActivated().contains(victim.getUniqueId().toString()))
				{
					for(String line : ColorUtils.color(messagesFile.getStringList("alert.under-max")))
					{
						line = line.replace("{attacker}", damager.getName());
						line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
						victim.sendMessage(prefix + line);
					}
				}
			}
		}
	}
}
