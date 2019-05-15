package heartin.plugin.indianpoker;

import nemo.mc.item.NemoItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameListener implements Listener {


    private GameProcess process;
    public static Map<Player, Integer> itemHash = new HashMap();
    public static ArrayList<Integer> itemAmount = new ArrayList();

    ItemStack emerald = new ItemStack(Material.EMERALD, itemAmount.size());


    GameListener(GameProcess process) {
        this.process = process;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.process.getPlayerManager().onJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuitPlayer(PlayerQuitEvent event) {
        this.process.getPlayerManager().onQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        NemoItemStack item = NemoItemStack.newInstance(388);

        Action action = event.getAction();
        Player player = event.getPlayer();

        if  (player.getItemInHand().getTypeId() == 388) {
            if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {

                player.sendMessage("난 에메랄드다");
                itemHash.put(player, Integer.valueOf((Integer) itemHash.get(player)).intValue() + 1);
                Bukkit.broadcastMessage("§b" + player.getName() + " §r님이 §6" + itemHash.get(player) + " §r개를 겁니다.");

                player.getPlayer().getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                itemAmount.add(itemHash.get(player).intValue());

                Bukkit.broadcastMessage("amount value : "  +  itemAmount.size());

                ItemStack emerald = new ItemStack(Material.EMERALD, itemAmount.size());
                this.emerald = emerald;

                if(player.getInventory().getItemInHand().getAmount() == 0)
                {
                    Bukkit.broadcastMessage(" 올인");
                }
            }
        }
    }

    public void addEmerald(GamePlayer gamePlayer)
    {
        Player player = gamePlayer.getPlayer();
        player.getInventory().addItem(emerald);
    }

}

