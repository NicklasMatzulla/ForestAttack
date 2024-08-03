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
import java.util.UUID;

/**
 * The implementation represents a player profile.
 */
@SuppressWarnings("unused")
public interface IProfile {

    /**
     * Get the unique id of the Minecraft player linked to the profile.
     * @return The unique id of the Minecraft player linked to the profile.
     */
    @NotNull UUID getUniqueId();

    /**
     * Each player is provided with a starter kit to simplify the start of the game. Receive information on whether the player has already claimed the starter kit.
     * @return True if the player has claimed the starter kit, false otherwise.
     */
    boolean hasStarterKitClaimed();

    /**
     * Overwrite the information whether the player has claimed the starter kit.
     * @param claimed True if the player has claimed the starter kit, false otherwise.
     * @throws SQLException If the database connection was interrupted or an error occurred while processing the player data.
     */
    void setStarterKitClaimed(boolean claimed) throws SQLException;

}
