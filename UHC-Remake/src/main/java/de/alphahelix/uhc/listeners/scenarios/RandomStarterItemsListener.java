package de.alphahelix.uhc.listeners.scenarios;

import de.alphahelix.alphaapi.listener.SimpleListener;
import de.alphahelix.alphaapi.utils.Util;
import de.alphahelix.uhc.enums.Scenarios;
import de.alphahelix.uhc.events.timers.LobbyEndEvent;
import de.alphahelix.uhc.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class RandomStarterItemsListener extends SimpleListener {

    @EventHandler
    public void onEnd(LobbyEndEvent e) {
        if (!Scenarios.isPlayedAndEnabled(Scenarios.RANDOM_STARTER_ITEMS)) return;

        for (Player p : Util.makePlayerArray(PlayerUtil.getSurvivors())) {
            for (Material m : getItems()) {
                p.getInventory().addItem(new ItemStack(m, new Random().nextInt(64)));
            }
        }
    }

    private ArrayList<Material> getItems() {
        ArrayList<Material> items = new ArrayList<>();
        int amount = 10;

        for (Material m : Material.values()) {
            if (amount <= 0) break;
            if (Math.random() < 0.07) {
                items.add(m);
                amount--;
            }
        }
        return items;
    }
}
