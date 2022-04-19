package project.instagram.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {

  static final String CLAIM_KEY_USERNAME = "sub";
  static final String CLAIM_KEY_CREATED = "iat";
  private static final long serialVersionUID = -3301605591108950415L;
  private Clock clock = DefaultClock.INSTANCE;
  
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${jwt.signing.key.secret}")
  private String secret;

  @Value("${jwt.token.expiration.in.seconds}")
  private Long expiration;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getIssuedAt);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(clock.now());
  }

  @SuppressWarnings("unused")
  private Boolean ignoreTokenExpiration(String token) {
    // here you specify tokens, for that the expiration is ignored
    return false;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    Set<String> Userroles = new HashSet<>();
    userDetails.getAuthorities().forEach(a -> Userroles.add(a.getAuthority()));
    claims.put("roles", Userroles.toArray());

    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);
    
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
        .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
  }
  
  public String generateRefreshToken(UserDetails userDetails) {
	 Map<String, Object> claims = new HashMap<>();
	  
	    return doGenerateRefreshToken(claims, userDetails.getUsername());
  }
  
  private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
	final Date createdDate = clock.now();
	final Date expirationDate = calculateExpirationDateForRefreshToken(createdDate);
	    
	 return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
	        .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
  }

//  public Boolean canTokenBeRefreshed(String token) {
//    return (!isTokenExpired(token) || ignoreTokenExpiration(token));
//  }
  
  public Boolean canTokenBeRefreshed(String token) {
	return (isTokenExpired(token));
  }

  public String refreshToken(String token) {
    final Date createdDate = new Date(System.currentTimeMillis()+120);
    final Date expirationDate = calculateExpirationDate(createdDate);

    final Claims claims = getAllClaimsFromToken(token);
    claims.setIssuedAt(createdDate);
    claims.setExpiration(expirationDate);

    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
	  JwtDetails user = (JwtDetails) userDetails;
    final String username = getUsernameFromToken(token);
    return (username.equals(user.getUsername()) && !isTokenExpired(token));
  }
  
  
  
  public boolean validateJwtToken(String authToken) {
	    try {
	      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
	      return true;
	    } catch (SignatureException e) {
	      logger.error("Invalid JWT signature: {}", e.getMessage());
	    } catch (MalformedJwtException e) {
	      logger.error("Invalid JWT token: {}", e.getMessage());
	    } catch (ExpiredJwtException e) {
	      logger.error("JWT token is expired: {}", e.getMessage());
	    } catch (UnsupportedJwtException e) {
	      logger.error("JWT token is unsupported: {}", e.getMessage());
	    } catch (IllegalArgumentException e) {
	      logger.error("JWT claims string is empty: {}", e.getMessage());
	    }

	    return false;
	  }
  

  private Date calculateExpirationDate(Date createdDate) {
	  
    return new Date(createdDate.getTime() + expiration * 1000);
  }
  
  private Date calculateExpirationDateForRefreshToken(Date createdDate) {
	  
	    return new Date(createdDate.getTime() + 604800 * 1000);
	  }
}

