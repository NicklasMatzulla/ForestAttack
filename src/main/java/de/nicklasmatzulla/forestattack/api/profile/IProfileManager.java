/*
 * MIT License
 *
 * Copyright (c) 2024 Nicklas Matzulla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.nicklasmatzulla.forestattack.api.profile;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * The profile manager enables to Load and create player profiles.
 */
public interface IProfileManager {

    /**
     * Create a new player profile.
     * @param uniqueId The unique id of the Minecraft player that should be linked to the profile.
     * @return The created player profile.
     * @throws SQLException If the database connection was interrupted, or if an error occurred while creating the player profile.
     */
    @NotNull IProfile create(@NotNull UUID uniqueId) throws SQLException;

    /**
     * Get an already loaded player profile, load it if it is not cached.
     * @param uniqueId The unique id of the Minecraft account linked with a profile.
     * @return The cached or loaded player profile.
     * @throws SQLException If the database connection was interrupted or an error occurred while loading the player data.
     */
    @NotNull Optional<IProfile> getOrLoad(@NotNull UUID uniqueId) throws SQLException;

}
