
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    public List<User> users;
    public List<Message> messages;

    public ChatLog() {
        this.rebuildLock();
        this.lockWait();
        users = new ArrayList<>();
        messages = new ArrayList<>();
        this.unlock();
    }

    public void addLoginName(User newName) {
        this.lockWait();
        users.add(newName);
        this.unlock();
    }

    public void addMessage(Message newMessage) {
        this.lockWait();
        messages.add(newMessage);
        this.unlock();
    }

    public void removeName(User name) {
        this.lockWait();
        if (users.contains(name)) {
            users.remove(users.indexOf(name));
        }
        this.unlock();
    }

    public void mergeLog(ChatLog in) {
        in.lockWait();
        this.lockWait();

        for (Message a : in.messages) {
            if (!this.messages.contains(a)) {
                this.messages.add(a);
            }
        }

        for (User a : in.users) {
            if (!this.users.contains(a)) {
                this.users.add(a);
            }
        }

        in.unlock();
        LogUtilities.sortFields(this);

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

    void rebuildLock() {
        this.mutex = new Semaphore(1);
    }
}
