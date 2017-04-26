package sirs.grupo7.securepayment.encryption;

import android.content.Context;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// TO SAVE FILES ON ANDROID
// https://developer.android.com/guide/topics/data/data-storage.html#filesInternal

public class AESFileEncryption {

    private Context context;

    public AESFileEncryption(Context context) {
        this.context = context;
    }

    public byte[] encrypt(byte[] code, byte[] value, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidParameterSpecException, InvalidKeyException, InvalidKeySpecException {
        return encrypt_decrypt(code, value, Cipher.ENCRYPT_MODE, iv);
    }

    public byte[] decrypt(byte[] code, byte[] value, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidParameterSpecException, InvalidKeyException, InvalidKeySpecException {
        return encrypt_decrypt(code, value, Cipher.DECRYPT_MODE, iv);
    }

    private byte[] encrypt_decrypt(byte[] code, byte[] value, int mode, byte[] iv) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException {
        byte[] key = makeHash(code);
        System.out.println("THE REAL KEY = " + Arrays.toString(key));
        SecretKey secret = new SecretKeySpec(key, "AES");
        //byte [] iv = Base64.decode(read(ReadWriteInfo.IV).getBytes(), Base64.NO_WRAP);
        System.out.println("IV = " + Arrays.toString(iv));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        try {
            cipher.init(mode, secret, new IvParameterSpec(iv));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipher.doFinal(value);
    }

    private byte[] makeHash(byte[] code) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(code);
    }

    private String read(String filename) {
        try {
            String message;
            FileInputStream fileInputStream = this.context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public byte[] getRandomIV() throws NoSuchAlgorithmException, IOException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[15];
        byte[] init = {1};
        random.nextBytes(iv);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        b.write(init);
        b.write(iv);
        return b.toByteArray();
    }

    /*
    public static String encrytData(String text) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] static_key = Constants.AES_KEY.getBytes();

        SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV_VECTOR);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes());

        String result = Base64.encodeToString(results, Base64.NO_WRAP|Base64.DEFAULT);
        return result;

    }


    public static String decryptData(String text)throws Exception{

        byte[] encryted_bytes = Base64.decode(text, Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] static_key = Constants.AES_KEY.getBytes();

        SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV_VECTOR);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decrypted = cipher.doFinal(encryted_bytes);
        String result = new String(decrypted);

        return result;
    }

    */
}
