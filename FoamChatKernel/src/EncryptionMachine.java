import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Handles Encryption and Decryption of Strings, key generation, and key loading
 * @author Chris Midkiff
 */
public class EncryptionMachine {
    public KeyPair keyPair;
    private Cipher keyCipher,aesCipher;
    private String ext = ".key";
    private String pubExt = ".pub";
    private String privExt = ".priv";
    public  static String filename;

    public EncryptionMachine(String fileName) throws Exception {
        this.keyCipher = Cipher.getInstance("RSA");
        this.aesCipher = Cipher.getInstance("AES");
        EncryptionMachine.filename = fileName;
        if (!(new File(fileName + pubExt + ext).exists() && new File(fileName + privExt + ext).exists())) {
            this.keyPair = generateKeyPair(fileName);
        }else{
            this.keyPair = new KeyPair(loadPublicKey(fileName + pubExt + ext), loadPrivateKey(fileName + privExt + ext));
        }
    }

    public SecretKey makeKey() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //keyGenerator.init(256);
        return keyGenerator.generateKey();
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
        saveKey(filename+ pubExt + ext,publicKeySpec.getModulus(),publicKeySpec.getPublicExponent());
        saveKey(filename+ privExt + ext,privateKeySpec.getModulus(),privateKeySpec.getPrivateExponent());
    }

    private void saveKey(String filename, BigInteger modulus, BigInteger exponent) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(fileOutputStream));
        objectOutputStream.writeObject(modulus);
        objectOutputStream.writeObject(exponent);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public String encryptKey(byte[] input, PublicKey publicKey) throws Exception {
        this.keyCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return DatatypeConverter.printBase64Binary(this.keyCipher.doFinal(input));
    }

    public SecretKey decryptKey(String input) throws Exception {
        keyCipher.init(Cipher.DECRYPT_MODE,this.keyPair.getPrivate());
        byte[] raw = DatatypeConverter.parseBase64Binary(input);
        byte[] decryptedKey = this.keyCipher.doFinal(raw);
        SecretKey key = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"AES");
        return key;
    }

    public String[] encrypt(String input,PublicKey publicKey) throws Exception {
        String[] data = new String[2];
        SecretKey aesKey = this.makeKey();
        data[0] = encryptKey(aesKey.getEncoded(),publicKey);
        this.aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        input = DatatypeConverter.printBase64Binary(input.getBytes(StandardCharsets.UTF_8));
        byte[] raw = DatatypeConverter.parseBase64Binary(input);
        data[1] = DatatypeConverter.printBase64Binary(this.aesCipher.doFinal(raw));
        return data;
    }


    public String decrypt(String input, String keyStr) throws Exception {
        SecretKey key = decryptKey(keyStr);
        aesCipher.init(Cipher.DECRYPT_MODE,key);
        byte[] raw = DatatypeConverter.parseBase64Binary(input);
        return new String(this.aesCipher.doFinal(raw));
    }


    public static void main(String[] args) throws Exception {
        String test = "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";
        System.out.println("Expected: " + test);
        EncryptionMachine encryptionMachine = new EncryptionMachine("testId");
        String[] encrypted = encryptionMachine.encrypt(test,encryptionMachine.keyPair.getPublic());
        String decrypted = encryptionMachine.decrypt(encrypted[1],encrypted[0]);
        System.out.println("Decrypted: " + decrypted);
        if(test.contentEquals(decrypted)){
            System.out.println("Hooray");
        }else{
            System.out.println("Fuck");
        }
    }
}
