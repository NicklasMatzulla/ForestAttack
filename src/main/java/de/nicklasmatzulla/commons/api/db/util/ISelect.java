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
import java.util.Optional;

/**
 * The implementation of this class makes it possible to build and execute a SELECT SQL statement.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ISelect {

    /**
     * Sets the table for the SELECT statement.
     * A table must be set, otherwise the statement cannot be executed.
     * @param table the name of the table
     * @return instance
     */
    @NotNull ISelect table(@NotNull String table);

    /**
     * Adds a column to be queried.
     * @param column columns that should be queried.
     * @return instance
     */
    @NotNull ISelect column(@NotNull String column);

    /**
     * Adds a condition for the SELECT statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param quotes Specifies whether quotes should be inserted automatically for the value.
     * @param operator The comparison operator (e.g. =, !=, ...)
     * @return instance
     */
    @NotNull ISelect where(@NotNull String column, @NotNull String value, @NotNull String operator, boolean quotes);

    /**
     * Adds a condition for the SELECT statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param operator The comparison operator (e.g. =, !=, ...)
     * @return instance
     */
    @NotNull ISelect where(@NotNull String column, @NotNull String value, @NotNull String operator);

    /**
     * Adds a condition for the SELECT statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @param quotes Specifies whether quotes should be inserted automatically for the value.
     * @return instance
     */
    @NotNull ISelect where(@NotNull String column, @NotNull String value, boolean quotes);

    /**
     * Adds a condition for the SELECT statement.
     * @param column The name of the column to filter by.
     * @param value The value to filter by.
     * @return instance
     */
    @NotNull ISelect where(@NotNull String column, @NotNull String value);

    /**
     * Execute this SELECT statement.
     * @return {@link Optional} of {@link IResult}, present when success and at least one row were found.
     * @throws SQLException If an error occurs during a database query.
     */
    @NotNull Optional<IResult> execute() throws SQLException;

    /**
     * Built the sql query.
     * @return The built sql query.
     */
    @NotNull String query();

}
