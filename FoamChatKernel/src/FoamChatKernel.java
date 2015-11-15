
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel extends Thread {

    FoamChatServer server;
    FoamChatPeering peering;

    public FoamChatKernel() {

        ChatLog chatLog = new ChatLog();
        List<String> man = new ArrayList<>();
        man.add("25.16.95.241");

        peering = new FoamChatPeering(chatLog, man);
        server = new FoamChatServer(chatLog);

    }

    public void run() {

    }

    public static void main(String[] args) {
        FoamChatKernel kernel = new FoamChatKernel();
        kernel.start();
    }
}
