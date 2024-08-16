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

import de.nicklasmatzulla.forestattack.config.util.BaseConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

@Getter
public class SettingsConfig extends BaseConfig {
    private boolean spawnEnabled;
    private boolean shopEnabled;
    private final List<ItemStack> starterKitItemStacks;

    public SettingsConfig(@NotNull Logger logger) {
        super(logger, new File("plugins/ForestAttack/settings.yml"), "config/settings.yml", true);
        this.spawnEnabled = this.config.getBoolean("commands.spawn.enabled", true);
        this.shopEnabled = this.config.getBoolean("commands.shop.enabled", true);
        this.starterKitItemStacks = initStarterKitItemStacks();
    }

    public void setSpawnEnabled(final boolean enabled) {
        this.config.set("commands.spawn.enabled", enabled);
        save();
        this.spawnEnabled = enabled;
    }

    public void setShopEnabled(final boolean enabled) {
        this.config.set("commands.shop.enabled", enabled);
        save();
        this.shopEnabled = enabled;
    }

    private @NotNull List<ItemStack> initStarterKitItemStacks() {
        return this.config.getStringList("commands.kit.items").stream()
                .map(element -> {
                    final String[] elements = element.split(":");
                    final String itemName = elements[0];
                    final int amount = Integer.parseInt(elements[1]);
                    final Material material = Material.valueOf(itemName);
                    return new ItemStack(material, amount);
                })
                .toList();
    }

}
