
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel extends Thread {

    FoamChatServer server;
    FoamChatPeering peering;
    User me;
    EncryptionMachine encryptor;

    public FoamChatKernel() throws Exception {
        ChatLog chatLog = new ChatLog();
        List<String> man = new ArrayList<>();
        man.add("25.6.157.8");
        
        encryptor = new EncryptionMachine("/Users/logan/fckey");
        me = new User( encryptor.keyPair.getPublic(), "Logan", IPTools.getHomeNodes() );
        chatLog.addUser(me);
       
        chatLog.addMessage( new Message("Hi", me, me, encryptor) );
        
        peering = new FoamChatPeering(chatLog, man);
        server = new FoamChatServer(chatLog);
    }

    public void run() {
        
    }

    public static void main(String[] args) throws Exception {
        FoamChatKernel kernel = new FoamChatKernel();
        kernel.start();
    }
}
