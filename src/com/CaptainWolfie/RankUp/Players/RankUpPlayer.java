package com.CaptainWolfie.RankUp.Players;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.CaptainWolfie.RankUp.Main;
import com.CaptainWolfie.RankUp.FileManager.FileManager;

public class RankUpPlayer
{

	public static void makeConfig( String name )
	{

	}

	public static String getPlayerRanks( String name )
	{
		return FileManager.databaseConfig.getString( name );
	}

	@SuppressWarnings( "deprecation" )
	public static boolean RankUp( String name )
	{
		String playerRank = null;

		try
		{
			String[] ranks1 = getPlayerRanks( name ).split( ";" );

			playerRank = ranks1[ ranks1.length - 1 ];
		} catch( NullPointerException e )
		{
			playerRank = "";
		}

		if( ! Main.plugin.getConfig().getString( "last-rank" ).equals( playerRank ) ) // if not last rank
		{
			String rankUpTo = "";
			// vars
			if( playerRank.equals( "" ) )
			{
				rankUpTo = Main.plugin.getConfig().getString( "first-rank" );
			} else
				rankUpTo = FileManager.ranksConfig.getString( "Ranks." + playerRank + ".RankUpTo" );
			int price = FileManager.ranksConfig.getInt( "Ranks." + rankUpTo + ".Price" );
			if( Main.econ.getBalance( Bukkit.getPlayer( name ) ) >= price ) // if enough money
			{
				// close inventory
				Bukkit.getPlayer( name ).closeInventory();

				String rankUpBroadcast = Main.plugin.getConfig().getString( "rankup-broadcast" )
						.replaceAll( "%next_rank%", FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Name" ) )
						.replaceAll( "%rank%", FileManager.ranksConfig.getString( "Ranks." + playerRank + ".Name" ) )
						.replaceAll( "%player%", name ).replaceAll( "&", "§" );

				// rank up broadcast
				if( ! rankUpBroadcast.equals( "" ) )
					for( Player p : Bukkit.getOnlinePlayers() )
					{
						p.sendMessage( rankUpBroadcast );
					}

				String rankUpMsg = Main.plugin.getConfig().getString( "rankup-message" )
						.replaceAll( "%next_rank%", FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Name" ) )
						.replaceAll( "%rank%", FileManager.ranksConfig.getString( "Ranks." + playerRank + ".Name" ) )
						.replaceAll( "%player%", name ).replaceAll( "&", "§" );
				// rank up message
				if( ! rankUpMsg.equals( "" ) )
					Bukkit.getPlayer( name ).sendMessage( rankUpMsg );

				// execute command
				String command = FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Command" );
				if( ! command.equals( "" ) )
					Bukkit.dispatchCommand( Bukkit.getConsoleSender(), command.replaceAll( "%player%", name ) );

				// remove money
				Main.econ.withdrawPlayer( Bukkit.getPlayer( name ), price );
				// update config file
				FileManager.databaseConfig.set( name, FileManager.databaseConfig.get( name ) + ";" + rankUpTo );
				try
				{
					FileManager.databaseConfig.save( FileManager.databaseFile );
				} catch( IOException e )
				{
					e.printStackTrace();
				}

				// screen title
				Bukkit.getPlayer( name ).sendTitle( Main.plugin.getConfig().getString( "rankup-message" )
						.replaceAll( "%next_rank%", FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Name" ) )
						.replaceAll( "%rank%", FileManager.ranksConfig.getString( "Ranks." + playerRank + ".Name" ) )
						.replaceAll( "%player%", name ).replaceAll( "&", "§" ), "" );

				// sound
				Bukkit.getPlayer( name ).playSound( Bukkit.getPlayer( name ).getLocation(),
						Sound.valueOf( Main.plugin.getConfig().getString( "RankUp-Sound" ) ), 1f, 1f );

				return true;
			}
		}
		return false;
	}

	public RankUpPlayer( String name )
	{
		makeConfig( name );
	}
}
