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

import de.mcmdev.staffprofiles.permission.PermissionProvider;
import org.jetbrains.annotations.Blocking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.UUID;

final class Staffprofiles {

    private static final Logger LOGGER = LoggerFactory.getLogger(Staffprofiles.class);

    private final ConfigurationData configurationData;
    private final PermissionProvider permissionProvider;

    private Staffprofiles(ConfigurationData configurationData, PermissionProvider permissionProvider) {
        this.configurationData = configurationData;
        this.permissionProvider = permissionProvider;
    }

    public static Staffprofiles create(Path dataDirectory, PermissionProvider permissionProvider) throws Exception {
        ConfigurationData configurationData = new ConfigurationLoader().load(dataDirectory);
        return new Staffprofiles(configurationData, permissionProvider);
    }

    @Blocking
    LoginResponse login(LoginRequest loginRequest) {
        try {
            if (!matchesHostname(loginRequest.hostname())) {
                return LoginResponse.ignore();
            }
        } catch (Exception e) {
            // If the hostname isn't matched yet, we should assume it's not the staff hostname and let the player in.
            LOGGER.warn("Failed to load login response from staffprofiles.", e);
            return LoginResponse.ignore();
        }

        try {
            if (!hasPermission(loginRequest.uuid())) {
                return LoginResponse.deny();
            }

            UUID newUUID = transformUUID(loginRequest.uuid());
            String newUsername = transformUsername(loginRequest.username());

            LOGGER.info("User {} ({}) has logged in as {} ({})", loginRequest.username(), loginRequest.uuid(), newUsername, newUUID);
            return LoginResponse.allow(newUUID, newUsername);
        } catch (Exception e) {
            LOGGER.warn("Failed to load login response from staffprofiles.", e);
            return LoginResponse.fail();
        }
    }

    private boolean matchesHostname(String hostname) {
        return configurationData.hostRegex().matcher(hostname).matches();
    }

    private UUID transformUUID(UUID uuid) throws IllegalArgumentException {
        return UUID.fromString(configurationData.uuidTransformer().transform(uuid.toString()));
    }

    private String transformUsername(String username) {
        return configurationData.usernameTransformer().transform(username);
    }

    @Blocking
    private boolean hasPermission(UUID uuid) {
        return permissionProvider.hasPermission(uuid, configurationData.permission());
    }

}
