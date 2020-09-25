/*
 * This file is part of BeezigForge.
 *
 * BeezigForge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BeezigForge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BeezigForge.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.api;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IBeezigService {
    void registerUserIndicator(Function<UUID, Integer> callback);
    void registerTitle(Function<Map.Entry<String, Integer>, String> callback);
    void registerFormatNumber(Function<Long, String> callback);
    void registerTranslate(Function<String, String> callback);
    void registerNormalizeMapName(Function<String, String> callback);
    void registerGetRegion(Supplier<String> callback);
    void registerTranslateFormat(Function<Pair<String, Object[]>, String> callback);
    void registerBeezigDir(Supplier<File> callback);
    void registerGetSetting(Function<String, Object> callback);
    void registerSetSetting(Consumer<Map.Entry<String, Object>> callback);
    void registerGetOverrides(Function<UUID, Map<String, Object>> callback);

    void setOnHive(boolean update);
    void setCurrentGame(String game);
    void addCommands(List<Object> commands);
    void loadConfig(File beezigDir);
    void autovoteShuffle(List<String> favorites);
    void displayWelcomeGui();
}
