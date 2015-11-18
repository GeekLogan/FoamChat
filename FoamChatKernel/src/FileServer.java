import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileServer {
    public static void main(String[] args) throws Exception {
        EncryptionMachine encryptionMachine = new EncryptionMachine("testId");
        Cipher ecipher = encryptionMachine.getAESCipher();
        SecretKey key = encryptionMachine.makeKey();
        ecipher.init(Cipher.ENCRYPT_MODE,key);
        FoamFile foamFile = new FileServer().getFoamFile("/home/chris/FUC.mp3",ecipher);
        String[] ip = {"10.0.0.100"};
        foamFile.ip = ip;
        foamFile.port = 3248;
        Cipher dcipher = encryptionMachine.getAESCipher();
        dcipher.init(Cipher.DECRYPT_MODE,key);
        FileClient fileClient = new FileClient(foamFile,"/home/chris/copy.mp3",dcipher);
    }
    public FoamFile getFoamFile(String fileStr, Cipher cipher) {
        FoamFile foamFile = new FoamFile();
        File file = new File(fileStr);
        foamFile.size = file.length();
        foamFile.fileName = file.getName();
        ServerThread serverThread = new ServerThread(file, cipher);
        return foamFile;
    }

    private static class ServerThread extends Thread {

        File file;
        Cipher cipher;

        public ServerThread(File file, Cipher cipher) {
            this.file = file;
            this.cipher = cipher;
            this.start();
        }

        public void run() {
            //while (true) {
            ServerSocket serverSocket = null;
            Socket socket = null;
            BufferedOutputStream toClient = null;
            try {
                serverSocket = new ServerSocket(3248);
                socket = serverSocket.accept();
                FileThread fileThread = new FileThread(toClient, socket, file, cipher);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //}
        }
    }

    private static class FileThread extends Thread {

        BufferedOutputStream toClient;
        Socket socket;
        File file;
        Cipher cipher;

        public FileThread(BufferedOutputStream out, Socket socket, File file, Cipher cipher) {
            this.toClient = out;
            this.socket = socket;
            this.file = file;
            this.cipher = cipher;
            this.start();
        }

        @Override
        public void run() {
            try {
                toClient = new BufferedOutputStream(socket.getOutputStream());
                if (toClient != null) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    CipherOutputStream cipherOutputStream = new CipherOutputStream(toClient,cipher);
                    byte[] fileBytes = new byte[1024];
                    int i = 0;
                    while((i = fileInputStream.read(fileBytes)) > -1){
                        cipherOutputStream.write(fileBytes,0,i);
                    }
                    cipherOutputStream.close();
                    toClient.flush();
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

