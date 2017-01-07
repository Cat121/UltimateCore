/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.sponge.modules.food.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.food.api.FoodPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
import org.spongepowered.api.data.property.item.SaturationProperty;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FoodCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.FOOD.get();
    }

    @Override
    public String getIdentifier() {
        return "food";
    }

    @Override
    public Permission getPermission() {
        return FoodPermissions.UC_FOOD_FOOD_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(FoodPermissions.UC_FOOD_FOOD_BASE, FoodPermissions.UC_FOOD_FOOD_OTHERS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("food", "feed", "eat");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(FoodPermissions.UC_FOOD_FOOD_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            //Self
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            p.offer(Keys.FOOD_LEVEL, p.get(FoodData.class).get().foodLevel().getMaxValue());
            p.offer(Keys.SATURATION, ItemStack.builder().itemType(ItemTypes.COOKED_BEEF).build().getProperty(SaturationProperty.class).get().getValue());
            p.sendMessage(Messages.getFormatted("food.command.food.success.self"));
            return CommandResult.success();
        } else {
            //Others
            if (!sender.hasPermission(FoodPermissions.UC_FOOD_FOOD_BASE.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            Player t = Selector.one(sender, args[0]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }
            t.offer(Keys.FOOD_LEVEL, t.get(FoodData.class).get().foodLevel().getMaxValue());
            t.offer(Keys.SATURATION, ItemStack.builder().itemType(ItemTypes.COOKED_BEEF).build().getProperty(SaturationProperty.class).get().getValue());
            sender.sendMessage(Messages.getFormatted("food.command.food.success.others.self", "%player%", VariableUtil.getNameSource(t)));
            t.sendMessage(Messages.getFormatted("food.command.food.success.others.others", "%player%", VariableUtil.getNameSource(sender)));
            return CommandResult.success();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}