package org.varketh.tools.security.codec;

import org.varketh.tools.security.Base64;

import static org.varketh.tools.TypeCvt.atos;

/**
 * @author Varketh Nockrath
 */
public class Base64Codec implements Base64 {
    @Override
    public String encode(String source) {
        return encode(source.getBytes());
    }

    @Override
    public String encode(byte[] b) {
        return java.util.Base64.getUrlEncoder().encodeToString(b);
    }

    @Override
    public String decode(String src) {
        return atos(decodeBytes(src));
    }

    @Override
    public byte[] decodeBytes(String src) {
        try {
            return java.util.Base64.getDecoder().decode(src);
        } catch (IllegalArgumentException e) {
            return java.util.Base64.getUrlDecoder().decode(src);
        }
    }

}
