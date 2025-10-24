package cn.reghao.jutil.tool.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 令牌
 *
 * @author reghao
 * @date 2019-11-17 23:10:58
 */
public class Jwt {
    public static final String JWT_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    /**
     * 生成一个 token
     *
     * @param
     * @return
     * @date 2019-11-21 下午4:39
     */
    public static String create(JwtPayload payload) {
        return Jwts.builder()
                .claim("loginType", payload.getLoginType())
                .claim("plat", payload.getPlat())
                .claim("authorities", payload.getRoles())
                .setSubject(payload.getUserId())
                .setExpiration(new Date(payload.getExpireAt()))
                .signWith(SignatureAlgorithm.HS256, payload.getSignKey())
                .compact();
    }

    /**
     * 解析 token，过期的 token 会抛出 ExpiredJwtException 异常
     *
     * @param
     * @return
     * @date 2021-07-27 下午2:37
     */
    public static JwtPayload parse(String token, String signKey) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(signKey).build();
        Claims claims = (Claims) jwtParser.parse(token);
        //Claims claims0 = Jwts.parser().setSigningKey(signKey).parseClaimsJws(token).getBody();

        String username = claims.getSubject();
        String roles = (String) claims.get("authorities");
        Integer loginType = (Integer) claims.get("loginType");
        Integer plat = (Integer) claims.get("plat");
        Date expiration = claims.getExpiration();
        return new JwtPayload(loginType, plat, username, roles, expiration.getTime(), signKey);
    }
}
