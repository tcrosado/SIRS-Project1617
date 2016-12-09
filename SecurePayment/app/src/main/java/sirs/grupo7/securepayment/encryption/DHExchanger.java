package sirs.grupo7.securepayment.encryption;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class DHExchanger {

    private DHParameterSpec spec;
    private int PORT = 5000;
    String HOSTNAME;
    byte[] sessionKey;
    private KeyPair kp;

    public DHExchanger(String hostname) {
        this.spec = null;
        this.HOSTNAME = hostname;
    }

    public DHExchanger(byte[] receivedKey) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, IOException {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedKey);
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        PublicKey clientKey = bobKeyFac.generatePublic(x509KeySpec);
        this.spec = ((DHPublicKey)clientKey).getParams();
        this.kp = this.generateKeyPair();
        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(kp.getPrivate());
        keyAgree.doPhase(clientKey, true);
        this.sessionKey = keyAgree.generateSecret();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kp = KeyPairGenerator.getInstance("DH");
        kp.initialize(this.spec);
        return kp.generateKeyPair();
    }

    public byte[] getKey() {
        return this.sessionKey;
    }

    public byte[] getKeyPair() {
        return this.kp.getPublic().getEncoded();
    }
}
