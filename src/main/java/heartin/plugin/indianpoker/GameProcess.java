package heartin.plugin.indianpoker;

import nemo.mc.packet.Packet;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public final class GameProcess {


    private final GamePlugin plugin;
    private final GamePlayerManager manager;
    private final GameListener listener;
    private final BukkitTask task;

    GameProcess(GamePlugin plugin) {
        this.plugin = plugin;
        this.manager = new GamePlayerManager(this);
        this.listener = new GameListener(this);
        this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, new GameTask(this), 0L, 1L);

        plugin.getServer().getPluginManager().registerEvents(this.listener, plugin);
    }

    public GamePlugin getPlugin() {
        return this.plugin;
    }

    public GamePlayerManager getPlayerManager() {
        return this.manager;
    }

    public GameListener getListener() {
        return this.listener;
    }

    void unregister() {

        HandlerList.unregisterAll(this.listener);
        this.task.cancel();

        for (GamePlayer gamePlayer : this.getPlayerManager().playersByUniqueId.values()) {
            Packet.ENTITY.destroy(gamePlayer.getStand().getBukkitEntity().getEntityId()).sendAll();
            GamePlayerManager.ordinal.clear();
            GameListener.itemHash.clear();
            GameListener.itemAmount.clear();
        }
    }


}
