
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chris on 11/14/15.
 */
public class FileServer {
    public static String file = "/home/chris/Downloads/MLP5x18.mp4";
    public FileServer(String file){
        FileServer.file = file;
    }
    public static void main(String[] args) {
        while(true){
            ServerSocket serverSocket = null;
            Socket socket = null;
            BufferedOutputStream toClient = null;
            try {
                serverSocket = new ServerSocket(3248);
                socket = serverSocket.accept();
                FileThread fileThread = new FileThread(toClient,socket,file);

            } catch (Exception e){

            }
            return;
        }
    }
    private static class FileThread extends Thread{
        BufferedOutputStream toClient;
        Socket socket;
        String file;
        public FileThread(BufferedOutputStream out, Socket socket, String file){
            this.toClient = out;
            this.socket = socket;
            this.file = file;
            this.start();
        }
        @Override
        public void run(){
            try {
                toClient = new BufferedOutputStream(socket.getOutputStream());
            }catch (Exception e){

            }
            if(toClient != null){
                File file = new File(this.file);
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
