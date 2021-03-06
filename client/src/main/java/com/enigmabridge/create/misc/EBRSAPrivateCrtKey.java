package com.enigmabridge.create.misc;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Random;

/**
 * Computing RSAPrivateKeyCrt from the normal private key.
 *
 * Created by dusanklinec on 13.07.16.
 */
public class EBRSAPrivateCrtKey implements RSAPrivateCrtKey {
    protected final RSAPrivateKey key;

    protected final BigInteger e;
    protected final BigInteger d;
    protected final BigInteger n;

    protected BigInteger p;
    protected BigInteger q;
    protected BigInteger dp;
    protected BigInteger dq;
    protected BigInteger qInv;

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger ZERO = BigInteger.ZERO;

    public EBRSAPrivateCrtKey(RSAPrivateKey key, BigInteger e) {
        this.key = key;
        this.e = e;
        this.d = key.getPrivateExponent();
        this.n = key.getModulus();
        init();
    }

    public EBRSAPrivateCrtKey(RSAPrivateKeySpec key, BigInteger e) {
        this.key = null;
        this.e = e;
        this.d = key.getPrivateExponent();
        this.n = key.getModulus();
        init();
    }

    public EBRSAPrivateCrtKey(BigInteger e, BigInteger d, BigInteger n) {
        this.key = null;
        this.e = e;
        this.d = d;
        this.n = n;
        init();
    }

    private void init(){
        computePq();

        final BigInteger d = key.getPrivateExponent();
        dp = d.mod(p.subtract(ONE));
        dq = d.mod(q.subtract(ONE));
        qInv = q.modInverse(p);
    }

    private void computePq(){
        final BigInteger d = key.getPrivateExponent();
        final BigInteger n = key.getModulus();

        // Step 1: Let k = de – 1. If k is odd, then go to Step 4
        BigInteger k = d.multiply(e).subtract(ONE);
        if (isEven(k)) {
            // Step 2 (express k as (2^t)r, where r is the largest odd integer
            // dividing k and t >= 1)
            BigInteger r = k;
            BigInteger t = ZERO;

            do {
                r = r.divide(TWO);
                t = t.add(ONE);
            } while (isEven(r));

            // Step 3
            Random random = new Random();
            boolean success = false;
            BigInteger y = null;

            step3loop:
            for (int i = 1; i <= 100; i++) {

                // 3a
                BigInteger g = getRandomBi(n, random);

                // 3b
                y = g.modPow(r, n);

                // 3c
                if (y.equals(ONE) || y.equals(n.subtract(ONE))) {
                    // 3g
                    continue step3loop;
                }

                // 3d
                for (BigInteger j = ONE; j.compareTo(t) <= 0; j = j.add(ONE)) {
                    // 3d1
                    BigInteger x = y.modPow(TWO, n);

                    // 3d2
                    if (x.equals(ONE)) {
                        success = true;
                        break step3loop;
                    }

                    // 3d3
                    if (x.equals(n.subtract(ONE))) {
                        // 3g
                        continue step3loop;
                    }

                    // 3d4
                    y = x;
                }

                // 3e
                BigInteger x = y.modPow(TWO, n);
                if (x.equals(ONE)) {
                    success = true;
                    break step3loop;
                }

                // 3g
                // (loop again)
            }

            if (success) {
                // Step 5
                p = y.subtract(ONE).gcd(n);
                q = n.divide(p);
                return;
            }
        }

        // Step 4
        throw new RuntimeException("Prime factors not found");
    }

    private static boolean isEven(BigInteger bi) {
        return bi.mod(TWO).equals(BigInteger.ZERO);
    }

    private static BigInteger getRandomBi(BigInteger n, Random rnd) {
        // From http://stackoverflow.com/a/2290089
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), rnd);
        } while (r.compareTo(n) >= 0);
        return r;
    }

    @Override
    public BigInteger getPublicExponent() {
        return e;
    }

    @Override
    public BigInteger getPrimeP() {
        return p;
    }

    @Override
    public BigInteger getPrimeQ() {
        return q;
    }

    @Override
    public BigInteger getPrimeExponentP() {
        return dp;
    }

    @Override
    public BigInteger getPrimeExponentQ() {
        return dq;
    }

    @Override
    public BigInteger getCrtCoefficient() {
        return qInv;
    }

    @Override
    public BigInteger getPrivateExponent() {
        return d;
    }

    @Override
    public String getAlgorithm() {
        return key != null ? key.getAlgorithm() : "RSA";
    }

    @Override
    public String getFormat() {
        return key != null ? key.getFormat() : null;
    }

    @Override
    public byte[] getEncoded() {
        return key != null ? key.getEncoded() : null;
    }

    @Override
    public BigInteger getModulus() {
        return n;
    }
}
