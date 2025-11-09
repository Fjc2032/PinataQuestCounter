package dev.Fjc.pinataQuestCounter;

import com.ordwen.odailyquests.ODailyQuests;
import dev.Fjc.pinataQuestCounter.counter.CounterClass;
import dev.Fjc.pinataQuestCounter.file.FileBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PinataQuestCounter extends JavaPlugin {

    private static PinataQuestCounter plugin;
    private static ODailyQuests instance;

    private final FileBuilder fileBuilder = new FileBuilder(this);

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("ODailyQuests")) {
            this.getLogger().warning("ODailyQuests could not be found! That's not good.");
            run();
        } else run();

    }

    public void run() {
        plugin = this;
        instance = ODailyQuests.INSTANCE;

        try {
            registerQuestEvent(new CounterClass(this));
        } catch (IllegalArgumentException | NullPointerException e) {
            plugin.getLogger().warning("Something horribly wrong has stopped this quest from registering!");
            plugin.getLogger().warning(e.getLocalizedMessage());
        } finally {
            plugin.getLogger().info("Finished with loading the event. Moving on...");
        }

        try {
            fileBuilder.build();
            fileBuilder.loadDefaults();
        } catch (IOException e) {
            plugin.getLogger().warning("Something went horribly wrong while attempting to construct new configs:");
            plugin.getLogger().warning(e.getLocalizedMessage());
        } finally {
            plugin.getLogger().info("Done.");
        }
    }

    @Override
    public void onDisable() {
        plugin = null;
        instance = null;
    }

    public static PinataQuestCounter getPlugin() {
        return plugin;
    }

    public static ODailyQuests getInstance() {
        return instance;
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }

    public void registerQuestEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
}
