package sirs.grupo7.securepayment.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AESFileEncryption {

    private String salt;
    private String iv;
    private String filename;

    public AESFileEncryption() {
        this.filename = "encryptedfile.des";
        this.salt = "salt.enc";
        this.iv = "iv.enc";
    }

    public AESFileEncryption(String filename) {
        this.filename = filename;
        this.salt = "salt.enc";
        this.iv = "iv.enc";
    }

    public String getFileName() {
        return this.filename;
    }

    public String getSaltFileName() {
        return this.salt;
    }

    public String getIvFileName() {
        return this.iv;
    }

    public void encrypt(String code, String value) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException {
        // encrypted file
        FileOutputStream outFile = new FileOutputStream(this.filename);

        // salt is used for encoding
        // writing it to a file
        // salt should be transferred to the recipient securely
        // for decryption
        byte[] salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        FileOutputStream saltOutFile = new FileOutputStream(this.salt);
        saltOutFile.write(salt);
        saltOutFile.close();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(code.toCharArray(), salt, 65536, 256);
        SecretKey secretKey = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        //
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();

        // iv adds randomness to the text and just makes the mechanism more
        // secure
        // used while initializing the cipher
        // file to store the iv
        FileOutputStream ivOutFile = new FileOutputStream(this.iv);
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        ivOutFile.write(iv);
        ivOutFile.close();

        // Key encryption
        byte[] output = cipher.doFinal(value.getBytes());
        if (output != null)
            outFile.write(output);

        outFile.flush();
        outFile.close();
    }

    public String decrypt(String code) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        try {

            // reading the salt
            // user should have secure mechanism to transfer the
            // salt, iv and password to the recipient
            FileInputStream saltFis = new FileInputStream(this.salt);
            byte[] salt = new byte[8];
            saltFis.read(salt);
            saltFis.close();

            // reading the iv
            FileInputStream ivFis = new FileInputStream(this.iv);
            byte[] iv = new byte[16];
            ivFis.read(iv);
            ivFis.close();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(code.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // file decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

            File inputFile = new File(this.filename);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            fis.read(inputBytes);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            fis.close();

            String key = new String(outputBytes);

            return key;
        } catch (BadPaddingException e) {
            return read();
        }
    }

    public String read() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.filename));
        String sCurrentLine = br.readLine();
        br.close();
        return sCurrentLine;
    }
}
