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
package bammerbom.ultimatecore.sponge.modules.afk.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.DataFailedException;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.AfkModule;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkDetectionListener;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = AfkModule.class, aliases = {"afk", "idle", "away"})
public class AfkCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return AfkPermissions.UC_AFK_AFK_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(AfkPermissions.UC_AFK_AFK_BASE, AfkPermissions.UC_AFK_AFK_BASE_MESSAGE, AfkPermissions.UC_AFK_AFK_OTHERS, AfkPermissions.UC_AFK_AFK_OTHERS_MESSAGE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.optionalWeak(GenericArguments.onlyOne(new PlayerArgument(Text.of("player")))),
                GenericArguments.optionalWeak(GenericArguments.remainingJoinedStrings(Text.of("message")))
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, AfkPermissions.UC_AFK_AFK_BASE);
        UltimateUser user;
        if (args.hasAny("player")) {
            user = UltimateCore.get().getUserService().getUser(args.<Player>getOne("player").get());
        } else {
            checkIfPlayer(sender);
            user = UltimateCore.get().getUserService().getUser((Player) sender);
        }

        //No isPresent() needed because IS_AFK has a default value
        boolean newafk = !user.get(AfkKeys.IS_AFK).get();
        if (user.offer(AfkKeys.IS_AFK, newafk)) {
            if (newafk) {
                user.offer(AfkKeys.AFK_TIME, System.currentTimeMillis());
                if (args.hasAny("message")) {
                    String message = args.<String>getOne("message").get();
                    user.offer(AfkKeys.AFK_MESSAGE, message);
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk.message", "%player%", user.getUser().getName(), "%message%", message));
                } else {
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk", "%player%", user.getUser().getName()));
                }
                //Make sure the player is not un-afked instantly
                AfkDetectionListener.afktime.put(user.getIdentifier(), 0L);
                user.offer(AfkKeys.LAST_LOCATION, new Transform<>(user.getPlayer().get().getLocation(), user.getPlayer().get().getRotation(), user.getPlayer().get().getScale()));
            } else {
                Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.nolonger", "%player%", user.getUser().getName(), "%time%", TimeUtil.formatDateDiff(user.get(AfkKeys.AFK_TIME).get(), 2, null)));
                user.offer(AfkKeys.AFK_TIME, null);
                user.offer(AfkKeys.AFK_MESSAGE, null);
            }
        } else {
            throw new DataFailedException(Messages.getFormatted(sender, "afk.command.afk.datafailed"));
        }
        return CommandResult.success();
    }
}
