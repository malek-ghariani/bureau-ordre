package tn.iit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.function.Function;

@Service
public class JwtService {

	@Value("${app.jwt.secret}") 
	private String secret;

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date()) 
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) 
				.signWith(getSecretKey(), SignatureAlgorithm.HS256).compact();
	}

	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	
	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}
}