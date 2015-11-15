
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel {

    private static FoamChatServer server;
    private static FoamChatPeering peering;
    private static User me;
    private static EncryptionMachine encryptor;
    private static ChatLog chatLog;

    public FoamChatKernel(String fn, String user, String bootip) throws Exception {
        chatLog = new ChatLog();

        List<String> man = new ArrayList<>();
        man.add(bootip);

        //"/Users/logan/fckey"
        encryptor = new EncryptionMachine(fn);
        me = new User(encryptor.keyPair.getPublic(), user, IPTools.getHomeNodes());
        chatLog.addUser(me);

        //chatLog.addMessage( new Message("Hi", me, me, encryptor) );
        peering = new FoamChatPeering(chatLog, man);
        server = new FoamChatServer(chatLog);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        boolean running = true;
        FoamChatKernel kernel = new FoamChatKernel(in.readLine(), in.readLine(), in.readLine());

        do {
            String line = in.readLine();

            chatLog.lockWait();
            if (line.equals("lsu")) {
                for (User a : chatLog.users) {
                    System.out.println(a.id + ":" + a.displayName);
                }
            } else if (line.equals("lsma")) {
                for (Message a : chatLog.messages) {
                    System.out.println(a.to + ":" + a.from + ":" + a.message);
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
                if (!found) {
                    System.out.println("NA");
                }
            } else if (line.equals("lsm")) {
                for (Message a : chatLog.messages) {
                    if (a.to == me.id) {
                        System.out.print(a.from + ":");
                        System.out.println(encryptor.decrypt(a.message, a.keyString));
                    }
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
    private static User getUser(int id) {
        for (User a : chatLog.users) {
            if (a.id == id) {
                return a;
            }
        }
        return me;
    }
}
