
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class FoamChatPeering extends Thread {

    private final ChatLog chatLog;
    private boolean running;
    private List<String> manualIPs;

    public FoamChatPeering(ChatLog cl, List<String> man) {
        manualIPs = man;
        chatLog = cl;
        running = true;
        this.start();
    }

    public void run() {
        while (running) {
            chatLog.lock();
            List<User> users = new ArrayList(chatLog.users);
            chatLog.unlock();
            for (User u : users) {
                for (String ip : u.addrs) {
                    tryConnect(ip);
                }
                for (String ip : manualIPs) {
                    tryConnect(ip);
                }
            }
        }
    }

    private void tryConnect(String ip) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            Socket sock = new Socket(ip, FoamChatServer.port);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            //can't connect
        }

        try {
            this.chatLog.lockWait();
            ChatLog recieved = null;
            if (in != null) {
                recieved = (ChatLog) in.readObject();
            }
            if (out != null) {
                out.writeObject(this.chatLog);
            }
            if (recieved != null) {
                recieved.rebuildLock();
                recieved.lockWait();
                this.chatLog.mergeLog(recieved);
                recieved.unlock();
            }
        } catch (IOException | ClassNotFoundException e) {
            //Do nothing...
        }
        LogUtilities.sortFields(this.chatLog);
    }
}
