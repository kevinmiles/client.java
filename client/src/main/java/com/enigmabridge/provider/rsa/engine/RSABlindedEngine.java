package com.enigmabridge.provider.rsa.engine;

import com.enigmabridge.provider.parameters.EBRSAKeyParameter;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * this does your basic RSA algorithm with blinding
 */
public class RSABlindedEngine
    implements AsymmetricBlockCipher
{
    private static final BigInteger ONE = BigInteger.valueOf(1);

    private RSACoreEngine    core = new RSACoreEngine();
    private EBRSAKeyParameter key;
    private SecureRandom     random;

    /**
     * initialise the RSA engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param the necessary RSA key parameters.
     */
    public void init(
        boolean             forEncryption,
        CipherParameters    param)
    {
        core.init(forEncryption, param);
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom rParam = (ParametersWithRandom) param;
            random = rParam.getRandom();
            param = rParam.getParameters();
        } else {
            random = new SecureRandom();
        }

        key = (EBRSAKeyParameter)param;
    }

    /**
     * Return the maximum size for an input block to this engine.
     * For RSA this is always one byte less than the key size on
     * encryption, and the same length as the key size on decryption.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize()
    {
        return core.getInputBlockSize();
    }

    /**
     * Return the maximum size for an output block to this engine.
     * For RSA this is always one byte less than the key size on
     * decryption, and the same length as the key size on encryption.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize()
    {
        return core.getOutputBlockSize();
    }

    /**
     * Process a single block using the basic RSA algorithm.
     *
     * @param in the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the RSA process.
     * @exception DataLengthException the input block is too large.
     */
    public byte[] processBlock(
        byte[]  in,
        int     inOff,
        int     inLen)
    {
        if (key == null)
        {
            throw new IllegalStateException("RSA engine not initialised");
        }

        BigInteger input = core.convertInput(in, inOff, inLen);
        BigInteger result;

        if (key.canBlind()){
            BigInteger e = key.getPublicExponent();
            BigInteger m = key.getModulus();
            BigInteger r = BigIntegers.createRandomInRange(ONE, m.subtract(ONE), random);

            BigInteger blindedInput = r.modPow(e, m).multiply(input).mod(m);
            BigInteger blindedResult = core.processBlock(blindedInput);

            BigInteger rInv = r.modInverse(m);
            result = blindedResult.multiply(rInv).mod(m);
            // defence against Arjen Lenstra’s CRT attack
            if (!input.equals(result.modPow(e, m)))
            {
                throw new IllegalStateException("RSA engine faulty decryption/signing detected");
            }
        } else {
            result = core.processBlock(input);
        }

        return core.convertOutput(result);
    }
}
