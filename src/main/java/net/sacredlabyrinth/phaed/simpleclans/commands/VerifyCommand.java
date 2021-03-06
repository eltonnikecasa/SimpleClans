package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class VerifyCommand {

    public VerifyCommand() {
    }

    /**
     * Execute the command
     *
     * @param sender
     * @param arg
     */
    public void execute(CommandSender sender, String[] arg) {
        SimpleClans plugin = SimpleClans.getInstance();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
            Clan clan = cp == null ? null : cp.getClan();

            boolean isNonVerified = clan != null && !clan.isVerified();
            boolean isBuyer = isNonVerified && plugin.getSettingsManager().isRequireVerification() && plugin.getSettingsManager().isePurchaseVerification();

            if (!plugin.getPermissionsManager().has(player, "simpleclans.mod.verify")) {
                if (arg.length != 0) {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                    return;
                }
                if (isBuyer) {
                    if (arg.length == 0 && plugin.getClanManager().purchaseVerification(player)) {
                        clan.verifyClan();
                        clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.0.has.been.verified"), clan.getName()));
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("the.clan.has.been.verified"));
                    }
                }
            } else {
                if (arg.length != 1) {
                    ChatBlock.sendMessage(sender, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.verify.tag"), plugin.getSettingsManager().getCommandClan()));
                    return;
                }
                verify(player, arg[0]);
            }
        } else {
            if (arg.length != 1) {
                ChatBlock.sendMessage(sender, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.verify.tag"), plugin.getSettingsManager().getCommandClan()));
                return;
            }
            verify(sender, arg[0]);
        }
    }

    private void verify(CommandSender player, String clanName) {
        SimpleClans plugin = SimpleClans.getInstance();
        Clan clan = plugin.getClanManager().getClan(clanName);

        if (clan != null) {
            if (!clan.isVerified()) {
                clan.verifyClan();
                clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.0.has.been.verified"), clan.getName()));
                ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("the.clan.has.been.verified"));
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.clan.is.already.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.clan.does.not.exist"));
        }
    }
}
