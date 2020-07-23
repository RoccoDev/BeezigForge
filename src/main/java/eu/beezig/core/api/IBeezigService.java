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

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface IBeezigService {
    void registerUserIndicator(Function<UUID, Integer> callback);
    void registerTitle(Function<String, String> callback);
    void registerFormatNumber(Function<Long, String> callback);
    void registerTranslate(Function<String, String> callback);
    void registerNormalizeMapName(Function<String, String> callback);

    void setOnHive(boolean update);
    void setCurrentGame(String game);
    void addCommands(List<Object> commands);
    void loadConfig(File beezigDir);
    void autovoteShuffle(List<String> favorites);
}
