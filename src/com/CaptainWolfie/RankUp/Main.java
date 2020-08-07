package com.CaptainWolfie.RankUp;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.CaptainWolfie.RankUp.Commands.BaseCommand;
import com.CaptainWolfie.RankUp.Events.EventsHandler;
import com.CaptainWolfie.RankUp.FileManager.FileManager;

import net.milkbowl.vault.economy.Economy;

/*
 * author: Matt Zafeiriou / CaptainWolfie
 * 
 * published: 7/8/2020
 * 
 */
public class Main extends JavaPlugin
{

	public static Economy econ = null;

	public static JavaPlugin plugin;

	@Override
	public void onEnable()
	{
		plugin = this;
		FileManager.init( this );
		this.saveDefaultConfig();
		BaseCommand bs = new BaseCommand( this );
		this.getCommand( "rank" ).setExecutor( bs );
		this.getCommand( "rankup" ).setExecutor( bs );
		this.getCommand( "ranks" ).setExecutor( bs );
		plugin.getServer().getPluginManager().registerEvents( new EventsHandler(), plugin );

		if( ! setupEconomy() )
		{
			this.getLogger()
					.severe( "This plugin requires Vault and Essentials and couldn\'t be found. Disabling plugin.." );
			Bukkit.getPluginManager().disablePlugin( this );
			return;
		}

		Logger log = this.getLogger();
		log.info( "RankUp System Enabled!" );
	}

	public static void registerEvent( Listener listener )
	{
		plugin.getServer().getPluginManager().registerEvents( listener, plugin );
	}

	public static void unregisterEvent( Listener listener )
	{
		HandlerList.unregisterAll( listener );
	}

	private boolean setupEconomy()
	{
		if( Bukkit.getPluginManager().getPlugin( "Vault" ) == null )
		{
			return false;
		}

		RegisteredServiceProvider< Economy > rsp = getServer().getServicesManager().getRegistration( Economy.class );
		if( rsp == null )
		{
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
