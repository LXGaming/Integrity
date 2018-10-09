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

import io.github.lxgaming.integrity.util.Toolbox;

import java.util.List;

public class Config {
    
    private boolean debug = false;
    private boolean crashServer = false;
    private String crashMessage = "\nLoaded: [LOADED]\nMissing: [MISSING]";
    private boolean whitelistServer = false;
    private String whitelistMessage = "&cServer is currently unavailable";
    private List<String> modList = Toolbox.newArrayList(
            "griefprevention",
            "!ftbutilities",
            "luckperms",
            "miscserverutils",
            "nucleus",
            "sledgehammer"
    );
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public boolean isCrashServer() {
        return crashServer;
    }
    
    public String getCrashMessage() {
        return crashMessage;
    }
    
    public boolean isWhitelistServer() {
        return whitelistServer;
    }
    
    public String getWhitelistMessage() {
        return whitelistMessage;
    }
    
    public List<String> getModList() {
        return modList;
    }
}