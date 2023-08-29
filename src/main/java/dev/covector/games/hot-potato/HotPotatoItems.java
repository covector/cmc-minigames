package dev.covector.cmcminigames.hotpotato;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Arrays;

public class HotPotatoItems {
    public void giveHotPotatoItems(Player player, HotPotato.Mode mode) {
        player.getInventory().clear();

        // give armor
        if (mode == HotPotato.Mode.SUSSY) {
            equipColoredArmor(player, Color.AQUA);
        } else if (mode == HotPotato.Mode.CLASSIC) {
            equipColoredArmor(player, Color.RED);
        }

        // give potato
        ItemStack item = new ItemStack(Material.BAKED_POTATO);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Hot Potato");
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Pass this to another player before it explodes!"));
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        item.setItemMeta(itemMeta);
        player.getInventory().addItem(item);

        // give speed
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    }

    public void giveSurvivorItems(Player player) {
        // give armor
        player.getInventory().clear();
        equipColoredArmor(player, Color.AQUA);

        // remove speed
        player.removePotionEffect(PotionEffectType.SPEED);
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