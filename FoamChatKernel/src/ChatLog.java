
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 * @editor Logan Walker <logan.walker@me.com>
 * 
 */
public class ChatLog implements Serializable {

    static public Semaphore mutex;
    public List<User> logins;
    public List<Message> messages;

    public ChatLog() {
        mutex = new Semaphore(1);
        this.lockWait();
        logins = new ArrayList<>();
        messages = new ArrayList<>();
        this.unlock();
    }

    public void addLoginName(User newName) {
        this.lockWait();
        logins.add(newName);
        this.unlock();
    }

    public void addMessage(Message newMessage) {
        this.lockWait();
        messages.add(newMessage);
        this.unlock();
    }

    public void removeName(User name) {
        this.lockWait();
        if (logins.contains(name)) {
            int position = logins.indexOf(name);
            logins.remove(position);
        }
        this.unlock();
    }
    
    public void mergeLog( ChatLog in ) {
        in.lockWait();
        this.lockWait();
        
        
        
        in.unlock();
        this.unlock();
    }

    public boolean lock() {
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
        while (!this.lock());
    }
}
