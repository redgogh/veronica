package org.redgogh.coreutils.jwtc;

import org.redgogh.coreutils.iface.TypeMapper;
import org.redgogh.coreutils.time.Chrono;
import org.redgogh.coreutils.TypeCvt;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.redgogh.coreutils.TypeCvt.atol;
import static org.redgogh.coreutils.TypeCvt.atos;
import static org.redgogh.coreutils.string.StringUtils.strlen;

/**
 * `JWTClaims` 是一个扩展自 {@link HashMap} 的类，表示 JWT 中的声明部分。
 *
 * <p>该类用于存储 JWT 的所有声明（如 subject、issuer、expiration 等），每个声明都是一个键值对。
 * 它继承自 {@link HashMap}，因此可以方便地进行键值对操作。
 *
 * <p>该类提供了与 JWT 声明相关的额外方法，可以灵活地获取或修改 JWT 声明。
 * 它的主要目的是将 JWT 中的声明封装为一个可操作的集合，便于处理 JWT 数据。<p>
 *
 * <h2>使用示例</h2>
 * <pre>
 *     JWTClaims claims = new JWTClaims();
 *     claims.put("sub", "user123");
 *     claims.put("iss", "myApp");
 *     String subject = (String) claims.get("sub");
 * </pre>
 *
 * @author Red Gogh
 * @since 1.0
 */
public class JWTClaims extends HashMap<String, Object> {

    /**
     * 设置 JWT 的主题（Subject）。
     *
     * <p>该方法用于将指定的主题信息存入 JWT 的声明中，通常表示该令牌的持有者。
     *
     * @param subject JWT 的主题
     */
    public void setSubject(String subject) {
        put("sub", subject);
    }

    /**
     * 设置 JWT 的受众（Audience）。
     *
     * <p>该方法用于指定 JWT 适用的受众，即该令牌预期的使用者。
     *
     * @param audience JWT 的受众
     */
    public void setAudience(String audience) {
        put("aud", audience);
    }

    /**
     * 设置 JWT 的签发者（Issuer）。
     *
     * <p>该方法用于记录 JWT 的签发者信息，通常用于验证令牌来源的可信度。
     *
     * @param issuer JWT 的签发者
     */
    public void setIssuer(String issuer) {
        put("iss", issuer);
    }

    /**
     * 设置 JWT 的签发时间（Issued At）。
     *
     * <p>该方法用于存储 JWT 的签发时间，表示令牌何时创建。签发时间通常用于验证
     * 令牌的有效性，例如防止令牌在未来时间点之前使用。
     *
     * @param duration 签发时间，使用 {@link Chrono} 表示
     */
    public void setIssuedAt(Chrono duration) {
        put("iat", duration.getTime());
    }

    /**
     * 设置 JWT 的过期时间（Expiration）。
     *
     * <p>该方法计算基于当前时间的过期时间戳，并存入 JWT 声明中。JWT 过期时间用于
     * 指定令牌的有效期，超过该时间后令牌将被视为无效。
     *
     * @param duration 过期时间的时长
     * @param timeUnit 时间单位（如 {@link TimeUnit#SECONDS}, {@link TimeUnit#MINUTES}, {@link TimeUnit#HOURS}）
     */
    public void setExpirationTime(long duration, TimeUnit timeUnit) {
        put("exp", System.currentTimeMillis() + timeUnit.toMillis(duration));
    }

    /**
     * 获取 JWT 中的 subject（主题）
     *
     * <p>从 JWT 声明中获取 "subject" 属性并返回其值。
     *
     * @return JWT 中的 subject 字段值
     */
    public String getSubject() {
        return getAttribute("sub", TypeCvt::atos);
    }

    /**
     * 获取 JWT 中的 audience（受众）
     *
     * <p>从 JWT 声明中获取 "audience" 属性并返回其值。
     *
     * @return JWT 中的 audience 字段值
     */
    public String getAudience() {
        return getAttribute("aud", TypeCvt::atos);
    }

    /**
     * 获取 JWT 中的 issuer（签发者）
     *
     * <p>从 JWT 声明中获取 "iss" 属性并返回其值。
     *
     * @return JWT 中的 issuer 字段值
     */
    public String getIssuer() {
        return getAttribute("iss", TypeCvt::atos);
    }

    /**
     * 获取 JWT 的 issuedAt（签发时间）
     *
     * <p>从 JWT 声明中获取 "iat" 属性并将其转换为 Chrono 对象，表示签发时间。
     *
     * @return JWT 的签发时间
     */
    public long getIssuedAt() {
        return getAttribute("iat", TypeCvt::atol);
    }

    /**
     * 获取 JWT 的 expiration（过期时间）
     *
     * <p>从 JWT 声明中获取 "exp" 属性并将其转换为 Chrono 对象，表示过期时间。
     *
     * @return JWT 的过期时间
     */
    public long getExpirationTime() {
        return getAttribute("exp", TypeCvt::atol);
    }

    /**
     * 检查 JWT 是否已过期
     *
     * <p>通过检查 JWT 的 expiration 属性与当前时间的对比，判断 JWT 是否已过期。
     *
     * @return 如果 JWT 已过期，返回 `true`；否则返回 `false`
     */
    public boolean isExpiration() {
        long expirationTime = getExpirationTime();
        // 如果是秒级时间戳则转为毫秒级别的时间戳
        if (expirationTime >= 0 && expirationTime <= 9999999999L)
            expirationTime *= 1000L;
        return Chrono.of(expirationTime).isBeforeNow();
    }

    /**
     * 获取 JWT 的唯一标识（JWT ID）。
     *
     * <p>JWT ID (JTI) 是一个唯一标识符，通常用于防止令牌重放攻击（Replay Attack）。
     * 该方法从 JWT 声明中提取 JTI 并转换为字符串。
     *
     * @return JWT 的唯一标识符 (JTI)，如果不存在则返回 null。
     */
    public String getJwtId() {
        return getAttribute("jti", TypeCvt::atos);
    }

    /**
     * 获取 JWT 的指定属性
     *
     * <p>根据指定的键获取 JWT 中的属性，并通过默认转换器（BasicConverter）进行转换。
     *
     * @param key 属性的键
     * @return 属性的值
     */
    public String getAttribute(String key) {
        return getAttribute(key, TypeCvt::atos);
    }

    /**
     * 获取 JWT 的指定属性并应用自定义转换器
     *
     * <p>根据指定的键获取 JWT 中的属性，并应用提供的转换器对值进行转换。
     *
     * @param key 属性的键
     * @param mapper 自定义的类型转换器
     * @param <T> 原始值的类型
     * @param <R> 转换后的值的类型
     * @return 转换后的属性值
     */
    @SuppressWarnings("unchecked")
    public <T, R> R getAttribute(String key, TypeMapper<T, R> mapper) {
        return mapper.call((T) get(key));
    }

}
