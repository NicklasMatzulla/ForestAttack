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

package de.nicklasmatzulla.commons.api.db;

import de.nicklasmatzulla.commons.api.db.util.*;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The DatabaseManager allows you to manage the database.
 * In addition, basic builders are provided for better database management.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface IDatabaseManager {

    /**
     * Create a new table in the database.
     * @return {@link ICreateTable}
     */
    @NotNull ICreateTable createTable();

    /**
     * Execute an insert statement.
     * @return {@link IInsert}
     */
    @NotNull IInsert insert();

    /**
     * Execute an update statement.
     * @return {@link IUpdate}
     */
    @NotNull IUpdate update();

    /**
     * Execute a delete statement.
     * @return {@link IDelete}
     */
    @NotNull IDelete delete();

    /**
     * Execute a select statement.
     * @return {@link ISelect}
     */
    @NotNull ISelect select();

    /**
     * Execute a custom sql query.
     * @param query The sql query to execute.
     * @return True if the query was successful, false otherwise.
     * @throws SQLException If an error occurs during a database query.
     */
    boolean execute(@NotNull String query) throws SQLException;

    /**
     * Execute a custom SQL query and return the result.
     * @param query The sql query to execute.
     * @return Optional {@link ResultSet}, present if the query was successful.
     * @throws SQLException If an error occurs during a database query.
     */
    @NotNull Optional<ResultSet> queryResultSet(@NotNull String query) throws SQLException;

    /**
     * Execute a custom sql query and return the result as {@link IResult}.
     * @param query The sql query to execute.
     * @return Optional {@link ResultSet}, present if the query was successful.
     * @throws SQLException If an error occurs during a database query.
     */
    @NotNull Optional<IResult> queryResult(@NotNull String query) throws SQLException;

    /**
     * Execute a custom SQL query and return the generated keys.
     * @param query The sql query to execute.
     * @return Optional {@link ResultSet}, present if the query was successful.
     * @throws SQLException If an error occurs during a database query.
     */
    @NotNull Optional<ResultSet> queryGeneratedKeys(@NotNull String query) throws SQLException;

}
