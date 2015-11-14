
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server Thread
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class FoamChatServer extends Thread {

    public final int port = 1010;
    public boolean running;

    private List<NodeReference> nodes;
    private List<NodeReference> homeNodes;

    private ServerSocket serverSocket;

    public FoamChatServer(List<NodeReference> nodes_in) {
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
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } catch (IOException ex) {
                System.err.println("Can not get input connection!");
                System.exit(402);
            }
        }
    }
}
