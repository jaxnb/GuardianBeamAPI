/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Jaxon A Brown
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.jaxonbrown.guardianBeam;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * GuardianBeamAPI containing plugin.
 * @author Jaxon A Brown
 */
public class GuardianBeamAPI extends JavaPlugin {
    @Getter
    private static GuardianBeamAPI instance;

    private String protocolLibVersion;

    public void onEnable() {
        GuardianBeamAPI.instance = this;

        try {
            com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
            this.protocolLibVersion = Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
        } catch(Exception ex) {
            this.getLogger().severe(ChatColor.RED + "GuardianBeamAPI could not start because " +
                    ChatColor.YELLOW + "ProtocolLib" + ChatColor.RED + " was not installed.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("GuardianBeamAPI")) {
            if(sender instanceof Player && sender.isOp() || sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + "GuardianBeamAPI v" + this.getDescription().getVersion() + " by " +
                        this.getDescription().getAuthors().get(0) + " is enabled and linked with Protocol Library v" +
                        this.protocolLibVersion + ".");
            }
            return true;
        }
        return false;
    }
}
