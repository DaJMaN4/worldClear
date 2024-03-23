package me.dajman4.worldclear;

import co.aikar.commands.PaperCommandManager;
import de.leonhard.storage.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.dajman4.worldclear.commands.VoidAdminCommand;
import me.dajman4.worldclear.commands.VoidCommand;
import me.dajman4.worldclear.inventory.Inventories;
import me.dajman4.worldclear.inventory.InventoryManager;
import me.dajman4.worldclear.listeners.OnEntityDeathEvent;
import me.dajman4.worldclear.listeners.OnInventoryClickEvent;
import me.dajman4.worldclear.listeners.OnItemDespawnEvent;
import me.dajman4.worldclear.utils.ConfigFile;
import net.milkbowl.vault.economy.Economy;
import me.dajman4.worldclear.commands.TemplateCommands;
import me.dajman4.worldclear.integrations.vault.VaultProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    @Getter
    @Accessors(fluent = true)
    private static Main instance;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private VaultProvider vault;
    private PaperCommandManager commandManager;
    @Getter InventoryManager inventoryManager;


    public Main() {
        instance = this;
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigFile configFile = new ConfigFile();
        configFile.load();

        setupVaultIntegration();
        setupCommands();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new OnItemDespawnEvent(), this);

        Inventories inventories = new Inventories();
        inventories.init();

        inventoryManager = new InventoryManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getLogger().info("Player joined.");
    }

    @EventHandler
    public void onServiceRegistration(ServiceRegisterEvent event) {
        if (event.getProvider().getService() == Economy.class) {
            setVault(new VaultProvider((Economy) event.getProvider().getProvider()));
            getLogger().info("Vault integration enabled.");
        }
    }

    private void setupVaultIntegration() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final RegisteredServiceProvider<Economy> serviceProvider = getServer().getServicesManager()
                .getRegistration(Economy.class);
            if (serviceProvider != null) {
                vault = new VaultProvider(Objects.requireNonNull(serviceProvider).getProvider());
                getLogger().info("Vault integration enabled.");
            }
        }
        if (vault == null) {
            vault = new VaultProvider();
            getLogger().warning("Vault integration is not yet available.");
        }
    }

    private void setupCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        loadCommandLocales(commandManager);

        commandManager.registerCommand(new TemplateCommands());
        commandManager.registerCommand(new VoidCommand());
        commandManager.registerCommand(new VoidAdminCommand());
    }

    // see https://github.com/aikar/commands/wiki/Locales
    private void loadCommandLocales(PaperCommandManager commandManager) {
        try {
            saveResource("lang_en.yaml", true);
            commandManager.getLocales().setDefaultLocale(Locale.ENGLISH);
            commandManager.getLocales().loadYamlLanguageFile("lang_en.yaml", Locale.ENGLISH);
            // this will detect the client locale and use it where possible
            commandManager.usePerIssuerLocale(true);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Failed to load language config 'lang_en.yaml': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
