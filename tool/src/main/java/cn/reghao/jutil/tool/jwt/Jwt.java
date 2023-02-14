package cn.reghao.jutil.tool.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 令牌
 * TODO 将 JWT 令牌存放在 redis 中
 *
 * @author reghao
 * @date 2019-11-17 23:10:58
 */
public class Jwt {
    public static final String JWT_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    // TODO 有效期和 key 都应该可以动态设置，有效期一周
    private static final long EXPIRATION_TIME = 60_000*60*24*7;
    private static final String SIGN_KEY = "tnb.reghao.cn";

    /**
     * 生成一个 token
     *
     * @param
     * @return
     * @date 2019-11-21 下午4:39
     */
    public static String create(JwtPayload payload) {
        return Jwts.builder()
                .claim("authorities", payload.getRoles())
                .setSubject(payload.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + payload.getExpireIn()))
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .compact();
    }

    /**
     * 解析 token，过期的 token 会抛出 ExpiredJwtException 异常
     *
     * @param
     * @return
     * @date 2021-07-27 下午2:37
     */
    public static JwtPayload parse(String token) {
        Claims claims = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        String roles = (String) claims.get("authorities");
        Date expiration = claims.getExpiration();
        return new JwtPayload(username, roles, expiration.getTime());
    }
}
