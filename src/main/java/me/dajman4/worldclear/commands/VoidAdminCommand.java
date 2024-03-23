package me.dajman4.worldclear.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.MessageType;
import co.aikar.commands.annotation.*;
import co.aikar.locales.MessageKey;
import me.dajman4.worldclear.utils.ClearWorld;
import me.dajman4.worldclear.utils.Constants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("voidadmin")
public class VoidAdminCommand extends BaseCommand {

    // see https://github.com/aikar/commands/wiki/Locales
    static MessageKey key(String key) {
        return MessageKey.of(Constants.ACF_BASE_KEY + "." + key);
    }

    // see https://github.com/aikar/commands/wiki/Command-Help
    @HelpCommand
    @Subcommand("help")
    public void showHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("clear|c")
    @Description("{@@commands.descriptions.info}")
    @CommandPermission(Constants.INFO_CMD_PERMISSION)
    public void info(@Flags("self") Player player) {
        ClearWorld.clearWorld(player.getWorld());
    }

    private void success(String key, String... replacements) {
        getCurrentCommandIssuer().sendMessage(MessageType.INFO, key(key), replacements);
    }

    private void error(String key, String... replacements) {
        getCurrentCommandIssuer().sendMessage(MessageType.ERROR, key(key), replacements);
    }
}