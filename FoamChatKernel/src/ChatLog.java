
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 */
public class ChatLog {
    public List<User> logins;
    public List<Message> messages;
    
    public ChatLog(){
        logins = new ArrayList<>();
        messages = new ArrayList<>();
    }
    
    public void addLoginName(User newName){
        logins.add(newName);
    }
    
    public void addMessage(Message newMessage){
        messages.add(newMessage);
    }
    
    public void removeName(User name){
        if(logins.contains(name)){
            int position = logins.indexOf(name);
            logins.remove(position);
        }
    }
}
