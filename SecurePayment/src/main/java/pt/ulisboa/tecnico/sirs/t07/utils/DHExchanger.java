package pt.ulisboa.tecnico.sirs.t07.utils;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

/**
 * Created by trosado on 28/11/16.
 */
public class DHExchanger {

    private static final  String HOSTNAME = "localhost";
    private static final Integer PORT = 5000;

    private DHParameterSpec spec;

    private byte[] sessionKey;

    public DHExchanger(Optional<DHParameterSpec> spec)
            throws NoSuchAlgorithmException, InvalidParameterSpecException, IOException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException {
        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
        paramGen.init(512);

        AlgorithmParameters params =  paramGen.generateParameters();
        this.spec = spec.orElse(params.getParameterSpec(DHParameterSpec.class));
        KeyPair kp = this.generateKeyPair();


        UDPConnection conn = new UDPConnection();
        conn.sendData(kp.getPublic().getEncoded(),HOSTNAME,PORT);

        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(kp.getPrivate());

        byte[] message = conn.receiveData().getData(); //FIXME
        KeyFactory keyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(message);
        PublicKey clientKey = keyFac.generatePublic(x509KeySpec);
        keyAgree.doPhase(clientKey, true);

        this.sessionKey = keyAgree.generateSecret();

        conn.closeConnection();
    }

    public DHExchanger(byte[] receivedKey)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidKeySpecException, IOException {

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedKey);
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        PublicKey clientKey = bobKeyFac.generatePublic(x509KeySpec);

        this.spec = ((DHPublicKey)clientKey).getParams();
        KeyPair kp = this.generateKeyPair();

        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(kp.getPrivate());
        keyAgree.doPhase(clientKey, true);

        this.sessionKey = keyAgree.generateSecret();

        UDPConnection conn =  new UDPConnection();
        conn.sendData(kp.getPublic().getEncoded(),HOSTNAME,PORT);
        conn.closeConnection();
    }


    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kp = KeyPairGenerator.getInstance("DH");
        kp.initialize(this.spec);
        return kp.generateKeyPair();
    }

    public byte[] getSessionKey(){
        return this.sessionKey;
    }

}
