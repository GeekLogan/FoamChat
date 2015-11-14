import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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

    /**
     *
     * Modified from
     * http://crunchify.com/how-to-get-server-ip-address-and-hostname-in-java/
     *
     * @return the IP address of current system in a String
     */
    public static String getCurrentIP_old() throws UnknownHostException {
        InetAddress ip = null;
        String hostname;
        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();
        System.out.println("Your current IP address : " + ip);
        System.out.println("Your current Hostname : " + hostname);

        return ip + "";
    }

    public static List<String> getCurrentIP() throws SocketException {
        ArrayList<String> out = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while( interfaces.hasMoreElements() ) {
            NetworkInterface current = interfaces.nextElement();
            Enumeration<InetAddress> addrs = current.getInetAddresses();
            while( addrs.hasMoreElements() ) {
                InetAddress addr = addrs.nextElement();
                if( !addr.isLoopbackAddress() ) { //Filter Loopbacks
                    if( !addr.getHostAddress().contains(":") ) { //Filter IPV6
                        out.add(addr.getHostAddress());
                    }
                }
                
            }
        }
        
        return out;
    }

    public static void main(String[] args) {
        try {
            System.out.println(IPTools.getCurrentIP());
        } catch (SocketException ex) {
            Logger.getLogger(IPTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
