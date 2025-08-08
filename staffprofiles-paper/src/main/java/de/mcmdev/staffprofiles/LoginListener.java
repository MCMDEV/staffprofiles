/*
 * staffprofiles
 * Copyright (C) 2025 MCMDEV
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.mcmdev.staffprofiles;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LoginListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginListener.class);

    private final Staffprofiles staffprofiles;

    LoginListener(Staffprofiles staffprofiles) {
        this.staffprofiles = staffprofiles;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        PlayerProfile playerProfile = event.getPlayerProfile();
        if (playerProfile.getId() == null || playerProfile.getName() == null) {
            // Strange profile, we should ignore it.
            LOGGER.warn("Invalid profile detected during login, ignoring");
            return;
        }

        LoginRequest loginRequest = new LoginRequest(
                event.getHostname(),
                playerProfile.getName(),
                playerProfile.getId()
        );
        LoginResponse loginResponse = staffprofiles.login(loginRequest);

        switch (loginResponse.type()) {
            case IGNORE -> {
            }
            case DENY -> {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(loginResponse.reason()));
            }
            case ALLOW -> {
                playerProfile.complete();
                PlayerProfile newProfile = Bukkit.createProfile(loginResponse.uuid(), loginResponse.username());
                newProfile.setProperties(playerProfile.getProperties());
                event.setPlayerProfile(newProfile);
            }
        }
    }
}
