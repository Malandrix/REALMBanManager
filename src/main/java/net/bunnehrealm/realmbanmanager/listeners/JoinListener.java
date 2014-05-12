/*
 *  REALMBanManager: Used for issuing and maintaing bans on bukkit server
    Copyright (C) 2014  Rory Finnegan 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	Contact me at bunnehrealm@gmail.com
 */
package net.bunnehrealm.realmbanmanager.listeners;

import java.math.BigInteger;

import net.bunnehrealm.realmbanmanager.MainClass;
import net.bunnehrealm.realmbanmanager.utils.BanManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class JoinListener implements Listener {
	MainClass MainClass;
	BanManager bm = new BanManager(MainClass);

	public JoinListener(MainClass MainClass) {
		this.MainClass = MainClass;
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		Player p = Bukkit.getPlayer(e.getName());
		if (!(MainClass.bans.contains(p.getUniqueId().toString()))) {
			MainClass.bans.set(p.getUniqueId().toString() + ".permabanned", false);
			MainClass.bans.set(p.getUniqueId().toString() + ".unbantime", false);
			MainClass.bans.set(p.getUniqueId().toString() + ".reason", "");
		}


		if (MainClass.bans.getBoolean(p.getUniqueId() + ".permabanned")) {
			e.disallow(Result.KICK_BANNED, ChatColor.RED
					+ "You have been banned for:"
					+ getReason(p.getUniqueId().toString()));
		}
		else if(MainClass.bans.getLong(p.getUniqueId().toString() + ".unbantime") > MainClass.bans.getLong("Timer")){
			e.disallow(Result.KICK_BANNED, ChatColor.RED
					+ "You have been banned for:"
					+ getReason(p.getUniqueId().toString()));
		}

		
	}

	public String getReason(String player_UUID) {
		MainClass.loadBans();
		String reason = null;
		reason = MainClass.bans.getString(player_UUID + ".reason");
		return reason;
	}

}
