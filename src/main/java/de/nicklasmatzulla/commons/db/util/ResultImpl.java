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

import de.nicklasmatzulla.commons.api.db.util.IResult;
import de.nicklasmatzulla.commons.api.db.util.IRow;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
public final class ResultImpl implements IResult {

    private final List<IRow> rows;

    public ResultImpl(final @NotNull List<IRow> rows) {
        this.rows = rows;
    }

    @Override
    public @NotNull List<IRow> getRows() {
        return this.rows;
    }

    @Override
    public @NotNull Optional<IRow> get(final int index) {
        if (index < this.rows.size()) {
            final IRow row = this.rows.get(index);
            return Optional.of(row);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull IRow getFirstRow() {
        return this.rows.getFirst();
    }
}
