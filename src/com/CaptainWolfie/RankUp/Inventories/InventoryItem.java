package com.CaptainWolfie.RankUp.Inventories;

public class InventoryItem
{

	public String realName;
	public String name = null;
	public String command = null;
	public int price;
	public String locked_lore;
	public String unlocked_lore;
	public String type;
	public String skullname;
	public byte data;

	public InventoryItem( String realName, String name, String command, int price, String locked_lore, String unlocked_lore, String type,
			String skullname, byte data )
	{
		this.realName = realName;
		this.name = name;
		this.command = command;
		this.price = price;
		this.locked_lore = locked_lore;
		this.unlocked_lore = unlocked_lore;
		this.type = type;
		this.skullname = skullname;
		this.data = data;
	}

}
