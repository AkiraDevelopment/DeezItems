package xyz.akiradev.deezitems.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class DeezItem {
    private Material material;
    private String name;
    private ItemRarity rarity;
    private int amount;
    private List<String> lore;
    private int itemID;
    private int uses;
    private List<ItemAbility> abilities;


    public DeezItem(Material material ,String name, ItemRarity rarity, int amount, List<String> lore, int uses, List<ItemAbility> abilities) {
        this.material = material;
        this.name = name;
        this.rarity = rarity;
        this.amount = amount;
        this.lore = lore;
        this.itemID = ItemUtils.stringToSeed(material.name() + name + rarity.name());
        this.uses = uses;
        this.abilities = abilities;
    }

    public ItemStack Generate(int Amount) {
        ItemStack item = ItemUtils.nameItem(this.material, this.rarity.getColor() + this.name);
        ItemUtils.storeIntInItem(item, itemID, "itemID");
        ItemUtils.storeIntInItem(item, uses, "uses");
        ItemUtils.setItemLore(item, getLore(item));
        item.setAmount(Amount);
        return item;
    }

    private List<String> getLore(ItemStack item){
        ArrayList<String> list = new ArrayList<>();
        int uses = ItemUtils.getIntFromItem(item, "uses");
        if(rarity.equals(ItemRarity.UNFINISHED)){
            list.add("Item Unfinished");
            list.add("Likely to be broken");
        }

        list.add("");

        for (ItemAbility ability : abilities) {
            list.addAll(ability.setLore());
            list.add("");
        }
        if(uses >= 1){
            list.add(TextUtils.colorize("&b&l(&a&lUses: " + uses + "&b&l)"));
        }
        list.add(TextUtils.colorize(rarity.getColor() + "&l" + rarity));
        return list;
    }

    public void onItemUse(Player player, ItemStack item) {
        int itemuses = ItemUtils.getIntFromItem(item, "uses");
        if(itemuses == 1 && player.getGameMode() != GameMode.CREATIVE){
            destroyItem(item, 1);
            if(item.getAmount() > 0) {
                ItemUtils.storeIntInItem(item, this.uses, "uses");
                ItemUtils.setItemLore(item, getLore(item));
            }
        }
        if(ItemUtils.getIntFromItem(item, "uses") > 0 && player.getGameMode() != GameMode.CREATIVE){
            itemuses--;
            ItemUtils.storeIntInItem(item, itemuses, "uses");
            ItemUtils.setItemLore(item, getLore(item));
        }
    }

    public static void destroyItem(ItemStack item, int amount){
        if(item.getAmount() <= amount){
            item.setAmount(0);
        } else {
            item.setAmount(item.getAmount() - amount);
        }
    }

    /**
     * event when player left clicks air
     */
    public abstract boolean leftClickAirAction(Player player, ItemStack item);
    /**
     * event when player left clicks block
     */
    public abstract boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);
    /**
     * event when player shift left clicks air
     */
    public abstract boolean shiftleftClickAirAction(Player player, ItemStack item);
    /**
     * event when player shift left clicks block
     */
    public abstract boolean shiftleftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);
    /**
     * event when player right clicks air
     */
    public abstract boolean rightClickAirAction(Player player, ItemStack item);
    /**
     * event when player right clicks block
     */
    public abstract boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);
    /**
     * event when player shift right clicks air
     */
    public abstract boolean shiftrightClickAirAction(Player player, ItemStack item);
    /**
     * event when player shift right clicks block
     */
    public abstract boolean shiftrightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);
    /**
     * event when player middle clicks
     */
    public abstract boolean middleClickAction(Player player, ItemStack item);
    /**
     * event when player breaks block
     */
    public abstract boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item);

    public abstract boolean projectileHitAction(Player player, ProjectileHitEvent event, ItemStack item);

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public int getItemID() {
        return itemID;
    }

}
