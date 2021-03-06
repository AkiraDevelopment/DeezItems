package xyz.akiradev.deezitems.events.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import xyz.akiradev.deezitems.utils.DeezItem;
import xyz.akiradev.deezitems.utils.ItemUtils;

public class EventProjectileHit implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if(ItemUtils.isDeez(player.getInventory().getItemInMainHand())){
                useItem(event, player.getInventory().getItemInMainHand());
            }
        }
    }

    private void useItem(ProjectileHitEvent event, ItemStack item) {
        Player player = (Player) event.getEntity().getShooter();
        DeezItem deezItem = ItemUtils.getDeezItem(item);
        if (deezItem != null) {
            if(deezItem.projectileHitAction(player, event, item)){
                deezItem.onItemUse(player, item);
            }
        }
    }

}
