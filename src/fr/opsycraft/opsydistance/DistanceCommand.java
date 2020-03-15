package fr.opsycraft.opsydistance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DistanceCommand implements CommandExecutor, Listener {

	private Main main;
	private List<String> playerActivated = new ArrayList<>();
	private FilesManager filesManager;
	private YamlConfiguration messagesFile;
	private YamlConfiguration configsFile;
	private String prefix;
	
	public DistanceCommand(Main main)
	{
		this.main = main;
		this.filesManager = main.getFilesManger();
		this.messagesFile = this.filesManager.getMessagesFile();
		this.configsFile = this.filesManager.getConfigsFile();
		this.prefix = ChatColor.translateAlternateColorCodes('&', messagesFile.getString("prefix"));
	}
	
	public List<String> getPlayerActivated()
	{
		return this.playerActivated;
	}
	
	public void setPlayerActivated(List<String> newList)
	{
		this.playerActivated = newList;
	}
	
	private static Method getHandleMethod;
    private static Field pingField;

    public static int getPing(Player player) {
        try {
            if (getHandleMethod == null) {
                getHandleMethod = player.getClass().getDeclaredMethod("getHandle");
                getHandleMethod.setAccessible(true);
            }
            Object entityPlayer = getHandleMethod.invoke(player);
            if (pingField == null) {
                pingField = entityPlayer.getClass().getDeclaredField("ping");
                pingField.setAccessible(true);
            }
            int ping = pingField.getInt(entityPlayer);

            return ping > 0 ? ping : 0;
        } catch (Exception e) {
            return 1;
        }
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
							for(String line : messagesFile.getStringList("hit-checker-enabled"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								player.sendMessage(prefix + line);
							}
						}
						else
						{
							playerActivated.remove(player.getUniqueId().toString());
							filesManager.saveDatabase();
							for(String line : messagesFile.getStringList("hit-checker-disabled"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								player.sendMessage(prefix + line);
							}
						}
					}
					else
					{
						for(String line : messagesFile.getStringList("errors.not-enought-permissions"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
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
								for(String line : messagesFile.getStringList("hit-checker-enabled"))
								{
									line = ChatColor.translateAlternateColorCodes('&', line);
									player.sendMessage(prefix + line);
								}
							}
							else
							{
								for(String line : messagesFile.getStringList("errors.already-enabled"))
								{
									line = ChatColor.translateAlternateColorCodes('&', line);
									player.sendMessage(prefix + line);
								}
							}
						}
						else
						{
							for(String line : messagesFile.getStringList("errors.not-enought-permissions"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
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
								for(String line : messagesFile.getStringList("hit-checker-disabled"))
								{
									line = ChatColor.translateAlternateColorCodes('&', line);
									player.sendMessage(prefix + line);
								}
							}
							else
							{
								for(String line : messagesFile.getStringList("errors.already-disabled"))
								{
									line = ChatColor.translateAlternateColorCodes('&', line);
									player.sendMessage(prefix + line);
								}
							}
						}
						else
						{
							for(String line : messagesFile.getStringList("errors.not-enought-permissions"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								player.sendMessage(prefix + line);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("help"))
					{
						for(String line : messagesFile.getStringList("plugin-help"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							player.sendMessage(prefix + line);
						}
					}
					else if(args[0].equalsIgnoreCase("reload"))
					{
						if(player.hasPermission("pvpdistance.reload"))
						{
							Bukkit.getPluginManager().disablePlugin(main);
							Bukkit.getPluginManager().enablePlugin(main);
							for(String line : messagesFile.getStringList("plugin-reloaded"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								player.sendMessage(prefix + line);
							}
						}
						else
						{
							for(String line : messagesFile.getStringList("errors.not-enought-permissions"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								player.sendMessage(prefix + line);
							}
						}
					}
				}
			}
			else
			{
				for(String line : messagesFile.getStringList("errors.not-a-player"))
				{
					line = ChatColor.translateAlternateColorCodes('&', line);
					sender.sendMessage(prefix + line);
				}
			}
		}
		return false;
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
			if(distance >= configsFile.getDouble("max-distance"))
			{
				for(String line : messagesFile.getStringList("alert.over-max"))
				{
					line = ChatColor.translateAlternateColorCodes('&', line);
					line = line.replace("{attacker}", damager.getName());
					line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
					victim.sendMessage(prefix + line);
				}
				for(Player staff : Bukkit.getServer().getOnlinePlayers())
				{
					if(staff.hasPermission("pvpdistance.alert"))
					{
						for(String line : messagesFile.getStringList("alert.staff-alert"))
						{
							line = ChatColor.translateAlternateColorCodes('&', line);
							line = line.replace("{attacker}", damager.getName());
							line = line.replace("{victim}", victim.getName());
							line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
							victim.sendMessage(prefix + line);
						}
					}
				}
				if(Boolean.TRUE.equals(configsFile.getBoolean("cancel-damage")))
				{
					e.setCancelled(true);
				}
				if(Boolean.TRUE.equals(configsFile.getBoolean("kick.enabled")
					&& configsFile.getDouble("kick.distance") >= configsFile.getDouble("max-distance"))
					&& distance >= configsFile.getDouble("kick.distance"))
				{
					double tps = Lag.getTPS();
					int ping = getPing(damager);
					if(!(tps < configsFile.getDouble("kick.tps-treshold") || ping > configsFile.getInt("kick.ping-treshold")))
					{
						if(configsFile.getString("kick.message") != null)
						{
							damager.kickPlayer(prefix + ChatColor.translateAlternateColorCodes('&', configsFile.getString("kick.message")));
						}
						else
						{
							main.getLogger().severe("Please, make sure you set the kick message in the configs file!");
						}
					}
				}
				else if(Boolean.TRUE.equals(configsFile.getBoolean("kick.enabled"))
						&& configsFile.getDouble("kick.distance") < configsFile.getDouble("max-distance"))
				{
					for(String line : messagesFile.getStringList("errors.kick-distance-under-max"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						main.getLogger().severe(line);
					}
				}
			}
			else
			{
				if(playerActivated.contains(victim.getUniqueId().toString()))
				{
					for(String line : messagesFile.getStringList("alert.under-max"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						line = line.replace("{attacker}", damager.getName());
						line = line.replace("{distance}", new DecimalFormat("#.##").format(distance));
						victim.sendMessage(prefix + line);
					}
				}
			}
		}
	}
}
