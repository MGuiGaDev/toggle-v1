package com.app.togglev1.security.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.app.togglev1.security.dtos.JwtDTO;
import com.app.togglev1.security.entities.MainUser;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Esta clase crea y valida el Token
 * @author Manuel
 *
 */

@Component
public class JwtProvider {

private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private int expiration;
	
	
	public String generateToken(Authentication authentication) {
		MainUser mainUser = (MainUser) authentication.getPrincipal();
		List<String> roles = mainUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		String userType;
		if(roles.contains("ROLE_MANAGER_CENTER")) {
			userType = "M";
		} else {
			userType = "N";
		}
		return Jwts.builder()
				.setSubject(mainUser.getUsername())
				.claim("roles", roles)
				.claim("userType", userType)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS256, secret.getBytes())
				.compact();
	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(secret.getBytes())
				.parseClaimsJws(token);
			return true;
		} catch(MalformedJwtException mF) {
			logger.error("token mal formado");
		}catch(UnsupportedJwtException unsupported) {
			logger.error("token no soportado");
		}catch(ExpiredJwtException expired) {
			logger.error("token expirado");
		}catch(IllegalArgumentException illegal) {
			logger.error("token vacío");
		}catch(SignatureException signature) {
			logger.error("fallo en la firma");
		}
		return false;
	}
	
	public String refreshToken(JwtDTO jwtDto) throws ParseException {
		JWT jwt = JWTParser.parse(jwtDto.getToken());
		JWTClaimsSet claims = jwt.getJWTClaimsSet();
		String username = claims.getSubject();
		List<String>roles = (List<String>) claims.getClaim("roles");
		return Jwts.builder()
				.setSubject(username)
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
}
