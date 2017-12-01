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

package io.github.lxgaming.integrity.listeners;

import io.github.lxgaming.integrity.Integrity;
import io.github.lxgaming.integrity.util.SpongeHelper;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class IntegrityListener {
    
    @Listener(order = Order.LAST)
    public void onClientConnection(ClientConnectionEvent.Join event) {
        if (!Integrity.getInstance().getConfig().isCrashServer() && !Integrity.getInstance().getConfig().isWhitelistServer()) {
            return;
        }
        
        if (event.getTargetEntity().hasPermission("integrity.notification")) {
            event.getTargetEntity().sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "An error occurred. Please check the console."));
        }
    }
}