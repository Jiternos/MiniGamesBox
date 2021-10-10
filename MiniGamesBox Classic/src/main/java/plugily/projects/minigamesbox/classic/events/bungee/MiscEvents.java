/*
 * MiniGamesBox - Library box with massive content that could be seen as minigames core.
 * Copyright (C)  2021  Plugily Projects - maintained by Tigerpanzer_02 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package plugily.projects.minigamesbox.classic.events.bungee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import plugily.projects.minigamesbox.classic.Main;
import plugily.projects.minigamesbox.classic.api.event.game.PlugilyGameStateChangeEvent;

/**
 * @author Tigerpanzer_02
 * <p>
 * Created at 09.10.2021
 */
public class MiscEvents implements Listener {

  private final Main plugin;

  public MiscEvents(Main plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent e) {
    if(!plugin.getServer().hasWhitelist() || e.getResult() != PlayerLoginEvent.Result.KICK_WHITELIST) {
      return;
    }
    if(e.getPlayer().hasPermission(plugin.getPermissionsManager().getJoinFullGames())) {
      e.setResult(PlayerLoginEvent.Result.ALLOWED);
    }

    if(!ArenaRegistry.getArenas().isEmpty()) {
      e.getPlayer().teleport(ArenaRegistry.getArenas().get(ArenaRegistry.getBungeeArena()).getLobbyLocation());
    }
  }

  @EventHandler
  public void onGameStateChange(PlugilyGameStateChangeEvent e) {
    switch(e.getArenaState()) {
      case WAITING_FOR_PLAYERS:
        plugin.getServer().setWhitelist(false);
        break;
      case IN_GAME:
        plugin.getServer().setWhitelist(e.getArena().getMaximumPlayers() <= e.getArena().getPlayers().size());
        break;
      case ENDING:
        plugin.getServer().setWhitelist(false);
        break;
      case STARTING:
      case RESTARTING:
      default:
        break;
    }
    if(e.getArenaState() == ArenaState.ENDING) {
      plugin.getServer().setWhitelist(false);
    }
  }

}
