/*
 * MIT License
 *
 * Copyright (c) 2024 Nicklas Matzulla
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

package de.nicklasmatzulla.forestattack.config;

import com.zaxxer.hikari.HikariDataSource;
import de.nicklasmatzulla.commons.db.MariadbConnectionFactory;
import de.nicklasmatzulla.forestattack.config.util.BaseConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

public class ConfidentialConfig extends BaseConfig {

    public ConfidentialConfig(final @NotNull Logger logger) {
        super(logger, new File("plugins/ForestAttack/confidential.yml"), "config/confidential.yml", true);
    }

    public @NotNull HikariDataSource createHikariDataSource() {
        final String host = this.config.getString("mariadb.host", "localhost");
        final int port = this.config.getInt("mariadb.port", 3306);
        final String user = this.config.getString("mariadb.user", "forestattack");
        final String pass = this.config.getString("mariadb.pass", "forestattack");
        final String db = this.config.getString("mariadb.db", "forestattack");
        final boolean useSSL = this.config.getBoolean("mariadb.useSSL", false);
        final int maxPoolSize = this.config.getInt("mariadb.maxPoolSize", 8);
        return new MariadbConnectionFactory()
                .host(host)
                .port(port)
                .user(user)
                .pass(pass)
                .db(db)
                .useSsl(useSSL)
                .maxPoolSize(maxPoolSize)
                .build();
    }

}
