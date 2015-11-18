package foamchat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Robert McKay <mckay.130@osu.edu>
 * @editor Logan Walker <logan.walker@me.com>
 */
public class Message implements Serializable {

    final String message, keyString;
    final Date created;
    final int from, to; //user # of sender/reciever
    //final FoamFile file;

    public Message(String message_in, User to, User from, EncryptionMachine em) throws Exception {//, foamchat.FoamFile file) {
        String[] enc = em.encrypt(message_in, to.key);

        this.keyString = enc[0];
        this.message = enc[1];

        created = new Date();

        this.to = to.id;
        this.from = from.id;
        //this.file = file;
        //this.file.ip = from.addrs;
    }

    public Date createdAt() {
        return created;
    }

    boolean roughEq(Message in) {
        return in.message.equals(this.message) && in.created.equals(this.created) && in.to == this.to && in.from == this.from;
    }
}
