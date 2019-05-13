package heartin.plugin.indianpoker;

import nemo.mc.entity.NemoArmorStand;
import nemo.mc.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GameTask implements Runnable {

    private final GameProcess process;

    public GameTask(GameProcess process)
    {
        this.process = process;
    }

    @Override
    public void run() {
        for (GamePlayer gamePlayer : this.process.getPlayerManager().getGamePlayers())
            gamePlayer.onUpdate();

        // update players
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (GamePlayer gamePlayer : this.process.getPlayerManager().getGamePlayers()) {
                NemoArmorStand stand = gamePlayer.getStand();

                Vector vec = stand.getBukkitEntity().getEyeLocation().subtract(player.getEyeLocation()).toVector().normalize();
                double yaw = -Math.toDegrees(Math.atan2(vec.getX(), vec.getZ()));
                double pitch = -Math.toDegrees(Math.asin(vec.getY()));
                Location loc = player.getLocation();
                loc.setYaw((float) yaw);
                Packet.ENTITY.teleport(stand.getBukkitEntity(), stand.getPosX(), stand.getPosY(), stand.getPosZ(), (float) yaw, (float) pitch, false).send(player);
            }
        }
    }
}
