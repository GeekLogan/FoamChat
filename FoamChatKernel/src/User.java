
import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 */
public class User implements Serializable {

    public final PublicKey key;
    public final String displayName;
    public final int id;
    public final String[] addrs;

    public User(PublicKey key_in, String display_name, String[] addrs_list) {
        key = key_in;
        displayName = display_name;
        id = LogUtilities.rand();
        addrs = addrs_list;
    }

    public boolean idEq( User in ) {
        return in.id == this.id;
    }
}
