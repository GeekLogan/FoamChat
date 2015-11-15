import java.io.Serializable;

/**
 * Created by chris on 11/14/15.
 */
public class FoamFile implements Serializable{
    public String[] ip;
    public int port;
    public String fileName;
    public long size;

    public FoamFile(String[] ip, int port, String fileName,long size){
        this.ip = ip;
        this.port = port;
        this.fileName = fileName;
        this.size = size;
    }
    public FoamFile(){
        this.ip = null;
        this.port = 0;
        this.fileName = null;
        this.size = 0;
    }
}