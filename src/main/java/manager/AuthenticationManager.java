package manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.User;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AuthenticationManager {
    static final String SECRET_KEY = "iemdb1401";

    private static Key getKey() {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
    
    public static String generateJWT(User user) {
        Key key = getKey();

        return Jwts.builder()
                .setIssuer("iemdb")
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .setIssuedAt(Date.from(Instant.now()))
                .claim("email", user.getEmail())
                .signWith(key)
                .compact();
    }

    public static Claims parseJWT(String jwtString) {
        Key key = getKey();

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();
    }
}
