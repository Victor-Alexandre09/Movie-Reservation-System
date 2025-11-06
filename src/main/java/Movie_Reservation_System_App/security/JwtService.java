package Movie_Reservation_System_App.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "hnvuioehf9p8zPOIFHj9843qkajZHF987kowerf398CWJ84XSNJC3";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final int EXPIRATION_TIME_MS = (1000 * 60 * 60);

    public String createJwt (String userEmail) {
        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key)
                .compact();
    }

    public String extractUserEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
