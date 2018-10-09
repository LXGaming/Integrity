/*
 * Copyright 2017 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.integrity.util;

import io.github.lxgaming.integrity.Integrity;
import net.minecraft.crash.CrashReport;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Toolbox {
    
    public static Text getTextPrefix() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.onHover(TextActions.showText(getPluginInformation()));
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, "[", Reference.PLUGIN_NAME, "]"));
        return Text.of(textBuilder.build(), TextStyles.RESET, " ");
    }
    
    public static Text getPluginInformation() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, Reference.PLUGIN_NAME, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Version: ", TextColors.WHITE, Reference.PLUGIN_VERSION, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Authors: ", TextColors.WHITE, Reference.AUTHORS, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Website: ", TextColors.BLUE, getURLTextAction(Reference.WEBSITE), Reference.WEBSITE));
        return textBuilder.build();
    }
    
    public static TextAction<?> getURLTextAction(String url) {
        try {
            return TextActions.openUrl(new URL(url));
        } catch (MalformedURLException ex) {
            return TextActions.suggestCommand(url);
        }
    }
    
    public static Text convertColor(String string) {
        return TextSerializers.formattingCode('&').deserialize(string);
    }
    
    public static boolean crashServer(Text reason) {
        Integrity.getInstance().getLogger().info("Crashing server, Reason: {}", Text.of(reason).toPlain());
        if (!Sponge.isServerAvailable() || !(Sponge.getServer() instanceof MinecraftServer)) {
            Integrity.getInstance().getLogger().error("Cannot crash, Server is not available");
            return false;
        }
        
        MinecraftServer minecraftServer = (MinecraftServer) Sponge.getServer();
        CrashReport crashReport = CrashReport.makeCrashReport(new IntegrityException(), Text.of(getTextPrefix(), reason).toPlain());
        File crashDirectory = new File(minecraftServer.getDataDirectory(), "crash-reports");
        File crashFile = new File(crashDirectory, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
        if (crashReport.saveToFile(crashFile)) {
            Integrity.getInstance().getLogger().info("This crash report has been saved to: {}", crashFile.getAbsolutePath());
        } else {
            Integrity.getInstance().getLogger().error("We were unable to save this crash report to disk.");
        }
        
        minecraftServer.initiateShutdown();
        return true;
    }
    
    public static boolean whitelistServer(Text reason) {
        Integrity.getInstance().getLogger().info("Whitelisting server, Reason: {}", Text.of(reason).toPlain());
        if (!Sponge.isServerAvailable()) {
            Integrity.getInstance().getLogger().error("Cannot whitelist, Server is not available");
            return false;
        }
        
        Sponge.getServer().setHasWhitelist(true);
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            player.kick(Text.of(getTextPrefix(), reason));
            Integrity.getInstance().debugMessage("Kicked {}...", player.getName());
        }
        
        return true;
    }
    
    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        return Stream.of(elements).collect(Collectors.toCollection(ArrayList::new));
    }
}