package xyz.akiradev.deezitems.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.akiradev.deezitems.DeezItems;
import xyz.akiradev.deezitems.manager.ConfigurationManager.Setting;
import xyz.akiradev.deezitems.manager.LocaleManager;
import xyz.akiradev.pluginutils.utils.HexUtils;
import xyz.akiradev.pluginutils.utils.StringPlaceholders;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {
    private final String abilityName;
    private final String abilityDescription;
    private final AbilityTypes abilityType;
    private int abilityCooldown;

    private static LocaleManager localeManager = DeezItems.getInstance().getManager(LocaleManager.class);
    public ItemAbility(String abilityName, String abilityDescription, AbilityTypes abilityType, int abilityCooldown) {
        this.abilityName = abilityName;
        this.abilityDescription = abilityDescription;
        this.abilityType = abilityType;
        this.abilityCooldown = abilityCooldown;
    }

    public ItemAbility(String abilityName, String abilityDescription, AbilityTypes abilityType) {
        this.abilityName = abilityName;
        this.abilityDescription = abilityDescription;
        this.abilityType = abilityType;
    }

    public List<String> setLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(HexUtils.colorify(ChatColor.GOLD + "Item Ability: " + this.abilityName + " &b&l" + this.abilityType.getName()));
        lore.addAll(ItemUtils.convertStringToLore(this.abilityDescription, 40, ChatColor.GRAY));
        if(this.abilityCooldown > 0) {
            lore.add(HexUtils.colorify(ChatColor.GOLD + "Cooldown: " + this.abilityCooldown + " seconds"));
        }

        return lore;
    }

    public static boolean enforceCooldown(Player player, String cooldownName, double cooldown, ItemStack item, boolean sendMessage){
        LocaleManager localeManager = DeezItems.getInstance().getManager(LocaleManager.class);
        double systime = (double)System.currentTimeMillis() / 1000.0;
        int cooldownTime = ItemUtils.getIntFromItem(item, cooldownName);
        if(cooldownTime <= 0){
            ItemUtils.storeIntInItem(item, (int)systime, cooldownName);
            return false;
        } else if(systime - cooldown > (double)cooldownTime){
            ItemUtils.storeIntInItem(item, (int)systime, cooldownName);
            return false;
        } else {
            if(Setting.USE_ACTIONBAR.getBoolean()){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(HexUtils.colorify(localeManager.getLocaleMessage("cooldown-message", StringPlaceholders.single("time", (cooldownTime - (int)(systime - cooldown)))))));
                }else {
                    localeManager.sendMessage(player, "cooldown-message", StringPlaceholders.single("time", (cooldownTime - (int)(systime - cooldown))));
                }
            }
            return true;
        }
    public enum AbilityTypes {
        NONE(""),
        LEFT_CLICK(localeManager.getLocaleMessage("left-click")),
        RIGHT_CLICK(localeManager.getLocaleMessage("right-click")),
        MIDDLE_CLICK(localeManager.getLocaleMessage("middle-click")),
        BLOCK_BREAK(localeManager.getLocaleMessage("block-break")),
        PROJECTILE_HIT(localeManager.getLocaleMessage("projectile-hit"));

        private final String name;

        AbilityTypes(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
