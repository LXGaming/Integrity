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

import java.util.List;

public class Config {
    
    private boolean debug;
    private boolean crashServer;
    private String crashMessage;
    private boolean whitelistServer;
    private String whitelistMessage;
    private List<String> modList;
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public boolean isCrashServer() {
        return crashServer;
    }
    
    public void setCrashServer(boolean crashServer) {
        this.crashServer = crashServer;
    }
    
    public String getCrashMessage() {
        return crashMessage;
    }
    
    public void setCrashMessage(String crashMessage) {
        this.crashMessage = crashMessage;
    }
    
    public boolean isWhitelistServer() {
        return whitelistServer;
    }
    
    public void setWhitelistServer(boolean whitelistServer) {
        this.whitelistServer = whitelistServer;
    }
    
    public String getWhitelistMessage() {
        return whitelistMessage;
    }
    
    public void setWhitelistMessage(String whitelistMessage) {
        this.whitelistMessage = whitelistMessage;
    }
    
    public List<String> getModList() {
        return modList;
    }
    
    public void setModList(List<String> modList) {
        this.modList = modList;
    }
}