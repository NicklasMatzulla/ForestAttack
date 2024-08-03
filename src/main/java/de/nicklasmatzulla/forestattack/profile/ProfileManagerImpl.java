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
import de.nicklasmatzulla.commons.api.db.util.IResult;
import de.nicklasmatzulla.commons.api.db.util.IRow;
import de.nicklasmatzulla.forestattack.api.profile.IProfile;
import de.nicklasmatzulla.forestattack.api.profile.IProfileManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManagerImpl implements IProfileManager {
    private final ConcurrentHashMap<UUID, IProfile> cachedProfiles = new ConcurrentHashMap<>();
    private final IDatabaseManager databaseManager;

    public ProfileManagerImpl(final @NotNull Logger logger, final @NotNull IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        try {
            databaseManager.createTable()
                    .name("fa_profiles")
                    .column("uuid UUID PRIMARY KEY NOT NULL")
                    .column("claimed_starter_kit BOOLEAN NOT NULL DEFAULT false")
                    .execute();
        } catch (final @NotNull SQLException e) {
            logger.error("Failed to create player profile table", e);
        }
    }

    @Override
    public @NotNull IProfile create(@NotNull UUID uniqueId) throws SQLException {
        this.databaseManager.insert()
                .table("fa_profiles")
                .column("uuid", uniqueId.toString())
                .execute();
        final ProfileImpl profile = new ProfileImpl(this.databaseManager, uniqueId, false);
        this.cachedProfiles.put(uniqueId, profile);
        return profile;
    }

    @Override
    public @NotNull Optional<IProfile> getOrLoad(@NotNull UUID uniqueId) throws SQLException {
        IProfile profile = this.cachedProfiles.get(uniqueId);
        if (profile != null) {
            return Optional.of(profile);
        }
        final Optional<IResult> optResult = this.databaseManager.select()
                .table("fa_profiles")
                .where("uuid", uniqueId.toString())
                .execute();
        if (optResult.isEmpty()) {
            return Optional.empty();
        }
        final IResult result = optResult.get();
        final IRow row = result.getFirstRow();
        final boolean starterKitClaimed = row.getBoolean("claimed_starter_kit").orElseThrow();
        profile = new ProfileImpl(this.databaseManager, uniqueId, starterKitClaimed);
        this.cachedProfiles.put(uniqueId, profile);
        return Optional.of(profile);
    }
}
