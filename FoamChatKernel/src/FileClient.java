import java.io.*;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileClient {
    private static final String ip = "127.0.0.1";
    private static final int port = 3248;
    private static final String out = "/home/chris/dev/tmp.mkv";

    public static void main(String[] args){
        byte[] aByte = new byte[1];
        int bytesRead;

        Socket socket = null;
        InputStream inputStream = null;

        try {
            socket = new Socket( ip , port );
            inputStream = socket.getInputStream();
        } catch (IOException ex) {

        }


        if (inputStream != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                fos = new FileOutputStream( out );
                bufferedOutputStream = new BufferedOutputStream(fos);

                do {
                    bufferedOutputStream.write(aByte);
                    bytesRead = inputStream.read(aByte);
                } while (bytesRead != -1);

                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
