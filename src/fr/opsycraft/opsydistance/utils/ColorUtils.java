package fr.opsycraft.opsydistance.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public final class ColorUtils
{
	private ColorUtils() {}
	
	/*
	 * A simple way to color a string or a list of string
	 * Where @textToColor is indeed the line of text to color
	 */
	
	public static final String color(String textToColor)
	{
		return ChatColor.translateAlternateColorCodes('&', textToColor);
	}
	
	public static final List<String> color(List<String> textToColor)
	{
		List<String> coloredText = new ArrayList<>();
		
		for(String line : textToColor)
		{
			line = ChatColor.translateAlternateColorCodes('&', line);
			coloredText.add(line);
		}
		return coloredText;
	}
	
	
	/*
	 * A simple way to discolor a string or a list of string
	 * Where @textToDiscolor is indeed the line of text to discolor
	 */
	
	public static final String discolor(String textToDiscolor)
	{
		return ChatColor.stripColor(textToDiscolor);
	}
	
	public static final List<String> discolor(List<String> textToDiscolor)
	{
		List<String> discoloredText = new ArrayList<>();
		
		for(String line : textToDiscolor)
		{
			line = ChatColor.stripColor(line);
			discoloredText.add(line);
		}
		return discoloredText;
	}
}
