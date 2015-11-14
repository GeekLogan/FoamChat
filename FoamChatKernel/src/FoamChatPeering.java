
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
    
    public FoamChatPeering( ChatLog cl ) {
        chatLog = cl;
        running = true;
        this.start();
    }
    
    public void run() {
        while( running ) {
            chatLog.lock();
            List<User> users = new ArrayList(chatLog.users);
            chatLog.unlock();
            for( User u : users ) {
                for( String ip : u.addrs ) {
                    tryConnect(ip);
                }
            }
        }
    }

    private void tryConnect(String ip) {
        
    }
}
