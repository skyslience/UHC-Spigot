package de.alphahelix.uhc.listeners.scenarios;

import de.alphahelix.alphaapi.item.ItemBuilder;
import de.alphahelix.alphaapi.listener.SimpleListener;
import de.alphahelix.alphaapi.utils.Util;
import de.alphahelix.uhc.enums.Scenarios;
import de.alphahelix.uhc.events.timers.LobbyEndEvent;
import de.alphahelix.uhc.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemHuntListener extends SimpleListener {

    private HashMap<String, ArrayList<String>> lists = new HashMap<>();

    @EventHandler
    public void onEnd(LobbyEndEvent e) {
        if (!Scenarios.isPlayedAndEnabled(Scenarios.ITEM_HUNT))
            return;

        for (Player p : Util.makePlayerArray(PlayerUtil.getSurvivors())) {
            ArrayList<String> list = getItems();
            String[] lore = list.toArray(new String[list.size()]);
            p.getInventory().addItem(new ItemBuilder(Material.PAPER).setLore(lore).build());
            lists.put(p.getName(), list);
        }
    }

    @EventHandler
    public void onOpen(PlayerPickupItemEvent e) {
        if (e.isCancelled()) return;
        if (!Scenarios.isPlayedAndEnabled(Scenarios.ITEM_HUNT)) return;

        if (lists.containsKey(e.getPlayer().getName())) {
            for (String mNames : lists.get(e.getPlayer().getName())) {
                if (e.getItem().getItemStack().getType().equals(Material.getMaterial(mNames.toUpperCase()))) {
                    lists.get(e.getPlayer().getName()).remove(Material.getMaterial(mNames.toUpperCase()));
                    if (e.getPlayer().getHealth() > 19.0) continue;
                    e.getPlayer().setHealth(e.getPlayer().getHealth() + 1.0);
                }
            }
        }
    }

    private ArrayList<String> getItems() {
        ArrayList<String> items = new ArrayList<>();
        int amount = 10;

        for (Material m : Material.values()) {
            if (amount <= 0) break;
            if (Math.random() < 0.07) {
                items.add("§7" + m.name().toLowerCase());
                amount--;
            }
        }
        return items;
    }
}
