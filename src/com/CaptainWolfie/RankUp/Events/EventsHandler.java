package com.CaptainWolfie.RankUp.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.CaptainWolfie.RankUp.Players.RankUpPlayer;

public class EventsHandler implements Listener
{

	@EventHandler
	public void onJoin( PlayerJoinEvent e )
	{
		RankUpPlayer.makeConfig( e.getPlayer().getName() );
	}

}
