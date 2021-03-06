/*
 *  REALMBanManager: Used for issuing and maintaining bans on bukkit server
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

import java.util.UUID;

import net.bunnehrealm.realmbanmanager.MainClass;
import net.bunnehrealm.realmbanmanager.utils.BanManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Rory Finnegan on 5/8/14.
 */
public class CommandListener implements Listener {
	MainClass MainClass;
	BanManager BanManager;

	public CommandListener(MainClass MainClass) {
		this.MainClass = MainClass;
	}

	public CommandListener(BanManager BanManager) {
		this.BanManager = BanManager;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		BanManager bm = new BanManager(MainClass);
		Player cs = e.getPlayer();
		String cmd = e.getMessage();
		String[] args = cmd.split(" ");
		if (cmd.startsWith("/ban")) {
			e.setCancelled(true);
			if ((args.length == 0) || (args.length == 1)) {
				cs.sendMessage(ChatColor.RED + "Correct Usage: "
						+ ChatColor.AQUA + "/ban <player> [reason]");
				return;
			} else {
				if (cs.hasPermission("BanManager.ban") || cs.isOp()
						|| !(cs instanceof Player)) {
					StringBuilder sb = new StringBuilder();
					for (int x = 2; x < args.length; x++) {
						sb.append(" ").append(args[x]);
					}

					String ban_msg = sb.toString();
					Ban(Bukkit.getPlayer(args[1]).getUniqueId(), ban_msg);
				} else {
					cs.sendMessage(ChatColor.RED
							+ "You do not have permission to use this command!");
				}
			}
		} 
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

	
}
