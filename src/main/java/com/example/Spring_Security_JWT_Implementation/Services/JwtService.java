package com.example.Spring_Security_JWT_Implementation.Services;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// Generate Token
	private String secretKey = "";

	// Generate Token
	public JwtService() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGenerator.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Generate Token
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 30))
				.and()
				.signWith(getKey())
				.compact();

	}

	// Generate Token
	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Verify Token
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// VerifyToken
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
		} catch (Exception e) {
			System.out.println("Token Parsing Error: " + e.getMessage());
			throw new RuntimeException("Invalid token");

		}
	}

	// VerifyToken
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		System.out.println("Extracted Username: " + userName);
		System.out.println("UserDetails Username: " + userDetails.getUsername());

		boolean isNotExpired = !isTokenExpired(token);
		System.out.println("Token Not Expired: " + isNotExpired);

		return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration);
	}

//
//	// Extract username from JWT
//	public String extractUserName(String token) {
//	    return extractClaim(token, Claims::getSubject);
//	}
//
//	// Extract a specific claim from JWT
//	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//	    final Claims claims = extractAllClaims(token);
//	    return claimsResolver.apply(claims);
//	}
//
//	// Extract all claims from JWT
//	private Claims extractAllClaims(String token) {
//	    return Jwts.parser()
//	               .setSigningKey(getKey()) // Correct key usage
//	               .build()
//	               .parseClaimsJws(token)
//	               .getBody();
//	}
//
//	// Validate the token
//	public boolean validateToken(String token, UserDetails userDetails) {
//	    final String username = extractUserName(token);
//	    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//	}
//
//	// Check if the token is expired
//	private boolean isTokenExpired(String token) {
//	    return extractExpiration(token).before(new Date());
//	}
//
//	// Extract expiration date from JWT
//	private Date extractExpiration(String token) {
//	    return extractClaim(token, Claims::getExpiration);
//	}	
}
