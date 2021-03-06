package foamchat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel {

    public static FoamChatServer server;
    public static FoamChatPeering peering;
    public static User me;
    public static EncryptionMachine encryptor;
    public static ChatLog chatLog;

    public FoamChatKernel(String fn, String user, String bootip) throws Exception {
        chatLog = new ChatLog();

        List<String> man = new ArrayList<>();
        man.add(bootip);

        //"/Users/logan/fckey"
        encryptor = new EncryptionMachine(fn);
        me = new User(encryptor.keyPair.getPublic(), user, IPTools.getHomeNodes());
        chatLog.addUser(me);

        //chatLog.addMessage( new foamchat.Message("Hi", me, me, encryptor) );
        peering = new FoamChatPeering(chatLog, man);
        server = new FoamChatServer(chatLog);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        boolean running = true;
        FoamChatKernel kernel = new FoamChatKernel(in.readLine(), in.readLine(), in.readLine());

        String useJSONs = in.readLine();
        boolean useJSON = useJSONs.toLowerCase().startsWith("y");

        do {
            String line = in.readLine();

            chatLog.lockWait();
            if (line.equals("lsu")) {
                if(!useJSON) {
                    boolean isFirst = true;
                    for (User a : chatLog.users) {
                        System.out.println(a.displayName+":"+a.id);
                    }
                }else{
                    JSONArray jsonArray = new JSONArray();
                    for(User user : chatLog.users){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id",user.id);
                        jsonObject.put("displayName",user.displayName);
                        jsonArray.put(jsonObject);
                    }
                    System.out.println(jsonArray.toString());
                }

            } else if (line.equals("lsma")) {
                for (Message a : chatLog.messages) {
                        System.out.println(getUser(a.from).displayName + ": " + a.message);
                }
            } else if (line.startsWith("getn-")) {
                boolean found = false;
                line = line.substring(5);
                for (User a : chatLog.users) {
                    if (a.id == Integer.valueOf(line)) {
                        System.out.println(a.displayName);
                        found = true;
                        break;
                    }
                }
                if (!found && !useJSON) {
                    System.out.println("\"NA\"");
                }
            } else if (line.equals("lsm")) {
                if(!useJSON) {
                    for (Message a : chatLog.messages) {
                        System.out.println(getUser(a.from).displayName + ": " + encryptor.decrypt(a.message, a.keyString));
                    }
                }else {
                    JSONArray jsonArray = new JSONArray();
                        for (Message msg : chatLog.messages) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", msg.from);
                            jsonObject.put("name",getUser(msg.from).displayName);
                            jsonObject.put("text", encryptor.decrypt(msg.message, msg.keyString));
                            jsonArray.put(jsonObject);
                        }
                        System.out.println(jsonArray.toString());
                }
            } else if (line.startsWith("msg")) {
                String[] split = line.split(":");
                int target = Integer.valueOf(split[1]);
                Message toAdd = new Message(split[2], getUser(target), me, encryptor);
                chatLog.addMessageNonBlocking(toAdd);
            } else if (line.equals("exit")) {
                System.exit(1);
            }
            chatLog.unlock();

        } while (running);
    }

    //Assumes semaphore lock
    public static User getUser(int id) {
        for (User a : chatLog.users) {
            if (a.id == id) {
                return a;
            }
        }
        return me;
    }
}
