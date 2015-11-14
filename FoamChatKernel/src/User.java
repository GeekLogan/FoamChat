
import java.security.PublicKey;
import java.util.Random;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 */
public class User {

    private PublicKey key;
    private String displayName;
    private int id;

    public User(PublicKey key_in, String display_name) {
        key = key_in;
        displayName = display_name;
        id = idInitializer();
    }

    public void changeDisplayName(String newDisplay) {
        displayName = newDisplay;
    }

    public String getDisplayName() {
        return displayName;
    }

    // this initializes the random id
    public static int idInitializer() {
        Random rndGenerator = new Random();
        int randomInt = rndGenerator.nextInt(2147483647);

        return randomInt;
    }

    public int getID() {
        return id;
    }

    public PublicKey getKey() {
        return key;
    }

}
