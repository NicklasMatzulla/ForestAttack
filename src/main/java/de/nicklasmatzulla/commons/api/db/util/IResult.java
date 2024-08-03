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

import java.util.List;
import java.util.Optional;

/**
 * The implementation of this class provides an SQL response.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface IResult {

    /**
     * Get all rows of the query.
     * @return {@link List} of {@link IRow}
     */
    @NotNull List<IRow> getRows();

    /**
     * Get a specific row of the query by index.
     * @param index Index of the row.
     * @return {@link Optional} of {@link IRow}
     */
    @NotNull Optional<IRow> get(int index);

    /**
     * Get the first row of the query.
     * @return {@link IRow}
     */
    @NotNull IRow getFirstRow();

}
