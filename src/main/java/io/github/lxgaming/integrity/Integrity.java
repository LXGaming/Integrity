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

package io.github.lxgaming.integrity;

import com.google.inject.Inject;
import io.github.lxgaming.integrity.configuration.Config;
import io.github.lxgaming.integrity.configuration.Configuration;
import io.github.lxgaming.integrity.listeners.IntegrityListener;
import io.github.lxgaming.integrity.util.Reference;
import io.github.lxgaming.integrity.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Plugin(
        id = Reference.PLUGIN_ID,
        name = Reference.PLUGIN_NAME,
        version = Reference.PLUGIN_VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class Integrity {
    
    private static Integrity instance;
    
    @Inject
    private Logger logger;
    
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path path;
    
    private Configuration configuration;
    
    public void onGameConstruction(GameConstructionEvent event) {
        instance = this;
        configuration = new Configuration(getPath());
    }
    
    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        getConfiguration().loadConfiguration();
    }
    
    @Listener
    public void onGamePostInitialization(GamePostInitializationEvent event) {
        getConfiguration().saveConfiguration();
    }
    
    @Listener(order = Order.LAST)
    public void onGameStartedServer(GameStartedServerEvent event) {
        getLogger().info("{} v{} has started.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
        List<String> loadedMods = Toolbox.newArrayList();
        List<String> missingMods = Toolbox.newArrayList();
        
        List<String> modList = getConfig().map(Config::getModList).orElse(null);
        if (modList == null || modList.isEmpty()) {
            return;
        }
        
        for (String modId : modList) {
            if (StringUtils.startsWith(modId, "!") && Sponge.getPluginManager().isLoaded(StringUtils.substringAfter(modId, "!"))) {
                loadedMods.add(StringUtils.substringAfter(modId, "!"));
            }
            
            if (!StringUtils.startsWith(modId, "!") && !Sponge.getPluginManager().isLoaded(modId)) {
                missingMods.add(modId);
            }
        }
        
        if (loadedMods.isEmpty() && missingMods.isEmpty()) {
            return;
        }
        
        getLogger().warn("-------------------- WARNING --------------------");
        
        if (!loadedMods.isEmpty()) {
            getLogger().warn("Loaded:");
            loadedMods.forEach(action -> getLogger().warn("- " + action));
        }
        
        if (!missingMods.isEmpty()) {
            getLogger().warn("Missing:");
            missingMods.forEach(action -> getLogger().warn("- " + action));
        }
        
        getLogger().warn("-------------------- WARNING --------------------");
        
        Sponge.getEventManager().registerListeners(getInstance(), new IntegrityListener());
        
        if (getConfig().map(Config::isWhitelistServer).orElse(false)) {
            Toolbox.whitelistServer(Toolbox.convertColor(getConfig().map(Config::getWhitelistMessage).orElse("Error")));
        }
        
        if (getConfig().map(Config::isCrashServer).orElse(false)) {
            String reason = getConfig().map(Config::getCrashMessage).orElse("Error")
                    .replace("[LOADED]", String.join(", ", loadedMods))
                    .replace("[MISSING]", String.join(", ", missingMods));
            
            Toolbox.crashServer(Text.of(reason));
        }
    }
    
    @Listener
    public void onGameStopping(GameStoppingEvent event) {
        getLogger().info("{} v{} has stopped.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
    }
    
    public void debugMessage(String string, Object... objects) {
        if (getConfig().map(Config::isDebug).orElse(false)) {
            getLogger().info(string, objects);
        }
    }
    
    public static Integrity getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Path getPath() {
        return path;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Optional<Config> getConfig() {
        if (getConfiguration() != null) {
            return Optional.ofNullable(getConfiguration().getConfig());
        }
        
        return Optional.empty();
    }
}
