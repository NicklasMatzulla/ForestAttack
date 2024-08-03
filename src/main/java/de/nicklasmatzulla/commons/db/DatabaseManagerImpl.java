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

package de.nicklasmatzulla.commons.db;

import com.zaxxer.hikari.HikariDataSource;
import de.nicklasmatzulla.commons.api.db.IDatabaseManager;
import de.nicklasmatzulla.commons.api.db.util.*;
import de.nicklasmatzulla.commons.db.util.*;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SqlSourceToSinkFlow"})
@AllArgsConstructor
public final class DatabaseManagerImpl implements IDatabaseManager {

    private final HikariDataSource dataSource;

    @Override
    public @NotNull ICreateTable createTable() {
        return new CreateTableImpl(this);
    }

    @Override
    public @NotNull IInsert insert() {
        return new InsertImpl(this);
    }

    @Override
    public @NotNull IUpdate update() {
        return new UpdateImpl(this);
    }

    @Override
    public @NotNull IDelete delete() {
        return new DeleteImpl(this);
    }

    @Override
    public @NotNull ISelect select() {
        return new SelectImpl(this);
    }

    @Override
    public boolean execute(final @NotNull String query) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public @NotNull Optional<ResultSet> queryResultSet(final @NotNull String query) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query);
             final ResultSet result = statement.executeQuery()) {
            return Optional.ofNullable(result);
        }
    }

    @Override
    public @NotNull Optional<IResult> queryResult(@NotNull String query) throws SQLException {
        final Optional<ResultSet> optResultSet = queryResultSet(query);
        if (optResultSet.isEmpty()) {
            return Optional.empty();
        }
            final ResultSet resultSet = optResultSet.get();
            final List<IRow> rows = new ArrayList<>();
            while (resultSet.next()) {
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final HashMap<String, Object> columnsData = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    final String columnName = metaData.getColumnName(i);
                    final Object columnValue = resultSet.getObject(i);
                    columnsData.put(columnName, columnValue);
                }
                rows.add(new RowImpl(columnsData));
            }
            if (!rows.isEmpty()) {
                final ResultImpl result = new ResultImpl(rows);
                return Optional.of(result);
            }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ResultSet> queryGeneratedKeys(@NotNull String query) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.execute();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            return Optional.ofNullable(generatedKeys);
        }
    }

}
