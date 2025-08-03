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

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LoginResponse {

    private static final String DENY_REASON = "You are not allowed to join using this hostname.";
    private static final String FAIL_REASON = "An error occurred during login. Please contact the server administrator.";

    private final Type type;
    @Nullable
    private final UUID uuid;
    @Nullable
    private final String username;
    @Nullable
    private final String reason;

    private LoginResponse(Type type, @Nullable UUID uuid, @Nullable String username, @Nullable String reason) {
        this.type = type;
        this.uuid = uuid;
        this.username = username;
        this.reason = reason;
    }

    static LoginResponse ignore() {
        return new LoginResponse(Type.IGNORE, null, null, null);
    }

    static LoginResponse allow(UUID newUUID, String newUsername) {
        return new LoginResponse(Type.ALLOW, newUUID, newUsername, null);
    }

    static LoginResponse deny() {
        return new LoginResponse(Type.DENY, null, null, DENY_REASON);
    }

    static LoginResponse fail() {
        return new LoginResponse(Type.DENY, null, null, FAIL_REASON);
    }

    public Type type() {
        return type;
    }

    public @Nullable UUID uuid() {
        return uuid;
    }

    public @Nullable String username() {
        return username;
    }

    public @Nullable String reason() {
        return reason;
    }

    public enum Type {
        IGNORE,
        ALLOW,
        DENY
    }

}
