package dev.Fjc.pinataQuestCounter;

import com.ordwen.odailyquests.ODailyQuests;
import dev.Fjc.pinataQuestCounter.counter.CounterClass;
import dev.Fjc.pinataQuestCounter.file.FileBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PinataQuestCounter extends JavaPlugin {

    private static PinataQuestCounter plugin;
    private FileBuilder fileBuilder;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        fileBuilder = new FileBuilder(this);
        try {
            fileBuilder.build();
            fileBuilder.loadDefaults();
        } catch (IOException e) {
            getLogger().severe("Failed to build data/config files!");
            e.printStackTrace();
        }

        if (getServer().getPluginManager().isPluginEnabled("ODailyQuests")) {
            ODailyQuests odq = (ODailyQuests) getServer().getPluginManager().getPlugin("ODailyQuests");
            getLogger().info("[DEBUG] ODailyQuests detected, registering listener...");
            registerQuestEvent(new CounterClass(this));
        } else {
            getLogger().warning("[DEBUG] ODailyQuests not found, quest tracking disabled.");
        }

        getLogger().info("PinataQuestCounter enabled successfully!");
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static PinataQuestCounter getPlugin() {
        return plugin;
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }

    public void registerQuestEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}
