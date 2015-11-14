import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Handles Encryption and Decryption of Strings, key generation, and key loading
 * @author chris
 */
public class EncryptionMachine {
    public KeyPair keyPair;

    public EncryptionMachine(String fileName) throws Exception {
        if (new File(fileName + ".pub.key").exists() && new File(fileName + ".priv.key").exists()) {
            this.keyPair = generateKeyPair(fileName);
        }else{
            this.keyPair = new KeyPair(loadPublicKey(fileName + ".pub.key"), loadPrivateKey(fileName + ".priv.key"));
        }
    }


    private PublicKey loadPublicKey(String fileName) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BigInteger modulus = (BigInteger) objectInputStream.readObject();
        BigInteger exponent = (BigInteger) objectInputStream.readObject();
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus,exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(rsaPublicKeySpec);
        return publicKey;
    }

    private PrivateKey loadPrivateKey(String fileName) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BigInteger modulus = (BigInteger) objectInputStream.readObject();
        BigInteger exponent = (BigInteger) objectInputStream.readObject();
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus,exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(rsaPrivateKeySpec);
        return privateKey;
    }

    private KeyPair generateKeyPair(String filename) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        saveKeyPair(filename, pair);
        return pair;
    }

    private void saveKeyPair(String filename, KeyPair pair) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = factory.getKeySpec(pair.getPublic(),RSAPublicKeySpec.class);
        RSAPrivateKeySpec privateKeySpec = factory.getKeySpec(pair.getPrivate(),RSAPrivateKeySpec.class);
        saveKey(filename+".pub.key",publicKeySpec.getModulus(),publicKeySpec.getPublicExponent());
        saveKey(filename+".priv.key",privateKeySpec.getModulus(),privateKeySpec.getPrivateExponent());
    }
    private void saveKey(String filename, BigInteger modulus, BigInteger exponent) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(fileOutputStream));
        objectOutputStream.writeObject(modulus);
        objectOutputStream.writeObject(exponent);
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
