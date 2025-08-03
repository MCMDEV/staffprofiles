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

package de.mcmdev.staffprofiles.permission;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.Blocking;

import java.util.UUID;

public class LuckPermsPermissionProvider implements PermissionProvider {

    private final LuckPerms luckPerms = LuckPermsProvider.get();

    @Override
    @Blocking
    public boolean hasPermission(UUID uuid, String permission) {
        return luckPerms.getUserManager().loadUser(uuid).join()
                .getCachedData()
                .getPermissionData()
                .checkPermission(permission)
                .asBoolean();
    }
}
