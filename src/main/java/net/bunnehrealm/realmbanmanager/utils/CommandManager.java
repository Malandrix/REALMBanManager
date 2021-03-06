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

import net.bunnehrealm.realmbanmanager.MainClass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	MainClass MainClass;
	net.bunnehrealm.realmbanmanager.utils.BanManager BanManager = new net.bunnehrealm.realmbanmanager.utils.BanManager(
			MainClass);

	public CommandManager(MainClass MainClass) {
		this.MainClass = MainClass;
	}

	public CommandManager(
			net.bunnehrealm.realmbanmanager.utils.BanManager BanManager) {
		this.BanManager = BanManager;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		Player p = (Player) cs;
		if (label.equalsIgnoreCase("tempban")) {
			if (args.length < 2) {
				cs.sendMessage(ChatColor.RED + "Correct Usage "
						+ ChatColor.AQUA
						+ "/tempban <player> <numberm/h/d> [Reason] ");
				return false;
			} else {
				if (cs.hasPermission("BanManager.tempban") || cs.isOp()
						|| !(cs instanceof Player)) {
					StringBuilder sb = new StringBuilder();
					for (int x = 2; x < args.length; x++) {
						sb.append(" ").append(args[x]);
					}

					String ban_msg = sb.toString();
					tempBan(p, Bukkit.getPlayer(args[1]).getUniqueId(),
							args[0], ban_msg);
				} else {
					cs.sendMessage(ChatColor.RED
							+ "You do not have permission to use this command!");
				}
			}
		}
		return false;
	}

	public void tempBan(Player p, UUID player_UUID, String time, String reason) {
		String finaltime = time;
		int timeint = 0;
		if (time.endsWith("m")) {
			time = time.replace("m", "");
			p.sendMessage(time);
			try {
				Integer.parseInt(time);
			} catch (Exception e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.RED + "Incorrect time format!");
				return;
			}
			timeint = Integer.parseInt(time) *20* 60;
		}
		else if (time.endsWith("s")) {
			time = time.replace("s", "");
			try {
				Integer.parseInt(time);
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "Incorrect time format!");
				return;
			}
			timeint = Integer.parseInt(time) *20;
		}
		else if (time.endsWith("h")) {
			time = time.replace("h", "");
			try {
				Integer.parseInt(time);
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "Incorrect time format!");
				return;
			}
			timeint = Integer.parseInt(time) *20* 60 * 60;
		}
		else if (time.endsWith("d")) {
			time = time.replace("d", "");
			try {
				Integer.parseInt(time);
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "Incorrect time format!");
				return;
			}
			timeint = Integer.parseInt(time) *20* 60  * 60 * 24;
		}


		MainClass.loadBans();
		MainClass.bans.set(player_UUID + ".unbantime", MainClass.timer
				+ timeint);
		MainClass.bans.set(player_UUID + ".reason", reason);
		if (p.getUniqueId().equals(player_UUID)) {
			p.kickPlayer(ChatColor.DARK_PURPLE + "You have been banned for "
					+ ChatColor.LIGHT_PURPLE + finaltime + ChatColor.DARK_PURPLE + " because you \n"
					+ ChatColor.GOLD + reason);
		}
		MainClass.saveBans();
	}

}
