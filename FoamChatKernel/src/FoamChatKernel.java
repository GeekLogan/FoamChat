
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class FoamChatKernel {

    List<NodeReference> nodes;
    FoamChatServer server;

    public FoamChatKernel() {

        nodes = new ArrayList<>();
        try {
            for (String ip : IPTools.getCurrentIP()) {
                nodes.add(new NodeReference(ip));
            }
        } catch (SocketException ex) {
            System.err.println("No IP Interfaces Found!");
            System.exit(404);
        }

        if (nodes.isEmpty()) {
            System.err.println("No IP Interfaces Found!");
            System.exit(404);
        }
        
        server = new FoamChatServer( nodes );

    }

    public static void main(String[] args) {
        FoamChatKernel kernel = new FoamChatKernel();
    }
}
