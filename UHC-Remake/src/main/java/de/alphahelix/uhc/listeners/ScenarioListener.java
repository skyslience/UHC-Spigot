package de.alphahelix.uhc.listeners;

import de.alphahelix.alphaapi.listener.SimpleListener;
import de.alphahelix.uhc.UHC;
import de.alphahelix.uhc.register.UHCFileRegister;
import de.alphahelix.uhc.register.UHCRegister;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScenarioListener extends SimpleListener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInHand() == null)
            return;
        if (!(UHC.isScenarios() || UHC.isScenarioVoting()))
            return;

        if (e.getPlayer().getInventory().getItemInHand().getType()
                == UHCFileRegister.getScenarioFile().getItem(null).getItemStack().getType()
                &&

                ChatColor.stripColor(e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName())
                        .equals(ChatColor
                                .stripColor(UHCFileRegister.getScenarioFile().getItem(null).getItemStack().getItemMeta().getDisplayName()))) {
            e.setCancelled(true);
            UHCRegister.getScenarioInventory().openInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem() == null)
            return;
        if (!ChatColor.stripColor(e.getClickedInventory().getTitle())
                .equals(ChatColor.stripColor(UHCFileRegister.getScenarioFile().getInventoryName())))
            return;

        e.setCancelled(true);

        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(" "))
            return;

        UHCRegister.getScenarioInventory().castVote((Player) e.getWhoClicked(),
                UHCFileRegister.getScenarioFile().getScenarioByItem(e.getCurrentItem()));
    }

    @EventHandler
    public void onInvClickInManagement(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem() == null)
            return;
        if (!ChatColor.stripColor(e.getInventory().getTitle()).equals("Change Scenarios"))
            return;

        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
            e.setCancelled(true);
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(UHCFileRegister.getKitsFile().getNextItem().getItemStack().getItemMeta().getDisplayName()))
            return;
        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(UHCFileRegister.getKitsFile().getPreviousItem().getItemStack().getItemMeta().getDisplayName()))
            return;


        e.setCancelled(true);

        UHCFileRegister.getScenarioFile().toggleScenarioStatus(UHCFileRegister.getScenarioFile().getScenarioByItem(e.getCurrentItem()));
        e.getWhoClicked().closeInventory();
        UHCRegister.getScenarioAdminInventory().fillInventory((Player) e.getWhoClicked());
    }
}
