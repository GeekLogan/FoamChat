
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
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

    public void setStop() {
        running = false;
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
        String[] locals = IPTools.getHomeNodes();
        for (int i = 0; i < locals.length; i++) {
            if (locals[i].equals(ip)) {
                return;
            }
        }

        System.err.println("Trying Connect... (" + ip + ")");
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            System.err.println("before");
            //Socket sock = new Socket(ip, FoamChatServer.port, 10);
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(ip, FoamChatServer.port), 1000);
            System.err.println("after");
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            System.err.println("Could not connect");
            //can't connect
        }

        if (in != null && out != null) {
            try {
                this.chatLog.lockWait();
                ChatLog recieved = null;
                recieved = (ChatLog) in.readObject();
                out.writeObject(this.chatLog);
                if (recieved != null) {
                    recieved.rebuildLock();
                    recieved.lockWait();
                    this.chatLog.mergeLog(recieved);
                    recieved.unlock();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Other Broke");
                //Do nothing...
            }
        }
        LogUtilities.sortFields(this.chatLog);
    }
}
