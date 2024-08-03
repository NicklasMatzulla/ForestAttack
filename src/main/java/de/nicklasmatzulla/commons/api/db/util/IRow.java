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

package de.nicklasmatzulla.commons.api.db.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * The implementation of this class represents a single row of an SQL response.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface IRow {

    /**
     * Get a specific object from the result of a database request.
     * @param column The name of the column to retrieve the object from.
     * @return The object retrieved from the specified column, or null if it doesn't exist.
     */
    @NotNull Optional<Object> get(@NotNull String column);

    /**
     * Get a byte value from the result of a database request.
     * @param column The name of the column to retrieve the byte value from.
     * @return The byte value retrieved from the specified column.
     */
    @NotNull Optional<Byte> getByte(@NotNull String column);

    /**
     * Get a short value from the result of a database request.
     * @param column The name of the column to retrieve the short value from.
     * @return The short value retrieved from the specified column.
     */
    @NotNull Optional<Short> getShort(@NotNull String column);

    /**
     * Get an integer value from the result of a database request.
     * @param column The name of the column to retrieve the integer value from.
     * @return The integer value retrieved from the specified column.
     */
    @NotNull Optional<Integer> getInteger(@NotNull String column);

    /**
     * Get a long value from the result of a database request.
     * @param column The name of the column to retrieve the long value from.
     * @return The long value retrieved from the specified column.
     */
    @NotNull Optional<Long> getLong(@NotNull String column);

    /**
     * Get a float value from the result of a database request.
     * @param column The name of the column to retrieve the float value from.
     * @return The float value retrieved from the specified column.
     */
    @NotNull Optional<Float> getFloat(@NotNull String column);

    /**
     * Get a double value from the result of a database request.
     * @param column The name of the column to retrieve the double value from.
     * @return The double value retrieved from the specified column.
     */
    @NotNull Optional<Double> getDouble(@NotNull String column);

    /**
     * Get a boolean value from the result of a database request.
     * @param column The name of the column to retrieve the boolean value from.
     * @return The boolean value retrieved from the specified column.
     */
    @NotNull Optional<Boolean> getBoolean(@NotNull String column);

    /**
     * Get a string from the result of a database request.
     * @param column The name of the column to retrieve the string from.
     * @return The string retrieved from the specified column, or null if it doesn't exist.
     */
    @NotNull Optional<String> getString(@NotNull String column);

    /**
     * Get a UUID from the result of a database request.
     * @param column The name of the column to retrieve the UUID from.
     * @return The UUID retrieved from the specified column, or null if it doesn't exist.
     */
    @NotNull Optional<UUID> getUUID(@NotNull String column);

}
