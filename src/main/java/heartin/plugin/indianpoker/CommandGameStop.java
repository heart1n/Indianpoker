package heartin.plugin.indianpoker;

import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandGameStop extends CommandComponent {


    CommandGameStop() {
        super(null, "게임을 종료합니다", "game.stop");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        if (GamePlugin.getInstance().processStop()) {
            sender.sendMessage("게임을 중지했습니다.");
        } else {
            sender.sendMessage("게임이 실행 중이지 않습니다.");
        }


        return true;
    }
}
