
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert McKay <mckay.130@osu.edu>
 */
public class ChatLog {
    public List<String> loginName;
    public List<String> message;
    
    public ChatLog(){
        loginName = new ArrayList<>();
        message = new ArrayList<>();
    }
    
    public void addLoginName(String newName){
        loginName.add(newName);
    }
    
    public void addMessage(String newMessage){
        message.add(newMessage);
    }
    
    public void removeName(String name){
        if(loginName.contains(name)){
            int position = loginName.indexOf(name);
            loginName.remove(position);
        }
    }
}
