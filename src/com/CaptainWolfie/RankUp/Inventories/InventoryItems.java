package com.CaptainWolfie.RankUp.Inventories;

import java.util.ArrayList;
import java.util.List;

import com.CaptainWolfie.RankUp.FileManager.FileManager;

public class InventoryItems
{

	public static List< InventoryItem > getItems()
	{
		Object[] items = FileManager.ranksConfig.getConfigurationSection( "Ranks" ).getKeys( false ).toArray();

		List< InventoryItem > itemss = new ArrayList< InventoryItem >();

		for( Object i : items )
		{
			String name = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".Name" );
			String command = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".Command" );

			String lockedlore = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".Locked-Lore" );
			String unlockedlore = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".Unlocked-Lore" );
			String type = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".Type" );
			String skull = FileManager.ranksConfig.getString( "Ranks." + i.toString() + ".SkullName" );

			int price = FileManager.ranksConfig.getInt( "Ranks." + i.toString() + ".Price" );
			byte data = (byte) FileManager.ranksConfig.getInt( "Ranks." + i.toString() + ".Data" );
			itemss.add( new InventoryItem( i.toString(), name, command, price, lockedlore, unlockedlore, type, skull, data ) );
		}
		return itemss;
	}

}
