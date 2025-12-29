package org.viakko.tools.jwtc;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.viakko.tools.collection.Maps;
import org.viakko.tools.exception.UncheckedException;
import org.viakko.tools.security.Codec;
import org.viakko.tools.Assert;
import org.viakko.tools.Rethrow;
import org.viakko.tools.Comparators;

import java.security.PrivateKey;

import static com.nimbusds.jose.JWSAlgorithm.*;
import static org.viakko.tools.generator.Generator.uuid;
import static org.viakko.tools.TypeCvt.atob;

/**
 * `JWTGrantor` 是一个用于生成和验证 JSON Web Token (JWT) 的类。
 *
 * <p>该类提供了生成 JWT 的功能，支持多种加密算法（如 HS256 和 RS256）。可以根据需要指定 JWT 的有效期、主题（subject）、受众（audience）、签发者（issuer）等信息。
 * 它还提供了 JWT 签名和验证的方法，能够确保 JWT 的完整性和有效性。
 *
 * <p>此外，`JWTGrantor` 还支持自定义加密算法和密钥管理，能够通过提供密钥或密钥对来签署和验证 JWT。
 * 该类是构建和验证 JWT 的核心工具，适用于需要处理身份认证和授权的应用程序。<p>
 *
 * <h2>使用示例</h2>
 * <pre>
 *     JWTGrantor grantor = new JWTGrantor("secret")
 *                            .subject("user123")
 *                            .issuer("myApp")
 *                            .audience("myAudience");
 *     String token = grantor.signWith(2, TimeUnit.HOURS);
 *     boolean isValid = grantor.verify(token);
 * </pre>
 *
 * @author Ekko
 * @since 1.0
 */
public class JWTSigner {

    /**
     * `algorithm` 表示用于签名和验证的 JWS 算法（如 `HS256`, `RS256` 等）。
     * 它指定了加密或签名的算法类型。
     */
    private final JWSAlgorithm algorithm;

    /**
     * `encryptKey` 是用于加密的密钥对象。
     * 它通常用于加密数据或消息，以确保数据的安全性和机密性。
     */
    private final Object encryptKey;

    /**
     * `decryptKey` 是用于解密的密钥对象。
     * 它用于解密通过 `encryptKey` 加密的数据，恢复原始信息。
     */
    private final Object decryptKey;

    /**
     * 使用对称密钥（如 HMAC）初始化 JWTGrantor
     *
     * <p>该构造函数使用相同的密钥作为加密密钥和解密密钥，适用于对称加密算法（如 HS256）。
     * 默认使用 HS256 算法。
     *
     * @param secret 用于加密和解密的对称密钥
     */
    public JWTSigner(Object secret) {
        this.encryptKey = secret;
        this.decryptKey = secret;
        this.algorithm = JWSAlgorithm.parse("HS256");
    }

    /**
     * 使用非对称密钥（公钥和私钥）初始化 JWTGrantor
     *
     * <p>该构造函数使用公钥和私钥初始化 JWTGrantor，适用于非对称加密算法（如 RS256）。私钥用于加密，公钥用于解密。
     * 默认使用 RS256 算法。
     *
     * @param publicKey  用于解密的公钥
     * @param privateKey 用于加密的私钥
     */
    public JWTSigner(Object publicKey, Object privateKey) {
        this.encryptKey = privateKey;
        this.decryptKey = publicKey;
        this.algorithm = JWSAlgorithm.parse("RS256");
    }

    /**
     * 使用指定的加密算法和有效期生成签名的 JWT 字符串
     *
     * <p>该方法使用指定的加密算法和有效期生成签名的 JWT 字符串。可以通过传入时间单位来调整有效期单位。
     * 还可以传入自定义的 JWT 声明。
     *
     * @param claims 自定义的 JWT 声明
     * @return 签名后的 JWT 字符串
     */
    public String signWith(JWTClaims claims) {
        Assert.notNull(claims, "签发 Token 必须要有 JWTClaims 对象");

        if (!claims.containsKey("exp"))
            throw new IllegalArgumentException("Token 签发失败 Claims 必须包含过期时间");

        return Rethrow.allow(() -> {
            JWSSigner signer = newSigner(encryptKey, algorithm);

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

            builder.claim("iat", System.currentTimeMillis());
            builder.claim("jti", Codec.BASE64.encode(uuid()));

            claims.forEach(builder::claim);

            JWTClaimsSet claimsSet = builder.build();

            JWSHeader header = JWSHeader.parse(Maps.of(
                    "alg", algorithm.getName(),
                    "typ", "JWT"
            ));

            SignedJWT signed = new SignedJWT(header, claimsSet);

            signed.sign(signer);

            return signed.serialize();
        });
    }

