package com.github.merelysnow.astralwarden;

import com.github.merelysnow.astralwarden.cache.WardenPlayerCache;
import com.github.merelysnow.astralwarden.check.manager.CheckManager;
import com.github.merelysnow.astralwarden.commands.AstralWardenCommand;
import com.github.merelysnow.astralwarden.commands.subcommand.NotifyCommand;
import com.github.merelysnow.astralwarden.commands.subcommand.ProfileCommand;
import com.github.merelysnow.astralwarden.listeners.PlayerConnectionListener;
import com.github.merelysnow.astralwarden.logger.AstralWardenLogger;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.plugin.java.JavaPlugin;

public class AstralWardenPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final WardenPlayerCache wardenPlayerCache = new WardenPlayerCache();
        final AstralWardenLogger astralWardenLogger = new AstralWardenLogger(wardenPlayerCache);
        final CheckManager checkManager = new CheckManager(wardenPlayerCache, astralWardenLogger, this);

        checkManager.init();
        registerCommands(wardenPlayerCache);

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(wardenPlayerCache), this);
    }

    private void registerCommands(WardenPlayerCache wardenPlayerCache) {
        BukkitFrame bukkitFrame = new BukkitFrame(this);

        bukkitFrame.registerCommands(
                new AstralWardenCommand(),
                new NotifyCommand(wardenPlayerCache),
                new ProfileCommand(wardenPlayerCache)
        );


        MessageHolder messageHolder = bukkitFrame.getMessageHolder();

        messageHolder.setMessage(MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar este comando.");
        messageHolder.setMessage(MessageType.ERROR, "§cUm erro ocorreu! {error}");
        messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cUtilize /{usage}");
        messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cVocê não pode utilizar este comando pois ele é direcioado apenas para {target}.");
    }

    public static AstralWardenPlugin getInstance() {
        return getPlugin(AstralWardenPlugin.class);
    }
}
