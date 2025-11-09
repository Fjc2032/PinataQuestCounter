package dev.Fjc.pinataQuestCounter.file;

import dev.Fjc.pinataQuestCounter.PinataQuestCounter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileBuilder {

    private final PinataQuestCounter plugin;

    private final File data;
    private final File head;

    private final YamlConfiguration dataConfig;
    private final FileConfiguration configuration;

    private final List<File> files;

    public FileBuilder(PinataQuestCounter plugin) {
        this.plugin = plugin;

        this.data = new File(plugin.getDataFolder(), "data.yml");
        this.head = new File(plugin.getDataFolder(), "config.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(data);
        this.configuration = plugin.getConfig();

        files = List.of(
                data,
                head
        );
    }

    public void loadDefaults() {
        configuration.addDefault("isEnabled", true);
        configuration.addDefault("threshold", 15);

        dataConfig.addDefault("questsDoneTemp", 0);
        dataConfig.addDefault("totalQuestsDonePersist", 0);

        configuration.options().copyDefaults(true);
    }

    public void build() throws IOException {
        if (!head.exists()) {
            head.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        if (!data.exists()) {
            data.getParentFile().mkdirs();
            data.createNewFile();

            dataConfig.save(data);
        }
    }

    private void save(YamlConfiguration yaml, File file) {
        try {
            yaml.save(file);
        } catch (IOException | IllegalArgumentException exception) {
            this.plugin.getLogger().warning("Something went wrong while attempting to save the file!");
            exception.printStackTrace();
        }
    }

    public void add(int amount) {
        String path = "amount";
        int current = dataConfig.getInt(path, 0);

        dataConfig.set(path, current + amount);
        save(dataConfig, data);
    }

    public int getQuestsCompleted() {
        return dataConfig.getInt("amount");
    }

    public int getThreshold() {
        return dataConfig.getInt("threshold", 15);
    }

    public void setToZero() {
        String path = "amount";
        dataConfig.set(path, 0);
        save(dataConfig, data);
    }
}
