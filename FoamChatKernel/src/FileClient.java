import java.io.*;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileClient {
    private static final String ip = "25.16.95.241";
    private static final int port = 3248;
    private static final String out = "<Output Path>";

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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (inputStream != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                fos = new FileOutputStream( out );
                bufferedOutputStream = new BufferedOutputStream(fos);
                bytesRead = inputStream.read(aByte, 0, aByte.length);

                do {
                    byteArrayOutputStream.write(aByte);
                    bytesRead = inputStream.read(aByte);
                } while (bytesRead != -1);

                bufferedOutputStream.write(byteArrayOutputStream.toByteArray());
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                socket.close();
            } catch (Exception ex) {

            }
        }
    }
}
