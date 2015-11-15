
import java.io.Serializable;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 * @editor Logan Walker <logan.walker@me.com>
 *
 */
public class ChatLog implements Serializable, Cloneable {

    public Semaphore mutex;
    public User[] users;
    public Message[] messages;

    public ChatLog() {
        this.rebuildLock();
        this.lockWait();
        users = new User[0];
        messages = new Message[0];
        this.unlock();
    }

    public void addUser(User newName) {
        this.lockWait();
        User[] newArr = new User[users.length + 1];
        for (int i = 0; i < users.length; i++) {
            newArr[i] = users[i];
        }
        newArr[users.length] = newName;
        users = newArr;
        this.unlock();
    }

    public void addMessage(Message newMessage) {
        this.lockWait();

        Message[] newArr = new Message[messages.length + 1];
        for (int i = 0; i < messages.length; i++) {
            newArr[i] = messages[i];
        }
        newArr[messages.length] = newMessage;
        messages = newArr;

        this.unlock();
    }

    public void mergeLog(ChatLog in) {

        System.err.println("\t...Merge messages");
        for ( int j = 0; j < in.messages.length; j++ ) {
            boolean isIn = false;
            for( int i = 0; i < this.messages.length && !isIn; i++ ) {
                if(this.messages[i].equals(in.messages[j])) isIn = true;
            }
            if( !isIn ) {
                this.addMessage(in.messages[j]);
            }
        }

        System.err.println("\t...Merge users");
        for ( int j = 0; j < in.users.length; j++ ) {
            boolean isIn = false;
            for( int i = 0; i < this.users.length && !isIn; i++ ) {
                if(this.users[i].equals(in.users[j])) isIn = true;
            }
            if( !isIn ) {
                this.addUser(in.users[j]);
            }
        }

        System.err.println("\t...unlock");

        LogUtilities.sortFields(this);
    }

    public boolean lock() {
        boolean didget = this.mutex.tryAcquire();
        if(didget) System.err.println("--> Locked");
        return didget;
    }

    public void unlock() {
        this.mutex.release();
        System.err.println("--> Unlocked!");
    }

    @SuppressWarnings("empty-statement")
    //@TODO rewite using events
    void lockWait() {
        this.mutex.acquireUninterruptibly();
        System.err.println("--> Locked!");
    }

    void rebuildLock() {
        this.mutex = new Semaphore(1);
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
