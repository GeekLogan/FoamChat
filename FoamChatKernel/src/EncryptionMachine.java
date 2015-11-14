import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Handles Encryption and Decryption of Strings, key generation, and key loading
 */
public class EncryptionMachine {
    public KeyPair keyPair;
    public EncryptionMachine() {
        //Do nothing
    }
    public PublicKey loadPublicKey(String fileName) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BigInteger modulus = (BigInteger) objectInputStream.readObject();
        BigInteger exponent = (BigInteger) objectInputStream.readObject();
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus,exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(rsaPublicKeySpec);
        return publicKey;
    }

    public PrivateKey loadPrivateKey(String fileName) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BigInteger modulus = (BigInteger) objectInputStream.readObject();
        BigInteger exponent = (BigInteger) objectInputStream.readObject();
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus,exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(rsaPrivateKeySpec);
        return privateKey;
    }
}
