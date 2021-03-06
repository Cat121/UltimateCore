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
package bammerbom.ultimatecore.sponge.modules.home.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import org.spongepowered.api.text.Text;

public class HomePermissions {
    public static Permission UC_HOME_HOME_BASE = Permission.create("uc.home.home.base", "home", PermissionLevel.EVERYONE, "home", Text.of("Allows you to teleport to your own home."));
    public static Permission UC_HOME_SETHOME_BASE = Permission.create("uc.home.sethome.base", "home", PermissionLevel.EVERYONE, "sethome", Text.of("Allows you to set your own home."));
    public static Permission UC_HOME_SETHOME_UNLIMITED = Permission.create("uc.home.sethome.unlimited", "home", PermissionLevel.VIP, "sethome", Text.of("Allows you to set an unlimited amount of homes."));
    public static Permission UC_HOME_DELHOME_BASE = Permission.create("uc.home.delhome.base", "home", PermissionLevel.EVERYONE, "delhome", Text.of("Allows you to remove one of your homes."));

    public static PermissionOption UC_HOME_HOMECOUNT = PermissionOption.create("uc.home.homecount", "home", "home", "1", Text.of("The amount of homes the player can have."));
}