
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class FoamChatPeering extends Thread {

    private final ChatLog chatLog;
    private boolean running;

    public FoamChatPeering(ChatLog cl) {
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
            }
        }
    }

    private void tryConnect(String ip) {
        try {
            Socket sock = new Socket(ip, FoamChatServer.port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in
                    = new ObjectInputStream(sock.getInputStream());
            
            this.chatLog.lockWait();
            
            ChatLog recieved = (ChatLog) in.readObject();
            out.writeObject(this.chatLog);
            recieved.rebuildLock();
            recieved.lockWait();
            
            this.chatLog.mergeLog(recieved);
            recieved.unlock();
            LogUtilities.sortFields(this.chatLog);
            
        } catch (IOException | ClassNotFoundException ex) {
            //can't connect
        } finally {
            this.chatLog.unlock();
        }
    }
}
