package tn.iit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	// 🔹 Clé secrète pour signer les tokens (doit rester confidentielle)
	private final SecretKey SECRET_KEY = Keys
			.hmacShaKeyFor("MaCleUltraSecretePourJWTDoitEtreTresTresLongue1234567890ABCDE".getBytes());

	// 🔹 Extraire le username (email) depuis le token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// 🔹 Extraire n’importe quelle donnée depuis le token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// 🔹 Générer un token simple pour un utilisateur
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		// 🔹 Ajouter le rôle dans le token si UserDetails est un Employe
		if (userDetails instanceof tn.iit.entity.Employe) {
			tn.iit.entity.Employe employe = (tn.iit.entity.Employe) userDetails;
			claims.put("role", employe.getRole().name());
		}

		return generateToken(claims, userDetails);
	}

	// 🔹 Générer un token avec des claims supplémentaires
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
	}

	// 🔹 Vérifier si un token est valide pour un utilisateur donné
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	// 🔹 Vérifier si le token est expiré
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// 🔹 Extraire la date d’expiration du token
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// 🔹 Extraire tous les claims du token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	}
}
