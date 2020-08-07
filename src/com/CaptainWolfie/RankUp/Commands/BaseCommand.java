package com.CaptainWolfie.RankUp.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.CaptainWolfie.RankUp.Inventories.InventoryItem;
import com.CaptainWolfie.RankUp.Inventories.InventoryItems;
import com.CaptainWolfie.RankUp.Inventories.InventoryLoader;
import com.CaptainWolfie.RankUp.Players.RankUpPlayer;

public class BaseCommand implements CommandExecutor
{

	private final JavaPlugin pl;

	public BaseCommand( JavaPlugin pl )
	{
		this.pl = pl;
		init();
	}

	private void init()
	{
	}

	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
	{
		if( sender instanceof Player )
		{
			Player p = (Player) sender;

			if( p.hasPermission( pl.getConfig().getString( "Use-Permission" ) ) )
			{
				RankUpPlayer.makeConfig( p.getName() );
				InventoryLoader loader = new InventoryLoader( pl, p );
				loader.openInventory( p );
			} else
			{
				p.sendMessage( pl.getConfig().getString( "NoPermissionsMessage" ).replaceAll( "&", "§" )
						.replaceAll( "%permission%", pl.getConfig().getString( "Use-Permission" ) ) );
			}
		} else
		{
			System.out.println( "Available Ranks:" );
			List< InventoryItem > items = InventoryItems.getItems();
			for( InventoryItem item : items )
			{
				System.out.println( item.realName );
			}
		}
		return true;
	}

}
