package heartin.plugin.indianpoker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {


    private GameProcess process;

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


}
