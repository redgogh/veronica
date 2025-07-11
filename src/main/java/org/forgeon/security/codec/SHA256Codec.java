package org.forgeon.security.codec;

import org.forgeon.exception.IOReadException;
import org.forgeon.exception.SystemRuntimeException;
import org.forgeon.io.IOUtils;
import org.forgeon.security.Codec;
import org.forgeon.security.SHA256;
import org.forgeon.utils.Rethrow;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Red Gogh
 */
public class SHA256Codec implements SHA256 {

    @Override
    public String encode(String source) {
        return encode(source.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encode(File file) {
        return Rethrow.swallow(() -> {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                int len = 0;
                byte[] buffer = new byte[IOUtils.MB];
                while ((len = fileInputStream.read(buffer)) != IOUtils.EOF)
                    messageDigest.update(buffer, 0, len);
            } catch (Exception e) {
                throw new IOReadException(e);
            }
            return Codec.toByteHex(messageDigest.digest());
        });
    }

    @Override
    public String encode(byte[] source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(source);
            return Codec.toByteHex(messageDigest.digest());
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

}
