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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

final class ConfigurationLoader {

    private static final String CONFIG_RESOURCE_NAME = "config.json";

    ConfigurationData load(Path dataDirectory) throws IOException {
        Path configPath = dataDirectory.resolve(CONFIG_RESOURCE_NAME);
        ensureConfigExists(configPath);

        try (BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            return getGson().fromJson(reader, ConfigurationData.class);
        }
    }

    private void ensureConfigExists(Path path) throws IOException {
        ensureParentDirectoryExists(path.getParent());

        if (!Files.exists(path)) {
            copyDefaultConfiguration(path);
        }
    }

    private void ensureParentDirectoryExists(Path parentPath) throws IOException, IllegalStateException {
        if (Files.exists(parentPath)) {
            if (!Files.isDirectory(parentPath)) {
                throw new IllegalStateException(parentPath + " is not a directory");
            }

            return;
        }

        Files.createDirectories(parentPath);
    }

    private void copyDefaultConfiguration(Path path) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
            if (inputStream == null) {
                throw new IllegalStateException("Default configuration file not found");
            }

            Files.copy(inputStream, path);
        }
    }

    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Pattern.class, new PatternDeserializer())
                .registerTypeAdapter(StringTransformer.class, new StringTransformer.Adapter())
                .setPrettyPrinting()
                .create();
    }

}
