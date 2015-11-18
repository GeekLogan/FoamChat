import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.security.InvalidKeyException;

/**
 * Created by chris on 11/14/15.
 */
public class FileClient {

    public FileClient(FoamFile foamFile, String out, Cipher cipher) throws InvalidKeyException {
        this.saveFile(foamFile,out,cipher);
    }

    public void saveFile(FoamFile foamFile, String out, Cipher cipher) throws InvalidKeyException {
        Socket socket = null;
        InputStream inputStream = null;

        try {
            socket = new Socket(foamFile.ip[0], foamFile.port);
            inputStream = socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inputStream != null) {
            FileOutputStream fos = null;
            try {
                CipherInputStream cipherInputStream = new CipherInputStream(inputStream,cipher);
                fos = new FileOutputStream(out);
                byte[] fileBytes = new byte[1024];
                int i = 0;
                while((i = cipherInputStream.read(fileBytes)) > -1){
                    fos.write(fileBytes,0,i);
                }
                fos.flush();
                fos.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String[] ip = {"127.0.0.1"};
        FoamFile file = new FoamFile(ip,3248,"copy.mp3",100);

    }
}
