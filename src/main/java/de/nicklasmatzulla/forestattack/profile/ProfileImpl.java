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

package de.nicklasmatzulla.forestattack.profile;

import de.nicklasmatzulla.commons.api.db.IDatabaseManager;
import de.nicklasmatzulla.forestattack.api.profile.IProfile;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProfileImpl implements IProfile {
    private final IDatabaseManager databaseManager;
    private final UUID uniqueId;
    private final AtomicBoolean starterKitClaimed;

    public ProfileImpl(final @NotNull IDatabaseManager databaseManager, final @NotNull UUID uniqueId, final boolean starterKitClaimed) {
        this.databaseManager = databaseManager;
        this.uniqueId = uniqueId;
        this.starterKitClaimed = new AtomicBoolean(starterKitClaimed);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public boolean hasStarterKitClaimed() {
        return this.starterKitClaimed.get();
    }

    @Override
    public void setStarterKitClaimed(boolean claimed) throws SQLException {
        this.databaseManager.update()
                .table("fa_profiles")
                .set("claimed_starter_kit", String.valueOf(claimed), false)
                .where("uuid", this.uniqueId.toString())
                .execute();
        this.starterKitClaimed.set(claimed);
    }
}
