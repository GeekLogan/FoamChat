import java.io.Serializable;

/**
 * Created by chris on 11/14/15.
 */
public class FoamFile implements Serializable{
    public String ip;
    public int port;
    public String fileName;
    public String aesKey;
    public int size;

    public FoamFile(String ip, int port, String fileName, String aesKey,int size){
        this.ip = ip;
        this.port = port;
        this.fileName = fileName;
        this.aesKey = aesKey;
        this.size = size;
    }
}