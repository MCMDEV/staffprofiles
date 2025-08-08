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

import com.google.inject.Inject;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.GameProfileRequestEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.GameProfile;
import de.mcmdev.staffprofiles.permission.LuckPermsPermissionProvider;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "staffprofiles",
        name = "staffprofiles",
        authors = "MCMDEV",
        dependencies = {@Dependency(id = "luckperms")},
        description = "Assigns staff members a different game profile if\n" +
                "they are joining using a specific hostname.",
        url = "https://github.com/MCMDEV/staffprofiles",
        version = PluginConstants.VERSION
)
public class StaffprofilesVelocity {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffprofilesVelocity.class);

    private final ProxyServer proxyServer;
    private final Path dataDirectory;

    private Staffprofiles staffprofiles;

    @Inject
    public StaffprofilesVelocity(ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            staffprofiles = Staffprofiles.create(dataDirectory, new LuckPermsPermissionProvider());
        } catch (Exception e) {
            LOGGER.error("An error occurred while enabling staffprofiles plugin", e);
            return;
        }

        proxyServer.getEventManager().register(this, PreLoginEvent.class, (AwaitingEventExecutor<PreLoginEvent>) this::onLogin);
        proxyServer.getEventManager().register(this, GameProfileRequestEvent.class, (AwaitingEventExecutor<GameProfileRequestEvent>) this::onGameProfileRequest);
    }

    @Nullable
    private EventTask onLogin(PreLoginEvent event) {
        Optional<String> rawVirtualHost = event.getConnection().getRawVirtualHost();
        if (rawVirtualHost.isEmpty()) {
            return null;
        }
        String hostname = rawVirtualHost.get();
        return EventTask.async(() -> {
            if(event.getUniqueId() == null) return;
            LoginRequest loginRequest = new LoginRequest(hostname, event.getUsername(), event.getUniqueId());
            LoginResponse loginResponse = staffprofiles.login(loginRequest);

            if (loginResponse.type() != LoginResponse.Type.DENY) return;

            event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text(loginResponse.reason())));
        });
    }

    @Nullable
    private EventTask onGameProfileRequest(GameProfileRequestEvent event) {
        Optional<String> rawVirtualHost = event.getConnection().getRawVirtualHost();
        if (rawVirtualHost.isEmpty()) {
            return null;
        }
        String hostname = rawVirtualHost.get();
        return EventTask.async(() -> {
            LoginRequest loginRequest = new LoginRequest(hostname, event.getOriginalProfile().getName(), event.getOriginalProfile().getId());
            LoginResponse loginResponse = staffprofiles.login(loginRequest);

            if (loginResponse.type() != LoginResponse.Type.ALLOW) return;

            event.setGameProfile(new GameProfile(loginResponse.uuid(), loginResponse.username(), event.getOriginalProfile().getProperties()));
        });
    }

}
