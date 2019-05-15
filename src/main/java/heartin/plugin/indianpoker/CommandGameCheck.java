package heartin.plugin.indianpoker;

import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import nemo.mc.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandGameCheck  extends CommandComponent
{

    private GameProcess process;

    public  CommandGameCheck()
    {
        super (null , "", "game.check");
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        GameProcess process = GamePlugin.getInstance().getProcess();

        Player player = (Player) sender;

        Collection<Integer> co = GamePlayerManager.ordinal.values();
        Integer max = (Integer) Collections.max(co);

        Map <GamePlayer, Integer> maxPlayer = new HashMap();
        Set<GamePlayer> maxuser = maxPlayer.keySet();

        for (GamePlayer gamePlayer : process.getPlayerManager().getGamePlayers())
        {
            if (GamePlayerManager.ordinal.get(gamePlayer) == max)
            {
                maxPlayer.put(gamePlayer, max);
            }
        }

        for (GamePlayer gamePlayer : maxuser)
        {
            Bukkit.broadcastMessage("§b" + gamePlayer.getName() + "§7 : §6" + maxPlayer.get(gamePlayer) + " §7점");

            process.getListener().addEmerald(gamePlayer);

            for (GamePlayer online : process.getPlayerManager().getGamePlayers())
            {
                Player target = online.getPlayer();

                Packet titlePacket = Packet.TITLE.compound(gamePlayer.getName() + " 님이 " + maxPlayer.get(gamePlayer) + " §7점으로", "§b승리!", 5, 60, 10);
                titlePacket.send(target);
            }
        }

        process.getPlugin().processStop();

        return true;
    }
}
