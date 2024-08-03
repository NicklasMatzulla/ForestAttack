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
 * The implementation of this class makes it possible to build and execute a CREATE TABLE SQL statement.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ICreateTable {

    /**
     * Sets the table name for the CREATE TABLE statement.
     * A table name must be set, otherwise the statement cannot be executed.
     * @param name the name of the table
     * @return instance
     */
    @NotNull ICreateTable name(@NotNull String name);

    /**
     * Adds a column to the CREATE TABLE statement.
     * @param column The name and options of the column.
     * @return instance
     */
    @NotNull ICreateTable column(@NotNull String column);

    /**
     * Execute this CREATE TABLE statement.
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
