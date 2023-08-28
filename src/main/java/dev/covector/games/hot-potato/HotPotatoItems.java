package dev.covector.cmcminigames.hotpotato;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Color;

import java.util.List;

public class HotPotatoItems {
    public void giveHotPotatoItems(Player player, HotPotato.Mode mode) {
        if (mode == HotPotato.Mode.SUSSY) {
            giveSurvivorItems(player);
        } else if (mode == HotPotato.Mode.CLASSIC) {
            player.getInventory().clear();
            // give red armor
            equipColoredArmor(player, Color.RED);
        }

        // give potato
        ItemStack item = new ItemStack(Material.POTATO);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Hot Potato");
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Pass this to another player before it explodes!"));
        item.setItemMeta(itemMeta);
        player.getInventory().addItem(item);
    }

    public void giveSurvivorItems(Player player) {
        player.getInventory().clear();
        // give blue armor
        equipColoredArmor(player, Color.AQUA);
    }

    private void equipColoredArmor(Player player, Color color) {
        ItemStack[] armor = {
            new ItemStack(Material.LEATHER_BOOTS),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_HELMET)
        };
        for (ItemStack item : armor) {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) item.getItemMeta();
            itemMeta.setColor(color);
            item.setItemMeta(itemMeta);
        }
        player.getInventory().setArmorContents(armor);
    }
}