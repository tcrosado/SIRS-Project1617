package sirs.grupo7.securepayment.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class DHExchanger {

    public DHExchanger(){
        this.spec = null;
        this.kp = null;
        this.sessionKey = null;
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kp = KeyPairGenerator.getInstance("DH");
        kp.initialize(this.spec);
        return kp.generateKeyPair();
    }

}
