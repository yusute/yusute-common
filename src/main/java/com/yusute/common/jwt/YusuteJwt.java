package com.yusute.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Map;

/**
 * @author yusutehot
 */
public class YusuteJwt {

    public static final String SECRET = "RS256";

    public static Algorithm algorithm(String secret) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return algorithm;
    }

    public static String createToken(Algorithm algorithm, String json) throws Exception {
        return JWT.create().withClaim("data", json).sign(algorithm);
    }

    public static void verifyToken(String token, Algorithm algorithm) throws Exception {
        JWTVerifier verifier = JWT.require(algorithm)
            .build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(claims.get("data").asString());
    }

    public static void main(String[] args) throws Exception {
        Algorithm algorithm = YusuteJwt.algorithm(SECRET);
        String token = YusuteJwt.createToken(algorithm, "{\n"
            + "  \"name\": \"json在线编辑器\",\n"
            + "  \"v1.0\": \"2014-07-02 22:05 工具上线\",\n"
            + "  \"v2.0\": \"2016-11-16 14:13 增加php, go类生成\",\n"
            + "  \"v2.1\": \"2016-11-19 01:17 增加java类生成\"\n"
            + "}");
        System.out.println("token="+token);
        YusuteJwt.verifyToken(token, algorithm);
    }
}
