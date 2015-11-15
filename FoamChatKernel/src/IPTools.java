
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class IPTools {

    public static List<String> getCurrentIP() throws SocketException {
        ArrayList<String> out = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface current = interfaces.nextElement();
            Enumeration<InetAddress> addrs = current.getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress addr = addrs.nextElement();
                if (!addr.isLoopbackAddress()) { //Filter Loopbacks
                    if (!addr.getHostAddress().contains(":")) { //Filter IPV6
                        out.add(addr.getHostAddress());
                    }
                }

            }
        }

        return out;
    }

    /**
     *
     * MAIN method: Runs simple sanity check of the currently implemented
     * methods
     *
     * @param args -> arguments from cmd line
     */
    public static void main(String[] args) {
        try {
            System.out.println("IPs: " + IPTools.getCurrentIP());
        } catch (SocketException ex) {
            Logger.getLogger(IPTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static String[] getHomeNodes() {
        List<String> out = new ArrayList<>();

        try {
            for (String ip : IPTools.getCurrentIP()) {
                out.add(ip);
            }
        } catch (SocketException ex) {
            System.err.println("No IP Interfaces Found!");
            System.exit(404);
        }

        String[] out2 = new String[out.size()];
        for (int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }

        return out2;
    }
}
