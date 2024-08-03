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

import java.sql.SQLException;

/**
 * The implementation of this class makes it possible to build and execute a DELETE SQL statement.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface IDelete {

    /**
     * Sets the table for the DELETE statement.
     * A table must be set, otherwise the statement cannot be executed.
     * @param table the name of the table
     * @return instance
     */
    @NotNull IDelete table(@NotNull String table);

    /**
     * Adds a condition for the DELETE statement.
     *
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param quotes Specifies whether quotes should be inserted automatically for the value.
     * @param operator The comparison operator (e.g. =, !=, ...)
     * @return instance
     */
    @NotNull IDelete where(@NotNull String column, @NotNull String value, @NotNull String operator, boolean quotes);

    /**
     * Adds a condition for the DELETE statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param operator The comparison operator (e.g. =, !=, ...)
     * @return instance
     */
    @NotNull IDelete where(@NotNull String column, @NotNull String value, @NotNull String operator);

    /**
     * Adds a condition for the DELETE statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param quotes Specifies whether quotes should be inserted automatically for the value.
     * @return instance
     */
    @NotNull IDelete where(@NotNull String column, @NotNull String value, boolean quotes);

    /**
     * Adds a condition for the DELETE statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @return instance
     */
    @NotNull IDelete where(@NotNull String column, @NotNull String value);

    /**
     * Execute this DELETE statement.
     * @return success status
     * @throws SQLException If an error occurs during a database query.
     */
    boolean execute() throws SQLException;

    /**
     * Built the sql query.
     * @return The built SQL query.
     */
    @NotNull String query();

}
