
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 * @editor Logan Walker <logan.walker@me.com>
 */
public class ChatLog {
    static public Semaphore mutex;
    public List<User> logins;
    public List<Message> messages;
    
    public ChatLog(){
        try {
            mutex = new Semaphore(1);
            mutex.acquire();
            logins = new ArrayList<>();
            messages = new ArrayList<>();
            mutex.release();
        } catch (InterruptedException ex) {
            System.exit(400);
        }
    }
    
    public void addLoginName(User newName){
        logins.add(newName);
    }
    
    public void addMessage(Message newMessage){
        messages.add(newMessage);
    }
    
    public void removeName(User name){
        if(logins.contains(name)){
            int position = logins.indexOf(name);
            logins.remove(position);
        }
    }
    
    public boolean lock(){
        try {
            this.mutex.acquire();
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }
    
    public void unlock() {
        this.mutex.release();
    }

    @SuppressWarnings("empty-statement")
    //@TODO rewite using events
    void lockWait() {
        while(!this.lock());
    }
}
