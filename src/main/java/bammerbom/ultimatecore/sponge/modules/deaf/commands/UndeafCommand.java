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
package bammerbom.ultimatecore.sponge.modules.deaf.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.deaf.DeafModule;
import bammerbom.ultimatecore.sponge.modules.deaf.api.DeafKeys;
import bammerbom.ultimatecore.sponge.modules.deaf.api.DeafPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = DeafModule.class, aliases = {"undeaf"})
public class UndeafCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return DeafPermissions.UC_DEAF_UNDEAF_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(DeafPermissions.UC_DEAF_UNDEAF_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, DeafPermissions.UC_DEAF_UNDEAF_BASE);
        Player t = args.<Player>getOne("player").get();

        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
        if (!ut.get(DeafKeys.DEAF).isPresent()) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "deaf.command.undeaf.notdeaf", "%player%", VariableUtil.getNameEntity(t)));
        }
        ut.offer(DeafKeys.DEAF, null);

        Messages.send(sender, "deaf.command.undeaf.success", "%player%", VariableUtil.getNameEntity(t));
        return CommandResult.success();
    }
}
