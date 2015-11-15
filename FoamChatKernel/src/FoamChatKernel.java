
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel extends Thread {

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

    public void run() {

    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        boolean running = true;
        FoamChatKernel kernel = new FoamChatKernel(in.readLine(), in.readLine(), in.readLine());

        do {
            String line = in.readLine();

            if (line.equals("lsu")) {
                chatLog.lockWait();
                for (User a : chatLog.users) {
                    System.out.println(a.id + ":" + a.displayName);
                }
                chatLog.unlock();
            } else if (line.equals("lsma")) {
                chatLog.lockWait();
                for (Message a : chatLog.messages) {
                    System.out.println(a.to + ":" + a.from + ":" + a.message);
                }
                chatLog.lockWait();
            } else if (line.startsWith("getn-")) {
                line = line.substring(4);
                chatLog.lockWait();
                for (User a : chatLog.users) {
                    if (a.id == Integer.valueOf(line)) {
                        System.out.println(a.displayName);
                        chatLog.unlock();
                        return;
                    }
                }
                System.out.println("NA");
                chatLog.unlock();
            } else if (line.equals("lsm")) {
                chatLog.lockWait();
                for (Message a : chatLog.messages) {
                    if (a.to == me.id) {
                        System.out.print(a.from + ":");
                        System.out.println(encryptor.decrypt(a.message, a.keyString));
                    }
                }
                chatLog.unlock();
            }

        } while (running);
        kernel.start();
    }
}
