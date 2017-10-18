package test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yusute.common.codec.md5.YusuteMD5;
import com.yusute.common.codec.rsa.RSAKeyPair;
import com.yusute.common.codec.rsa.YusuteRSA;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yusutehot
 */
public class MainTest {

    public static void main(String[] args) {

        try {
            RSAKeyPair rsaKeyPair = YusuteRSA.genKeyPair();

            RSAPublicKey publicKey = rsaKeyPair.getPublicKey();
            RSAPrivateKey privateKey = rsaKeyPair.getPrivateKey();

            Algorithm algorithmRS = Algorithm.RSA256(publicKey, privateKey);
            String token = createToken(algorithmRS);
            System.out.println(token.length());
            System.out.println(YusuteRSA.getPrivateKey(rsaKeyPair));
            System.out.println(YusuteRSA.getPublicKey(rsaKeyPair));
            String md5Token = YusuteMD5.encode(token);
            System.out.println("token="+md5Token);
            verifyToken(token, algorithmRS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createToken(Algorithm algorithm) throws Exception {
        //Map<String, Object> map = new HashMap<String, Object>();
        //map.put("alg", "RS256");
        //map.put("typ", "JWT");
        String token = JWT.create()
        //    .withHeader(map)//header
           // .withClaim("name", "zwz")//payload
            //.withClaim("age", "18")
            .sign(algorithm);//加密
        return token;
    }

    public static void verifyToken(String token, Algorithm algorithm) throws Exception {
        JWTVerifier verifier = JWT.require(algorithm)
            .build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(claims);
        System.out.println(claims.get("name").asString());
    }
}
