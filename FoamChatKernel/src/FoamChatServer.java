
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server Thread Object
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class FoamChatServer extends Thread {

    public final int port = 1010;
    public boolean running;

    private List<NodeReference> nodes;
    private List<NodeReference> homeNodes;
    private ChatLog chatLog;

    private ServerSocket serverSocket;

    public FoamChatServer(List<NodeReference> nodes_in, ChatLog cl) {
        this.chatLog = cl;
        nodes = nodes_in;
        homeNodes = new ArrayList<>();

        try {
            for (String ip : IPTools.getCurrentIP()) {
                homeNodes.add(new NodeReference(ip));
            }
        } catch (SocketException ex) {
            System.err.println("No IP Interfaces Found!");
            System.exit(404);
        }

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
            this.start();
        }

        @Override
        public void run() {
            try {
                this.chatLog.lockWait();
                out.writeObject(this.chatLog);
                this.chatLog.unlock();
            } catch (IOException ex) {
                
            }

            try {
                ChatLog chatIn = (ChatLog) this.in.readObject();
            } catch (IOException | ClassNotFoundException ex) {
            }

        }
    }
}
