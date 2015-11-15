
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;

/**
 * Created by chris on 11/14/15.
 */
public class FileClient {

    public FileClient() {
        //Do Nothing
    }

    public void saveFile(FoamFile foamFile, String out, Cipher cipher) throws InvalidKeyException {
        byte[] aByte = new byte[1];
        int bytesRead;
        Socket socket = null;
        CipherInputStream cipherInputStream = null;

        try {
            socket = new Socket(foamFile.ip[0], foamFile.port);
            cipherInputStream = new CipherInputStream(socket.getInputStream(), cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cipherInputStream != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                fos = new FileOutputStream(out);
                bufferedOutputStream = new BufferedOutputStream(fos);

                do {
                    bufferedOutputStream.write(aByte);
                    bytesRead = cipherInputStream.read(aByte);
                } while (bytesRead != -1);

                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }
}