    /**
     * 验证 JWT 的签名和有效性
     *
     * <p>该方法验证给定的 JWT 字符串的签名，并检查其是否过期。如果验证失败或 JWT 已过期，返回 `false`。
     *
     * @param token JWT 字符串
     * @return 如果验证成功且未过期，返回 `true`；否则返回 `false`
     * @throws UncheckedException 如果解析或验证过程出现错误
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean verify(String token) {
        try {
            SignedJWT signed = SignedJWT.parse(token);
            JWSVerifier jwsVerifier = newVerifier(decryptKey, algorithm);

            if (!signed.verify(jwsVerifier))
                return false;

            JWTClaims claims = parseClaims(token);

            return !claims.isExpiration();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * 解析 JWT 字符串中的声明部分并返回 JWTClaims 对象
     *
     * <p>该方法从 JWT 字符串中提取声明部分（即 `payload`），并将其解析为一个 `JWTClaims` 对象。
     * 该过程包括对 base64 编码的声明部分进行解码，并将解码结果转化为 JSON 对象，最终封装成 `JWTClaims`。
     *
     * @param token JWT 字符串，包含头部、声明部分和签名
     * @return 解析得到的 `JWTClaims` 对象，包含 JWT 的声明信息
     */
    public static JWTClaims parseClaims(String token) {
        return Rethrow.allow(() -> {
            String[] payloadPart = token.split("\\.");
            JSONObject Result = (JSONObject) JSONObject.parse(Codec.BASE64.decode(payloadPart[1]));
            JWTClaims claims = new JWTClaims();
            claims.putAll(Result);
            return claims;
        });
    }

    /**
     * 创建一个新的签名器（JWSSigner）
     *
     * <p>根据给定的密钥和加密算法，创建适当的签名器。如果使用 HMAC 算法（如 HS256），则返回 MACSigner；
     * 如果使用 RSA 算法（如 RS256、RS384、RS512），则返回 RSASSASigner。如果算法无效或不支持，抛出异常。
     *
     * @param key       用于签名的密钥
     * @param algorithm 使用的 JWS 加密算法
     * @return 返回适配的 JWSSigner 对象
     * @throws KeyLengthException       如果密钥长度不符合要求
     * @throws IllegalArgumentException 如果加密算法无效或不支持
     */
    private static JWSSigner newSigner(Object key, JWSAlgorithm algorithm) throws KeyLengthException {
        if (Comparators.checkin(algorithm, HS256))
            return new MACSigner(atob(key));

        if (Comparators.checkin(algorithm, RS256, RS384, RS512))
            return new RSASSASigner((PrivateKey) key);

        throw new IllegalArgumentException("无效或不支持的加密算法 " + algorithm.getName());
    }

    /**
     * 创建一个新的验证器（JWSVerifier）
     *
     * <p>根据给定的密钥和加密算法，创建适当的验证器。如果使用 HMAC 算法（如 HS256），则返回 MACVerifier；
     * 如果使用 RSA 算法（如 RS256、RS384、RS512），则返回 RSASSAVerifier。如果算法无效或不支持，抛出异常。
     *
     * @param key       用于验证的密钥
     * @param algorithm 使用的 JWS 加密算法
     * @return 返回适配的 JWSVerifier 对象
     * @throws JOSEException            如果创建验证器时出现错误
     * @throws IllegalArgumentException 如果加密算法无效或不支持
     */
    private static JWSVerifier newVerifier(Object key, JWSAlgorithm algorithm) throws JOSEException {
        if (Comparators.checkin(algorithm, HS256))
            return new MACVerifier(atob(key));

        if (Comparators.checkin(algorithm, RS256, RS384, RS512))
            return new RSASSAVerifier((java.security.interfaces.RSAPublicKey) key);

        throw new IllegalArgumentException("无效或不支持的加密算法 " + algorithm.getName());
    }

}
