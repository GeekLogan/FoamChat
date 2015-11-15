
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 * @editor Logan Walker <logan.walker@me.com>
 */
public class Message implements Serializable {

    final String message;
    final Date created;
    final int from, to; //user # of sender/reciever
    final boolean isEncrypted;
    //final FoamFile file;

    public Message(String message_in, String key, User to, User from, FoamFile file) {
        isEncrypted = (key == null);
        if (isEncrypted) {
            //encrypt

        }

        this.message = message_in;
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
