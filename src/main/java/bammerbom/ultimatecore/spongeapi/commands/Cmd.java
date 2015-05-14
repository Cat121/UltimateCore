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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommandExecutor;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Arrays;
import java.util.List;

public class Cmd implements UltimateCommandExecutor {

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getPermission() {
        return "uc.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public void run(final CommandSource cs, String label, final String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String[] args, String label, String curs, Integer curn) {
        return null;
    }

}