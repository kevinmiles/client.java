package com.enigmabridge.provider.rsa;

import java.security.interfaces.RSAPublicKey;

/**
 * RSA public key in EB.
 * Conforms to general Java Key interface.
 *
 * Inspiration: bcprov-jdk15on-1.54-sources.jar!/org/bouncycastle/jcajce/provider/asymmetric/rsa/BCRSAPublicKey.java
 * Created by dusanklinec on 26.04.16.
 */
public class EBRSAPublicKey extends EBRSAKey implements RSAPublicKey {
    static final long serialVersionUID = 1;

    public static class Builder extends EBRSAKey.AbstractBuilder<EBRSAPublicKey, Builder> {
        private final EBRSAPublicKey parent = new EBRSAPublicKey();

        @Override
        public Builder getThisBuilder() {
            return this;
        }

        @Override
        public EBRSAPublicKey getObj() {
            return parent;
        }

        @Override
        public EBRSAPublicKey build() {
            return parent;
        }
    }
}