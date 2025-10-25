package cn.reghao.jutil.auth;

import cn.reghao.jutil.auth.model.OssPayload;
import cn.reghao.jutil.auth.model.RefreshPayload;
import cn.reghao.jutil.auth.model.JwtPayload;
import io.jsonwebtoken.*;

import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 令牌
 *
 * @author reghao
 * @date 2025-10-25 11:10:58
 */
public class JwtUtil {
    public static final String JWT_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    public static String createOssToken(OssPayload ossPayload, long expireAt, String signKey) {
        return Jwts.builder()
                .claim("action", ossPayload.getAction())
                .claim("channelCode", ossPayload.getChannelCode())
                .setSubject(String.valueOf(ossPayload.getUserId()))
                .setExpiration(new Date(expireAt))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();
    }

    public static OssPayload getOssPayload(String jwtToken, String signKey) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(signKey).build();
        Claims claims = (Claims) jwtParser.parse(jwtToken).getPayload();

        String action = (String) claims.get("action");
        Double channelCodeD = (Double) claims.get("channelCode");
        int channelCode = channelCodeD.intValue();
        String userIdStr = claims.getSubject();
        return new OssPayload(channelCode, action, Integer.parseInt(userIdStr));
    }

    /**
     * 生成一个 token
     *
     * @param
     * @return
     * @date 2019-11-21 下午4:39
     */
    public static String createAccessToken(JwtPayload jwtPayload, long expireAt, String signKey) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        return Jwts.builder()
                .claim("plat", jwtPayload.getPlat())
                .claim("loginId", jwtPayload.getLoginId())
                .claim("loginType", jwtPayload.getLoginType())
                .claim("authorities", jwtPayload.getAuthorities())
                .setSubject(jwtPayload.getUserId()+"")
                .setExpiration(new Date(expireAt))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setId(jti)
                .compact();
    }

    public static String createAccessToken(JwtPayload jwtPayload, long expireAt, RSAPrivateKey privateKey) {
        // 根据 org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter 中的 WELL_KNOWN_AUTHORITIES_CLAIM_NAMES 字段
        // 将用户的 authorities 设置到 scope claim
        // TODO authorities claim 待删除
        String jti = UUID.randomUUID().toString().replace("-", "");
        return Jwts.builder()
                .claim("plat", jwtPayload.getPlat())
                .claim("loginId", jwtPayload.getLoginId())
                .claim("loginType", jwtPayload.getLoginType())
                .claim("scope", jwtPayload.getAuthorities())
                .claim("authorities", jwtPayload.getAuthorities())
                .setSubject(jwtPayload.getUserId()+"")
                .setExpiration(new Date(expireAt))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .setId(jti)
                .compact();
    }

    /**
     * 从 jwt token 中解析出已认证的 Authentication 对象
     * 过期的 token 会抛出 ExpiredJwtException 异常
     *
     * @param
     * @return
     * @date 2023-02-17 17:36:34
     */
    public static JwtPayload getJwtPayload(String jwtToken, PublicKey publicKey) {
        JwtParser jwtParser = Jwts.parser().verifyWith(publicKey).build();
        Claims claims = (Claims) jwtParser.parse(jwtToken).getPayload();

        Double platD = (Double) claims.get("plat");
        String loginId = (String) claims.get("loginId");
        Double loginTypeD = (Double) claims.get("loginType");
        String userId = claims.getSubject();
        // TODO userId 是系统分配且固定的，但需要检查用户的 roles 是否发生变化
        String roles = (String) claims.get("authorities");
        long expireAt = claims.getExpiration().getTime();
        String jti = (String) claims.get("jti");
        if (platD == null || loginId == null || userId == null || roles == null) {
            return null;
        }

        int plat = platD.intValue();
        int loginType = loginTypeD.intValue();
        return new JwtPayload(plat, loginId, Long.parseLong(userId), loginType, roles, jti);
    }

    /**
     * 创建刷新令牌
     *
     * @param
     * @return
     * @date 2023-02-17 15:20:33
     */
    public static String createRefreshToken(JwtPayload jwtPayload, long expireAt, String signKey) {
        return Jwts.builder()
                .claim("plat", jwtPayload.getPlat())
                .claim("loginId", jwtPayload.getLoginId())
                .setSubject(String.valueOf(jwtPayload.getUserId()))
                .setExpiration(new Date(expireAt))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();
    }

    /**
     * 解析刷新令牌
     *
     * @param
     * @return
     * @date 2023-02-17 15:22:54
     */
    public static RefreshPayload getRefreshPayload(String jwtToken, String signKey) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(signKey).build();
        Claims claims = (Claims) jwtParser.parse(jwtToken).getPayload();

        long userId = Long.parseLong(claims.getSubject());
        Double platD = (Double) claims.get("plat");
        int plat = platD.intValue();
        String loginId = (String) claims.get("loginId");
        return new RefreshPayload(userId, plat, loginId);
    }
}
