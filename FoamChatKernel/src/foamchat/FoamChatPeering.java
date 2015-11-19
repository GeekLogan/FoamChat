package foamchat;

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
            List<String> ips = new ArrayList<>(manualIPs);
            chatLog.lockWait();
            for (User u : chatLog.users) {
                for (String ip : u.addrs) {
                    ips.add(ip);
                }
            }
            chatLog.unlock();

            for (String ip : ips) {
                tryConnect(ip);
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
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
            //Socket sock = new Socket(ip, foamchat.FoamChatServer.port, 10);
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(ip, FoamChatServer.port), 1500);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            System.err.println("Could not connect");
            //can't connect
        }

        this.chatLog.lockWait();
        if (in != null && out != null) {
            try {
                ChatLog recieved = null;
                System.err.println("... read Object");
                recieved = (ChatLog) in.readObject();
                out.writeObject(this.chatLog);
                if (recieved != null) {
                    this.chatLog.mergeLog(recieved);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Peer-processing Broke");
            }
        }
        this.chatLog.unlock();
    }
}
