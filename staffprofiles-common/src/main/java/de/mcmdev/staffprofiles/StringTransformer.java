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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class StringTransformer {

    private final Pattern pattern;
    private final String replacement;

    private StringTransformer(String matching, String replacement) throws PatternSyntaxException {
        this.pattern = Pattern.compile(matching);
        this.replacement = replacement;
    }

    public static StringTransformer parse(String string) {
        String[] split = string.split("/", 2);
        return new StringTransformer(split[0], split[1]);
    }

    public String transform(String input) {
        return pattern.matcher(input).replaceAll(replacement);
    }

    static class Adapter implements JsonDeserializer<StringTransformer> {

        @Override
        public StringTransformer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return StringTransformer.parse(json.getAsString());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }
    }

}
