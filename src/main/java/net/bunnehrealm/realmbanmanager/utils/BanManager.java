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

package net.bunnehrealm.realmbanmanager.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.bunnehrealm.realmbanmanager.MainClass;
import net.bunnehrealm.realmbanmanager.listeners.CommandListener;

public class BanManager {

	MainClass MainClass;
	CommandListener CommandListener;

	public BanManager(MainClass MainClass) {
		this.MainClass = MainClass;
	}

	public BanManager(CommandListener CommandListener) {
		this.CommandListener = CommandListener;
	}

	public String getReason(String player_UUID) {
		MainClass.loadBans();
		String reason = null;
		reason = MainClass.bans.getString(player_UUID + ".reason");
		return reason;
	}

	public void unBan(String player_UUID) {
		MainClass.loadBans();
		MainClass.bans.set(player_UUID + ".permabanned", null);
		MainClass.bans.set(player_UUID + ".banned", null);
		MainClass.bans.set(player_UUID + ".unbantime", null);
		MainClass.bans.set(player_UUID + ".reason", null);
		MainClass.saveBans();
	}

	public void Ban(UUID uuid, String reason) {
		MainClass.loadBans();
		MainClass.bans.set(uuid + ".permabanned", true);
		MainClass.bans.set(uuid + ".reason", reason);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getUniqueId().equals(uuid)) {
				p.kickPlayer(reason);
			}
		}
		MainClass.saveBans();
	}

	public void tempBan(Player p, UUID player_UUID, String time, String reason) {
		time.replace("m", "*60");
		time.replace("h", "*60*60");
		time.replace("d", "*60*60*24");
		try{
			Integer.parseInt(time);
			}
		catch(Exception e){
			p.sendMessage("Incorrect time format!");
			return;
		}
		int finaltime = Integer.parseInt(time);
		MainClass.loadBans();
		MainClass.bans.set(player_UUID + ".banned", true);
		MainClass.bans.set(player_UUID + ".unbantime", MainClass.timer + finaltime);
		MainClass.bans.set(player_UUID + ".reason", reason);
		MainClass.saveBans();
	}

	public int loadTime() {
		MainClass.loadBans();
		int timer = MainClass.bans.getInt("Timer");
		MainClass.saveBans();
		return timer;
	}

	public void saveTime(int timer) {
		MainClass.loadBans();
		MainClass.bans.set("Timer", timer);
		MainClass.saveBans();
	}

}
