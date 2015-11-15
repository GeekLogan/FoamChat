
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Random;

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
        id = idInitializer();
        addrs = addrs_list;
    }

    // this initializes the random id
    public static int idInitializer() {
        Random rndGenerator = new Random();
        return rndGenerator.nextInt(2147483647);
    }

}
