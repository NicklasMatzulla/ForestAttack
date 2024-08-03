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

import de.nicklasmatzulla.commons.api.db.IDatabaseManager;
import de.nicklasmatzulla.commons.api.db.util.IResult;
import de.nicklasmatzulla.commons.api.db.util.ISelect;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;

@SuppressWarnings("DuplicatedCode")
public final class SelectImpl implements ISelect {

    private final IDatabaseManager databaseManager;
    private String table;
    private final StringBuilder columns = new StringBuilder();
    private final StringBuilder condition = new StringBuilder();

    public SelectImpl(final @NotNull IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public @NotNull ISelect table(final @NotNull String table) {
        this.table = table;
        return this;
    }

    @Override
    public @NotNull ISelect column(final @NotNull String column) {
        if (!this.columns.isEmpty()) {
            this.columns.append(", ");
        }
        this.columns.append(column);
        return this;
    }

    @Override
    public @NotNull ISelect where(final @NotNull String column, @NotNull String value, final @NotNull String operator, final boolean quotes) {
        if (!this.condition.isEmpty()) {
            this.condition.append(" AND ");
        }
        if (quotes) {
            value = "'" + value + "'";
        }
        this.condition.append(column).append(" ").append(operator).append(" ").append(value);
        return this;
    }

    @Override
    public @NotNull ISelect where(final @NotNull String column, final @NotNull String value, final @NotNull String operator) {
        return where(column, value, operator, true);
    }

    @Override
    public @NotNull ISelect where(final @NotNull String column, final @NotNull String value, final boolean quotes) {
        return where(column, value, "=", quotes);
    }

    @Override
    public @NotNull ISelect where(final @NotNull String column, final @NotNull String value) {
        return where(column, value, "=", true);
    }

    @Override
    public @NotNull Optional<IResult> execute() throws SQLException {
        return this.databaseManager.queryResult(query());
    }

    @Override
    public @NotNull String query() {
        return "SELECT " + (this.columns.isEmpty() ? "*" : this.columns.toString()) + " FROM " + this.table + (this.condition.isEmpty() ? "" : " WHERE " + this.condition);
    }
}
