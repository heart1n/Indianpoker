package heartin.plugin.indianpoker;

import nemo.mc.entity.NemoArmorStand;
import nemo.mc.entity.NemoEntity;
import nemo.mc.entity.NemoPlayer;
import nemo.mc.packet.Packet;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class GamePlayer {

    private final GamePlayerManager manager;
    private final UUID uniqueId;
    private String name;
    private Player player;
    private int ordinal;
    private final NemoArmorStand stand = NemoEntity.createEntity(ArmorStand.class);


    public GamePlayer (GamePlayerManager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        onUpdate();
        stand.setInvisible(true);
    }

    public Player getPlayer()
    {
        return player;
    }

    void setPlayer(Player player)
    {
        this.player = player;
    }
    public int ordinal()
    {
        return this.ordinal;
    }

    public UUID getUniqueId()
    {
        return this.uniqueId;
    }
    public String getName()
    {
        return this.name;
    }

    public NemoArmorStand getStand()
    {
        return this.stand;
    }

    public void onUpdate()
    {
        if (player == null)
            return;

        NemoPlayer np = NemoPlayer.fromPlayer(this.player);
        this.stand.setPositionAndRotation(np.getPosX(), np.getPosY() + GameConfig.standOffsetY, np.getPosZ(), 0F, 0F);
    }

    public void setOrdinal(int i)
    {
        this.ordinal = i;
        Packet.ENTITY.equipment(this.stand.getId(), EquipmentSlot.HEAD, GameConfig.getByOrdinal(i)).sendAll();
    }

    public void remove()
    {
        Packet.ENTITY.destroy(this.stand.getId()).sendAll();
    }
}
