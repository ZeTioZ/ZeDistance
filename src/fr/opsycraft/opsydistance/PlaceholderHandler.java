package fr.opsycraft.opsydistance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlaceholderHandler
{	
	private static String attackerPlaceholder = "{attacker}";
	private static String victimPlaceholder = "{victim}";
	private static String distancePlaceholder = "{distance}";
	
	private PlaceholderHandler() {
		throw new IllegalStateException("Placeholder Handler");
	}
	
	public static String sendPlaceholderMessage(String mainString)
	{
		String message = mainString;
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
	
	public static String sendPlaceholderMessage(String mainString, double distance)
	{
		String message = mainString;
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace(distancePlaceholder, new DecimalFormat("#.##").format(distance));
		return message;
	}
	
	public static String sendPlaceholderMessage(String mainString, Player damager)
	{
		String message = mainString;
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace(attackerPlaceholder, damager.getName());
		return message;
	}
	
	public static String sendPlaceholderMessage(String mainString, Player damager, Player victim)
	{
		String message = mainString;
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace(attackerPlaceholder, damager.getName());
		message = message.replace(victimPlaceholder, victim.getName());
		return message;
	}
	
	public static String sendPlaceholderMessage(String mainString, double distance, Player damager)
	{
		String message = mainString;
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace(distancePlaceholder, new DecimalFormat("#.##").format(distance));
		message = message.replace(attackerPlaceholder, damager.getName());
		return message;
	}
	
	public static List<String> sendPlaceholderMessageList(List<String> mainStringList)
	{
		List<String> convertedMessageList = new ArrayList<>();
		for(String line : mainStringList)
		{
			convertedMessageList.add(sendPlaceholderMessage(line));
		}
		
		return convertedMessageList;
	}
}