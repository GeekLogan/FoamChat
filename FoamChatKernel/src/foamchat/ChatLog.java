package foamchat;

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
        this.lock();
        users = new User[0];
        messages = new Message[0];
        this.unlock();
    }

    public void addUserNonBlock(User newName) {
        User[] newArr = new User[users.length + 1];
        for (int i = 0; i < users.length; i++) {
            newArr[i] = users[i];
        }
        newArr[users.length] = newName;
        users = newArr;
    }

    public void addUser(User newName) {
        this.lockWait();
        addUserNonBlock(newName);
        this.unlock();
    }

    public void addMessageNonBlocking(Message newMessage) {
        Message[] newArr = new Message[messages.length + 1];
        for (int i = 0; i < messages.length; i++) {
            newArr[i] = messages[i];
        }
        newArr[messages.length] = newMessage;
        messages = newArr;
    }

    public void addMessage(Message newMessage) {
        this.lockWait();
        addMessageNonBlocking(newMessage);
        this.unlock();
    }

    public void mergeLog(ChatLog in) {
        for (int j = 0; j < in.messages.length; j++) {
            boolean isIn = false;
            for (int i = 0; i < this.messages.length && !isIn; i++) {
                if (this.messages[i].roughEq(in.messages[j])) {
                    isIn = true;
                }
            }
            if (!isIn) {
                this.addMessageNonBlocking(in.messages[j]);
            }
        }

        for (int j = 0; j < in.users.length; j++) {
            boolean isIn = false;
            for (int i = 0; i < this.users.length && !isIn; i++) {
                if (this.users[i].idEq(in.users[j])) {
                    isIn = true;
                }
            }
            if (!isIn) {
                this.addUserNonBlock(in.users[j]);
            }
        }

        LogUtilities.sortFields(this);
        //foamchat.LogUtilities.statLog(this);
        //foamchat.LogUtilities.dumpLog(this);

    }

    public final boolean lock() {
        boolean didget = this.mutex.tryAcquire();
        return didget;
    }

    public final void unlock() {
        this.mutex.release();
    }

    public void lockWait() {
        this.mutex.acquireUninterruptibly();
    }

    final void rebuildLock() {
        this.mutex = new Semaphore(1);
    }

}
