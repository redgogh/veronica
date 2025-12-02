package org.Viakko.tools.security.cipher;

import org.Viakko.tools.security.AES;
import org.Viakko.tools.security.Codec;
import org.Viakko.tools.Rethrow;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static org.Viakko.tools.TypeCvt.atos;

/**
 * @author Ekko
 */
public class AESCipher implements AES {

    @Override
    public String encrypt(String data, String secret) {
        return encrypt(data.getBytes(), secret);
    }

    @Override
    public String encrypt(byte[] bytes, String secret) {
        return Rethrow.allow(() -> {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Codec.BASE64.encode(cipher.doFinal(bytes));
        });
    }

    @Override
    public String decrypt(String data, String secret) {
        return atos(decrypt(Codec.BASE64.decodeBytes(data), secret));
    }

    @Override
    public byte[] decrypt(byte[] bytes, String secret) {
        return Rethrow.allow(() -> {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(bytes);
        });
    }

}
