
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileServer {
    public FoamFile getFoamFile(String fileStr, Cipher cipher) {
        FoamFile foamFile = new FoamFile();
        File file = new File(fileStr);
        foamFile.size = file.length();
        foamFile.fileName = file.getName();
        ServerThread serverThread = new ServerThread(file,cipher);
        return foamFile;
    }
    private static class ServerThread extends Thread {
        File file;
        Cipher cipher;
        public ServerThread(File file, Cipher cipher){
            this.file = file;
            this.cipher = cipher;
            this.start();
        }
        public void run() {
            while (true) {
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
            }
        }
    }
    private static class FileThread extends Thread{
        BufferedOutputStream toClient;
        Socket socket;
        File file;
        Cipher cipher;
        public FileThread(BufferedOutputStream out, Socket socket, File file, Cipher cipher){
            this.toClient = out;
            this.socket = socket;
            this.file = file;
            this.cipher = cipher;
            this.start();
        }
        @Override
        public void run(){
            try {
                CipherOutputStream cipherOutputStream = new CipherOutputStream(socket.getOutputStream(),cipher);
                toClient = new BufferedOutputStream(cipherOutputStream);
            }catch (Exception e){

            }

            if(toClient != null){
                byte[] fileBytes = new byte[(int)file.length()];
                FileInputStream fileInputStream = null;
                try{
                    fileInputStream = new FileInputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                try {
                    bufferedInputStream.read(fileBytes,0,fileBytes.length);
                    toClient.write(fileBytes,0,fileBytes.length);
                    toClient.flush();
                    toClient.close();
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
