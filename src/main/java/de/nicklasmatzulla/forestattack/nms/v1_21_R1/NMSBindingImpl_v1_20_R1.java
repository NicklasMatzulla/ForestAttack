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

package de.nicklasmatzulla.forestattack.nms.v1_21_R1;

import de.nicklasmatzulla.forestattack.nms.INMSBinding;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class NMSBindingImpl_v1_20_R1 implements INMSBinding {

    @Override
    public Optional<ItemStack[]> loadOfflinePlayerEnderChest(final @NotNull String worldName, final @NotNull UUID uniqueId) {
        final File playerDataFile = new File(worldName + "/playerdata/" + uniqueId + ".dat");
        if (!playerDataFile.exists()) {
            return Optional.empty();
        }
        try (final FileInputStream inputStream = new FileInputStream(playerDataFile)) {
            final CompoundTag playerData = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
            final ListTag enderItems = playerData.getList("EnderItems", 10); // 10 is the NBT type for CompoundTag
            final ItemStack[] items = new ItemStack[27];
            for (int i = 0; i < enderItems.size(); i++) {
                final CompoundTag itemTag = enderItems.getCompound(i);
                final int slot = itemTag.getByte("Slot") & 255;
                if (slot < items.length) {
                    final ItemStack itemStack = parseItemStack(itemTag);
                    items[slot] = itemStack != null ? itemStack : new ItemStack(Material.AIR);
                }
            }
            return Optional.of(items);
        } catch (final @NotNull IOException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Convert an ItemStack NBT tag to a Bukkit ItemStack.
     * @param itemTag The ItemStack NBT tag to be converted.
     * @return The converted Bukkit ItemStack, null if the conversation failed.
     */
    private org.bukkit.inventory.ItemStack parseItemStack(final @NotNull CompoundTag itemTag) {
        try {
            final HolderLookup.Provider registries = ((CraftServer) Bukkit.getServer()).getServer().registryAccess();
            final net.minecraft.world.item.ItemStack nmsItemStack = net.minecraft.world.item.ItemStack.parseOptional(registries, itemTag);
            return CraftItemStack.asBukkitCopy(nmsItemStack);
        } catch (final @NotNull Exception ignored) {
            return null;
        }
    }

}
