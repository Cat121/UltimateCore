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
package bammerbom.ultimatecore.sponge.modules.warp.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.warp.WarpModule;
import bammerbom.ultimatecore.sponge.modules.warp.api.Warp;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpKeys;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpPermissions;
import bammerbom.ultimatecore.sponge.modules.warp.commands.arguments.WarpArgument;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CommandInfo(module = WarpModule.class, aliases = {"warp"})
public class WarpCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return WarpPermissions.UC_WARP_WARP_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WarpPermissions.UC_WARP_WARP_BASE, WarpPermissions.UC_WARP_WARP_WARP);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new WarpArgument(Text.of("warp"))).onlyOne().optional().usage("<Warp>").build()};
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, WarpPermissions.UC_WARP_WARP_BASE);
        if (!args.hasAny("warp")) {
            checkPermission(sender, WarpPermissions.UC_WARP_WARPLIST_BASE);
            //Get all warps
            List<Warp> warps = GlobalData.get(WarpKeys.WARPS).get();
            List<Text> texts = new ArrayList<>();
            //Add entry to texts for every warp
            for (Warp warp : warps) {
                if (!sender.hasPermission("uc.warp.warp." + warp.getName().toLowerCase())) {
                    continue;
                }
                texts.add(Messages.getFormatted("warp.command.warplist.entry", "%warp%", warp.getName(), "%description%", warp.getDescription()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("warp.command.warplist.hoverentry", "%warp%", warp.getName()))).onClick(TextActions.runCommand("/warp " + warp.getName())).build());
            }
            //If empty send message
            if (texts.isEmpty()) {
                throw new ErrorMessageException(Messages.getFormatted(sender, "warp.command.warplist.empty"));
            }
            //Sort alphabetically
            Collections.sort(texts);

            PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
            PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("warp.command.warplist.header").toBuilder().format(Messages.getFormatted("warp.command.warplist.char").getFormat()).build()).padding(Messages.getFormatted("warp.command.warplist.char")).build();
            paginationList.sendTo(sender);
            return CommandResult.empty();
        }
        //Teleport the player to a warp
        checkIfPlayer(sender);
        Player p = (Player) sender;
        //Try to find warp
        Warp warp = args.<Warp>getOne("warp").get();
        //Check permissions
        checkPermission(sender, "uc.warp.warp." + warp.getName().toLowerCase());
        //Teleport to the warp
        Transform<World> loc = warp.getLocation().toOptional().orElseThrow(() -> Messages.error(sender, "warp.command.warp.notavailable"));
        Teleportation request = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), loc, req -> {
            Messages.send(sender, "warp.command.warp.success", "%warp%", warp.getName());
        }, (red, reason) -> {
        }, true, false);
        request.start();
        return CommandResult.success();
    }
}
