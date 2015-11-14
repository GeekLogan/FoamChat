
import java.io.Serializable;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 */
public class Message implements Serializable{
    final int userID;
    final String[] message;
    
    public Message(int I_D, String[] message_eh){
        userID = I_D;
        message = message_eh;
    }
}
