
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileServer {
    public File file;
    public FileServer(String file){
        this.file = new File(file);
    }
    public static void main(String[] args) {
        while(true){
            ServerSocket serverSocket = null;
            Socket socket = null;
            BufferedOutputStream toClient = null;
            try {
                serverSocket = new ServerSocket(3248);
                socket = serverSocket.accept();
                FileThread fileThread = new FileThread(toClient,socket);

            } catch (Exception e){

            }
            return;
        }
    }
    private static class FileThread extends Thread{
        BufferedOutputStream toClient;
        Socket socket;
        public FileThread(BufferedOutputStream out, Socket socket){
            this.toClient = out;
            this.socket = socket;
            this.start();
        }
        @Override
        public void run(){
            try {
                toClient = new BufferedOutputStream(socket.getOutputStream());
            }catch (Exception e){

            }
            if(toClient != null){
                File file = new File("/home/chris/Downloads/OP1.mkv");
                byte[] fileBytes = new byte[(int)file.length()];
                FileInputStream fileInputStream = null;
                try{
                    fileInputStream = new FileInputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                try{
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
