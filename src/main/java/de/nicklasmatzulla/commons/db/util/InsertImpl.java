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
import de.nicklasmatzulla.commons.api.db.util.IInsert;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;

public final class InsertImpl implements IInsert {

    private final IDatabaseManager databaseManager;
    private String table;
    private final StringBuilder columns = new StringBuilder();
    private final StringBuilder values = new StringBuilder();

    public InsertImpl(final @NotNull IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public @NotNull IInsert table(final @NotNull String table) {
        this.table = table;
        return this;
    }

    @Override
    public @NotNull IInsert column(final @NotNull String column, @NotNull String value, final boolean quotes) {
        if (!this.columns.isEmpty()) {
            this.columns.append(", ");
            this.values.append(", ");
        }
        this.columns.append(column);
        if (quotes) {
            value = "'" + value + "'";
        }
        this.values.append(value);
        return this;
    }

    @Override
    public @NotNull IInsert column(final @NotNull String column, final @NotNull String value) {
        return column(column, value, true);
    }

    @Override
    public boolean execute() throws SQLException {
        return this.databaseManager.execute(query());
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public @NotNull Optional<Long> executeId() throws SQLException {
        final Optional<ResultSet> optResultSet = this.databaseManager.queryGeneratedKeys(query());
        if (optResultSet.isPresent()) {
            try {
                final ResultSet generatedKeys = optResultSet.get();
                final ResultSetMetaData metaData = generatedKeys.getMetaData();
                int index = 1;
                while (generatedKeys.next()) {
                    final String columnName = metaData.getColumnName(index);
                    if (columnName.contains("id")) {
                        if (!generatedKeys.wasNull()) {
                            return Optional.of(generatedKeys.getLong(index));
                        }
                    }
                    index++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

    @Override
    public @NotNull String query() {
        return "INSERT INTO " + this.table + " (" + this.columns + ") VALUES (" + this.values + ")";
    }
}
