package com.CaptainWolfie.RankUp.Inventories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.CaptainWolfie.RankUp.Main;
import com.CaptainWolfie.RankUp.FileManager.FileManager;
import com.CaptainWolfie.RankUp.Players.RankUpPlayer;

public class InventoryLoader implements Listener
{
	private Inventory inv;

	private JavaPlugin pl;

	private Player p = null;

	public InventoryLoader( JavaPlugin pl, Player p )
	{
		this.inv = null;
		this.pl = pl;
		this.p = p;
		Main.registerEvent( this );
		// Create a new inventory, with no owner (as this isn't a real inventory), a
		// size of nine, called example
		int size = InventoryItems.getItems().size();
		int rows = ( size + ( 9 % size ) ) / 9 + 2;

		if( size < 9 )
			rows = 3;

		inv = Bukkit.createInventory( null, rows * 9,
				pl.getConfig().getString( "inventory-name" ).replaceAll( "&", "§" ) );

		initializeItems();
	}

	public InventoryLoader( JavaPlugin pl )
	{
		this.inv = null;
		this.pl = pl;
	}

	// You can call this whenever you want to put the items in
	public void initializeItems()
	{
		// variables for the top column
		String topName = FileManager.itemsConfig.getString( "Top.Name" ).replaceAll( "&", "§" );
		String[] topLore = FileManager.itemsConfig.getString( "Top.Lore" ).replaceAll( "&", "§" ).split( "\n" );
		String topType = FileManager.itemsConfig.getString( "Top.Type" );
		byte topData = (byte) FileManager.itemsConfig.getInt( "Top.Data" );
		String topSkullName = FileManager.itemsConfig.getString( "Top.SkullName" );

		// variables for the bottom column
		String bottomName = FileManager.itemsConfig.getString( "Bottom.Name" ).replaceAll( "&", "§" );
		String[] bottomLore = FileManager.itemsConfig.getString( "Bottom.Lore" ).replaceAll( "&", "§" ).split( "\n" );
		String bottomType = FileManager.itemsConfig.getString( "Bottom.Type" );
		byte bottomData = (byte) FileManager.itemsConfig.getInt( "Bottom.Data" );
		String bottomSkullName = FileManager.itemsConfig.getString( "Bottom.SkullName" );

		for( int i = 0; i < 9; i++ )
		{
			inv.setItem( i, createGuiItem( Material.valueOf( topType ), topName, topData, topSkullName, topLore ) );
		}

		for( int i = inv.getSize() - 9; i < inv.getSize(); i++ )
		{
			inv.setItem( i, createGuiItem( Material.valueOf( bottomType ), bottomName, bottomData, bottomSkullName,
					bottomLore ) );
		}

		List< InventoryItem > items = InventoryItems.getItems();

		for( int i = 0; i < items.size(); i++ )
		{
			InventoryItem item = items.get( i );

			String skull = item.skullname;
			String lore;

			String pRanks = RankUpPlayer.getPlayerRanks( p.getName() );
			if( pRanks != null )
			{
				String[] ranks = pRanks.split( ";" );
				List< String > list = Arrays.asList( ranks );

				if( list.contains( item.realName ) )
				{
					lore = StringEscapeUtils.unescapeJava( item.unlocked_lore );

					lore += "\n" + pl.getConfig().getString( "unlocked" ).replaceAll( "&", "§" );
				} else
				{
					lore = StringEscapeUtils.unescapeJava( item.locked_lore );
					lore += "\n" + pl.getConfig().getString( "locked" ).replaceAll( "&", "§" );
					skull = pl.getConfig().getString( "locked-skull" );
				}

				String[] ranks1 = RankUpPlayer.getPlayerRanks( p.getName() ).split( ";" );

				// check which of those ranks do exist
				List< String > finalRanks = new ArrayList< String >();
				for( String rank : ranks1 )
				{
					for( InventoryItem item1 : InventoryItems.getItems() )
					{
						// add only the ranks that exist
						if( item1.realName.equals( rank ) )
							finalRanks.add( rank );
					}
				}

				ranks1 = new String[ finalRanks.size() ];
				ranks1 = finalRanks.toArray( ranks1 );

				String finalstr = "";
				for( String i2 : ranks1 )
				{
					finalstr += i2 + ";";
				}
				// save and delete the last char
				FileManager.databaseConfig.set( p.getName(), finalstr.replaceFirst( ".$", "" ) );
				try
				{
					FileManager.databaseConfig.save( FileManager.databaseFile );
				} catch( IOException e1 )
				{
					e1.printStackTrace();
				}

				String playerRank = ranks1[ ranks1.length - 1 ];

				if( ! pl.getConfig().getString( "last-rank" ).equals( playerRank ) )
				{

					String rankUpTo = FileManager.ranksConfig.getString( "Ranks." + playerRank + ".RankUpTo" );

					if( Main.econ.getBalance( p ) >= item.price )
					{
						if( rankUpTo.equals( item.realName ) )
						{
							lore += "\n" + pl.getConfig().getString( "rankup-available" )
									.replaceAll( "%next_rank%",
											FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Name" ) )
									.replaceAll( "%rank%",
											FileManager.ranksConfig.getString( "Ranks." + playerRank + ".Name" ) );
						}
					}
				}

			} else
			{
				lore = StringEscapeUtils.unescapeJava( item.locked_lore );
				lore += "\n" + pl.getConfig().getString( "locked" ).replaceAll( "&", "§" );
				skull = pl.getConfig().getString( "locked-skull" );

				String rankUpTo = Main.plugin.getConfig().getString( "first-rank" );

				if( Main.econ.getBalance( p ) >= item.price )
				{
					if( rankUpTo.equals( item.realName ) )
					{
						lore += "\n" + pl.getConfig().getString( "rankup-available" )
								.replaceAll( "%next_rank%",
										FileManager.ranksConfig.getString( "Ranks." + rankUpTo + ".Name" ) )
								.replaceAll( "%rank%", "None" );
					}
				}
			}

			inv.setItem( 9 + i, createGuiItem( Material.valueOf( item.type ), item.name.replaceAll( "&", "§" ),
					item.data, skull, lore.replaceAll( "&", "§" ).split( "\n" ) ) );
		}
	}

	// Nice little method to create a gui item with a custom name, and description
	protected ItemStack createGuiItem( final Material material, final String name, byte data, final String skullName,
			final String... lore )
	{
		final ItemStack item = new ItemStack( material, 1, data );
		final ItemMeta meta = item.getItemMeta();
		if( material.equals( Material.SKULL_ITEM ) )
		{

			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

			skullMeta.setOwner( skullName );
			skullMeta.setDisplayName( name );

			skullMeta.setLore( Arrays.asList( lore ) );
			item.setItemMeta( skullMeta );
		} else
		{
			// Set the name of the item
			meta.setDisplayName( name );

			// Set the lore of the item
			meta.setLore( Arrays.asList( lore ) );

			item.setItemMeta( meta );
		}

		return item;
	}

	// You can open the inventory with this
	public void openInventory( final HumanEntity ent )
	{
		ent.openInventory( inv );
	}

	@EventHandler
	public void onInventoryClick( final InventoryClickEvent e )
	{
		if( ! e.getInventory().equals( inv ) )
			return;

		e.setCancelled( true );

		final ItemStack clickedItem = e.getCurrentItem();

		// verify current item is not null
		if( clickedItem == null || clickedItem.getType() == Material.AIR )
			return;

		final Player p = (Player) e.getWhoClicked();

		List< InventoryItem > items = InventoryItems.getItems();

		// Using slots click is a best option for your inventory click's
		try
		{
			String playerRank = null;

			try
			{
				String[] ranks1 = RankUpPlayer.getPlayerRanks( p.getName() ).split( ";" );

				playerRank = ranks1[ ranks1.length - 1 ];

				if( ! pl.getConfig().getString( "last-rank" ).equals( playerRank ) )
				{

					String rankUpTo = FileManager.ranksConfig.getString( "Ranks." + playerRank + ".RankUpTo" );

					if( rankUpTo.equals( items.get( e.getRawSlot() - 9 ).realName ) )
					{
						RankUpPlayer.RankUp( p.getName() );
					}
				}
			} catch( NullPointerException e2 )
			{
				String rankUpTo = pl.getConfig().getString( "first-rank" );
				p.sendMessage( rankUpTo );

				if( rankUpTo.equals( items.get( e.getRawSlot() - 9 ).realName ) )
				{
					RankUpPlayer.RankUp( p.getName() );
				}

			}
		} catch( IndexOutOfBoundsException e1 )
		{
			if( e.getRawSlot() <= 9 )
			{
				String topCommand = FileManager.itemsConfig.getString( "Top.Command" ).replaceAll( "%player%",
						p.getName() );

				if( ! topCommand.equals( "" ) )
					Bukkit.dispatchCommand( Bukkit.getConsoleSender(), topCommand );
			}

			if( e.getRawSlot() >= inv.getSize() - 9 )
			{
				String bottomCommand = FileManager.itemsConfig.getString( "Bottom.Command" ).replaceAll( "%player%",
						p.getName() );

				if( ! bottomCommand.equals( "" ) )
					Bukkit.dispatchCommand( Bukkit.getConsoleSender(), bottomCommand );
			}
		}
	}

	@EventHandler
	public void onInventoryClick( final InventoryDragEvent e )
	{
		if( e.getInventory().equals( inv ) )
		{
			e.setCancelled( true );
		}
	}

	@EventHandler
	public void onClose( final InventoryCloseEvent e )
	{
		if( e.getInventory().equals( inv ) )
		{
			// optimize the server
			Main.unregisterEvent( this );
			inv = null;
			pl = null;
			p = null;
			Runtime.getRuntime().gc();
		}
	}
}