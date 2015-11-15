
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
            //Socket sock = new Socket(ip, FoamChatServer.port, 10);
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(ip, FoamChatServer.port), 1500);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            System.err.println("Could not connect");
            //can't connect
        }

        if (in != null && out != null) {
            try {
                System.err.println("Before");
                this.chatLog.lockWait();
                System.err.println("...1");
                ChatLog recieved = null;
                System.err.println("...2 (read Object)");
                recieved = (ChatLog) in.readObject();
                System.err.println("...3");
                out.writeObject(this.chatLog);
                System.err.println("...4");
                if (recieved != null) {
                    System.err.println("...5");
                    recieved.rebuildLock();
                    System.err.println("...6");
                    recieved.lockWait();
                    System.err.println("...7");
                    this.chatLog.mergeLog(recieved);
                    System.err.println("...8");
                    recieved.unlock();
                    System.err.println("...9");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Other Broke");
                //Do nothing...
            }
        }
        LogUtilities.sortFields(this.chatLog);
    }
}
