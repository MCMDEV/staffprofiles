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

import de.mcmdev.staffprofiles.permission.LuckPermsPermissionProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffprofilesPaper extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            Staffprofiles staffprofiles = Staffprofiles.create(getDataPath(), new LuckPermsPermissionProvider());
            Bukkit.getPluginManager().registerEvents(new LoginListener(staffprofiles), this);
        } catch (Exception e) {
            getSLF4JLogger().error("An error occurred while enabling staffprofiles plugin", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
