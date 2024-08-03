/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Nicklas Matzulla
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

package de.nicklasmatzulla.commons.db.util;

import de.nicklasmatzulla.commons.api.db.util.IRow;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class RowImpl implements IRow {

    private final Map<String, Object> columns;

    public RowImpl(final @NotNull Map<String, Object> columns) {
        this.columns = columns;
    }

    @Override
    public @NotNull Optional<Object> get(final @NotNull String column) {
        return Optional.ofNullable(this.columns.get(column));
    }

    @Override
    public @NotNull Optional<Byte> getByte(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Byte value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Short> getShort(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Short value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Integer> getInteger(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Integer value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Long> getLong(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Long value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Float> getFloat(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Float value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Double> getDouble(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Double value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Boolean> getBoolean(final @NotNull String column) {
        if (this.columns.get(column) instanceof final Boolean value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<String> getString(final @NotNull String column) {
        if (this.columns.get(column) instanceof final String value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<UUID> getUUID(final @NotNull String column) {
        if (this.columns.get(column) instanceof final UUID value) {
            return Optional.of(value);
        }
        return Optional.empty();
    }
}
