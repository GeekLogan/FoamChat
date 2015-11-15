
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server Thread Object
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class FoamChatServer extends Thread {

    public static final int port = 4242;
    public boolean running;

    private final String[] homeNodes;
    private final ChatLog chatLog;

    private ServerSocket serverSocket;

    public FoamChatServer(ChatLog cl) {
        this.chatLog = cl;

        homeNodes = IPTools.getHomeNodes();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println("Socket Creation Failure! (Port: " + port + ")");
            System.exit(403);
        }

        running = true;
        this.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket connection = serverSocket.accept();
                ObjectOutputStream out
                        = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(
                        connection.getInputStream());
                ResponderThread rs = new ResponderThread(out, in, chatLog);
            } catch (IOException ex) {
                System.err.println("Can not get input connection!");
                System.exit(402);
            }
        }
    }

    private class ResponderThread extends Thread {

        ObjectOutputStream out;
        ObjectInputStream in;
        ChatLog chatLog;

        public ResponderThread(ObjectOutputStream out, ObjectInputStream in, ChatLog cl) {
            this.out = out;
            this.in = in;
            this.chatLog = cl;

            //System.err.println("Responding to request!");
            this.start();
        }

        @Override
        public void run() {

            try {
                this.chatLog.lockWait();
                out.writeObject(this.chatLog);
                this.chatLog.unlock();
            } catch (IOException ex) {
                //System.err.println("Failed to Write object!");
                //Could not send
            }

            try {
                ChatLog chatIn = (ChatLog) this.in.readObject();
                this.chatLog.lockWait();
                this.chatLog.mergeLog(chatIn);
                this.chatLog.unlock();
            } catch (IOException | ClassNotFoundException ex) {
                //System.err.println("Failed to rebuild from transmission!");
                //Could not recieve
            }
        }

    }
}
