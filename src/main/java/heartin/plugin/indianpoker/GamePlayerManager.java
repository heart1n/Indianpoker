package heartin.plugin.indianpoker;

import nemo.mc.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class GamePlayerManager {

    private GameProcess process;

     Map<UUID, GamePlayer> playersByUniqueId;
     public static  Map<GamePlayer, Integer> ordinal = new HashMap();

    GamePlayerManager(GameProcess process) {
        this.process = process;

        Collection<? extends Player> players = process.getPlugin().getServer().getOnlinePlayers();
        int size = players.size();
        Map playersByUniqueId = new HashMap(size);

        for (Player player : players)
        {
            GameMode mode = player.getGameMode();

            if (mode == GameMode.SPECTATOR)
            {
                continue;
            }
            GamePlayer gamePlayer = new GamePlayer(this, player);
            playersByUniqueId.put(gamePlayer.getUniqueId(), gamePlayer);

        }

        if (playersByUniqueId.size() < 2)
        {
            throw  new IllegalArgumentException("게임의 필요한 인원이 부족합니다 (최소 2명)");
        }

        this.playersByUniqueId = playersByUniqueId;
        List<GamePlayer> other = new ArrayList<>(playersByUniqueId.values());
        Collections.shuffle(other);

        Random random = new Random();

        int ordinal = 10;
        for (GamePlayer gamePlayer : other)
        {
            gamePlayer.setOrdinal(random.nextInt(ordinal));

            Player player = gamePlayer.getPlayer();

            GamePlayerManager.ordinal.put(gamePlayer, gamePlayer.ordinal());
            GameListener.itemHash.put(player,  Integer.valueOf(0));
        }
        setEntity();
    }

    public void setEntity()
    {
        for (GamePlayer gamePlayer : this.playersByUniqueId.values())
        {
            Packet.ENTITY.spawnMob(gamePlayer.getStand().getBukkitEntity()).sendAll();
            Packet.ENTITY.metadata(gamePlayer.getStand().getBukkitEntity()).sendAll();;
            Packet.ENTITY.equipment(gamePlayer.getStand().getId(), EquipmentSlot.HEAD, GameConfig.getByOrdinal(gamePlayer.ordinal())).sendAll();
        }
    }

    public Collection<GamePlayer> getGamePlayers()
    {
        return this.playersByUniqueId.values();
    }

    public void removePlayer(UUID uniqueId)
    {
        this.playersByUniqueId.remove(uniqueId);
    }

    void onJoin(Player player)
    {
        GamePlayer gamePlayer = this.playersByUniqueId.get(player.getUniqueId());

        if (gamePlayer != null)
            gamePlayer.setPlayer(player);

        for (GamePlayer other : this.playersByUniqueId.values())
        {
            Packet.ENTITY.spawnMob(other.getStand().getBukkitEntity()).send(player);
            Packet.ENTITY.metadata(other.getStand().getBukkitEntity()).send(player);
            Packet.ENTITY.equipment(other.getStand().getId(), EquipmentSlot.HEAD, GameConfig.getByOrdinal(other.ordinal())).send(player);
        }
    }

    void onQuit(Player player)
    {
        GamePlayer gamePlayer = this.playersByUniqueId.get(player.getUniqueId());

        if (gamePlayer != null)
            gamePlayer.setPlayer(null);
    }
}
