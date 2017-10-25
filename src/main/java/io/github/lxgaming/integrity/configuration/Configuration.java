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

package io.github.lxgaming.integrity.configuration;

import io.github.lxgaming.integrity.Integrity;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.util.function.Function;

public class Configuration {
    
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private ConfigurationOptions configurationOptions;
    private CommentedConfigurationNode configurationNode;
    
    private Config config;
    
    public void loadConfiguration() {
        try {
            configurationLoader = HoconConfigurationLoader.builder().setPath(Integrity.getInstance().getPath()).build();
            configurationOptions = ConfigurationOptions.defaults().setShouldCopyDefaults(true);
            configurationNode = getConfigurationLoader().load(getConfigurationOptions());
            config = new Config();
            
            getConfig().setDebug(getConfigurationNode().getNode("general", "debug").getBoolean(false));
            getConfig().setCrashServer(getConfigurationNode().getNode("general", "crashServer").getBoolean(false));
            getConfig().setCrashMessage(getConfigurationNode().getNode("general", "crashMessage").getString("\nLoaded: [LOADED]\nMissing: [MISSING]"));
            getConfig().setWhitelistServer(getConfigurationNode().getNode("general", "whitelistServer").getBoolean(true));
            getConfig().setWhitelistMessage(getConfigurationNode().getNode("general", "whitelistMessage").getString("&cServer is currently unavailable"));
            getConfig().setModList(getConfigurationNode().getNode("general", "modList").getList(getStringTransformer()));
            
            Integrity.getInstance().getLogger().info("Successfully loaded configuration file.");
        } catch (IOException | RuntimeException ex) {
            configurationNode = getConfigurationLoader().createEmptyNode(getConfigurationOptions());
            Integrity.getInstance().getLogger().error("Encountered an error processing {}::loadConfiguration", getClass().getSimpleName());
            ex.printStackTrace();
        }
    }
    
    public void saveConfiguration() {
        try {
            if (getConfig() == null) {
                throw new NullPointerException("Config is null!");
            }
            
            getConfigurationNode().getNode("general", "debug").setValue(getConfig().isDebug());
            getConfigurationNode().getNode("general", "crashServer").setValue(getConfig().isCrashServer()).setComment("Generates a crash reports then stops the server.");
            getConfigurationNode().getNode("general", "crashMessage").setValue(getConfig().getCrashMessage());
            getConfigurationNode().getNode("general", "whitelistServer").setValue(getConfig().isWhitelistServer()).setComment("Prevents players from joining the server.");
            getConfigurationNode().getNode("general", "whitelistMessage").setValue(getConfig().getWhitelistMessage());
            getConfigurationNode().getNode("general", "modList").setValue(getConfig().getModList()).setComment("List of mods required for the server to be operational.");
            
            getConfigurationLoader().save(getConfigurationNode());
            Integrity.getInstance().getLogger().info("Successfully saved configuration file.");
        } catch (IOException | RuntimeException ex) {
            Integrity.getInstance().getLogger().error("Encountered an error processing {}::saveConfiguration", getClass().getSimpleName());
            ex.printStackTrace();
        }
    }
    
    private Function<Object, String> getStringTransformer() {
        return (Object object) -> {
            if (object instanceof String) {
                return (String) object;
            }
            
            return null;
        };
    }
    
    public ConfigurationLoader<CommentedConfigurationNode> getConfigurationLoader() {
        return configurationLoader;
    }
    
    public ConfigurationOptions getConfigurationOptions() {
        return configurationOptions;
    }
    
    public CommentedConfigurationNode getConfigurationNode() {
        return configurationNode;
    }
    
    public Config getConfig() {
        return config;
    }
}