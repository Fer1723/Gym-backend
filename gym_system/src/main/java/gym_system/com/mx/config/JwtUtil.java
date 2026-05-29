package gym_system.com.mx.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String SECRET = "FactoryGymSuperSecretKeyParaJWTQueDebeSerLarga2026";
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	private final long EXPIRATION_TIME = 86400000;
	
	public String generarToken(String username, String rol) {
		return Jwts.builder()
				.setSubject(username)
				.claim("rol", rol)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
}
