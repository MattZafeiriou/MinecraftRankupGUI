package com.CaptainWolfie.RankUp.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager
{

	private static JavaPlugin plugin = null;
	public static FileConfiguration ranksConfig, itemsConfig, databaseConfig;
	public static File ranksFile, itemsFile, databaseFile;

	public static void init( JavaPlugin pl )
	{
		plugin = pl;
		createConfigs();
	}

	private static void createConfigs()
	{
		ranksFile = createFile( "Ranks",
				"Ranks:\r\n" + "    # can be any name you want like 'Pro' or 'Noob' (you)\r\n" + "  '1':\r\n"
						+ "    Name: \"&7Elite Rank\"\r\n" + "    Locked-Lore: |-\r\n" + "      \r\n"
						+ "      &7\\u272C &fYou do not have this Rank!\r\n" + "      \r\n"
						+ "      &7\\u272C &f&l/sethome &8(&f&l3&8)\r\n" + "      \r\n"
						+ "      &7\\u272C &fPrice&8: &f$15,000\r\n" + "      \r\n" + "    Unlocked-Lore: |-\r\n"
						+ "      \r\n" + "      &7\\u272C &fYou do have this rank\r\n" + "      \r\n"
						+ "      &7\\u272C &fYou are not pro, you are just\r\n" + "      \r\n" + "      &fbroken\r\n"
						+ "      &fsad.\r\n" + "\r\n" + "    Command: \"time set 1000\"\r\n" + "    Price: 15000\r\n"
						+ "    Type: \"SKULL_ITEM\"\r\n" + "    Data: 3\r\n" + "    RankUpTo: \"2\"\r\n"
						+ "    SkullName: \"CaptainWolfieGR\"\r\n" + "  '2':\r\n" + "    Name: \"Super oof\"\r\n"
						+ "    Locked-Lore: |-\r\n" + "      \r\n" + "      &7\\u272C &fYou do not have this Rank!\r\n"
						+ "      \r\n" + "      &7\\u272C &f&l/sethome &8(&f&l5&8)\r\n" + "      \r\n"
						+ "      &7\\u272C &fPrice&8: &f$25,000\r\n" + "      \r\n" + "    Unlocked-Lore: |-\r\n"
						+ "      \r\n" + "      &7\\u272C &fYou do have this rank\r\n" + "      \r\n"
						+ "      7\\u272C &fYou are not pro, you are just\r\n" + "      \r\n" + "      &fbroken\r\n"
						+ "      &fsad.\r\n" + "      \r\n" + "    Command: \"time set 0\"\r\n" + "    Price: 25000\r\n"
						+ "    Type: \"SKULL_ITEM\"\r\n" + "    Data: 3\r\n" + "    RankUpTo: \"3\"\r\n"
						+ "    SkullName: \"Hypixel\"\r\n" + "  '3':\r\n" + "    Name: \"Super duper oof\"\r\n"
						+ "    Locked-Lore: |-\r\n" + "      \r\n" + "      &7\\u272C &fYou do not have this Rank!\r\n"
						+ "      \r\n" + "      &7\\u272C &f&l/sethome &8(&f&l7&8)\r\n" + "      \r\n"
						+ "      &7\\u272C &fPrice&8: &f$50,000\r\n" + "      \r\n" + "    Unlocked-Lore: |-\r\n"
						+ "      \r\n" + "      &7\\u272C &fYou do have this rank\r\n" + "      \r\n"
						+ "      7\\u272C &fYou are not broken, you are just\r\n" + "      \r\n" + "      &fpro\r\n"
						+ "      &fnot sad.\r\n" + "      \r\n" + "    Command: \"give %player% steak\"\r\n"
						+ "    Price: 50000\r\n" + "    Type: \"SKULL_ITEM\"\r\n" + "    Data: 3\r\n"
						+ "    SkullName: \"Pro\"\r\n" + "" );
		ranksConfig = createConfig( ranksFile );

		itemsFile = createFile( "Items",
				"Top:\r\n" + "    Name: \"&9&m+--&c[&bHecate&c]&9&m--+\"\r\n" + "    Lore: |-\r\n"
						+ "      &7Join this server\r\n" + "      &7right now or death\r\n"
						+ "    Command: \"msg %player% You can change this action in the Items.yml :D\"\r\n"
						+ "    Type: \"STAINED_GLASS_PANE\"\r\n" + "    Data: 7\r\n" + "    SkullName: \"none\"\r\n"
						+ "\r\n" + "Bottom:\r\n" + "    Name: \"&9&m+--&c[&bHecate&c]&9&m--+\"\r\n" + "    Lore: |-\r\n"
						+ "      &7Join this server\r\n" + "      &7right now or death\r\n"
						+ "    Command: \"vote %player%\"\r\n" + "    Type: \"STAINED_GLASS_PANE\"\r\n"
						+ "    Data: 7\r\n" + "    SkullName: \"none\"" );
		itemsConfig = createConfig( itemsFile );

		databaseFile = createFile( "Database", "" );
		databaseConfig = createConfig( databaseFile );
	}

	private static FileConfiguration createConfig( File file )
	{
		return YamlConfiguration.loadConfiguration( file );
	}

	private static File createFile( String name, String data )
	{
		File newFile = new File( plugin.getDataFolder(), name + ".yml" );
		if( ! newFile.exists() )
		{
			newFile.getParentFile().mkdirs();
			try
			{
				newFile.createNewFile();
				writeToFile( newFile, data );
			} catch( IOException e )
			{
				e.printStackTrace();
			}
		}
		return newFile;
	}

	private static void writeToFile( File file, String data )
	{
		try
		{
			FileWriter myWriter = new FileWriter( file );
			myWriter.write( data );
			myWriter.close();
		} catch( IOException e )
		{
			System.out.println( "An error occurred." );
			e.printStackTrace();
		}
	}

}
