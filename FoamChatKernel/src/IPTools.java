
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 *
 */
public class IPTools {

    /**
     *
     * Modified from
     * http://crunchify.com/how-to-get-server-ip-address-and-hostname-in-java/
     *
     * @return the IP address of current system in a String
     */
    public static String getHostname_BAD() {
        InetAddress ip = null;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (ip == null) {
            return null;
        }
        return "" + ip;
    }

    public static String getIP() {
        NetworkInterface.getNetworkInterfaces();
        NetworkInterface.get
    }

}
