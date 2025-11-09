package dev.Fjc.pinataQuestCounter.counter;

import com.ordwen.odailyquests.api.events.QuestCompletedEvent;
import dev.Fjc.pinataQuestCounter.PinataQuestCounter;
import dev.Fjc.pinataQuestCounter.file.FileBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CounterClass implements Listener {

    private final PinataQuestCounter plugin;

    private final FileBuilder fileBuilder;

    int amount;

    final int THRESHOLD;

    public CounterClass(PinataQuestCounter plugin) {
        this.plugin = plugin;
        this.fileBuilder = plugin.getFileBuilder();

        this.amount = fileBuilder.getQuestsCompleted();
        THRESHOLD = fileBuilder.getThreshold();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuestComplete(QuestCompletedEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) plugin.getLogger().warning("This event has been canceled.");
        player.sendMessage("[DEBUG] Quest completed.");
        fileBuilder.add(1);

        if (amount >= THRESHOLD) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pinata spawn spawn");

            //Then we set the value back to 0 to start it over again.
            fileBuilder.setToZero();
        }
    }

}
