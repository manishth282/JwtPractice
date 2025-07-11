package com.company.projectjwt1.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final static Logger log = LoggerFactory.getLogger(JWTService.class);

//    private String secretKeyStr;
    final private String secretKeyString = "NcVv+s0DGHa1YATmy2XAXg8GLS2bkaMNBRBuUo1QVEM=";
    private byte[] keyBytes;

    JWTService(){
        log.warn("************** JWTService constructor execution.....");
        /** Instead of creation of secretKey everytime when server start Recommended to use single(constant) key*/
        keyBytes = Decoders.BASE64.decode(secretKeyString);
        /*try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey();
//            keyBytes = secretKey.getEncoded();

            // We can convert SecretKey(Which is containing bytes form of Key) to String
//            secretKeyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }*/
    }


    public String generateToken(String usernameOrEmail) {
        log.warn("************** JWTService's method -> generateToken execution");
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usernameOrEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30*60*1000))
                .signWith(getKey())
                .compact();
    }

    public Key getKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(secretKeyStr);  //Changing secretKey String form to byte array
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        log.warn("JWTService's method -> validate token method execution");
        final String username = extractUsername(token); //not use of
        System.out.println("JWTService ::: \n ValidateToken ---> token :::: "+token+" \n username ::::"+username);
        System.out.println("username.equals(userDetails.getUsername()) ;;;;;  "+username.equals(userDetails.getUsername()));
        System.out.println("isTokenExpired(token) ;;;; "+isTokenExpired(token));
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractException(token).before(new Date());
    }

    private Date extractException(String token) {
        return extractClaim(token, claims -> claims.getExpiration());
    }

    public String extractUsername(String token) {
        System.out.println("JWTService  :::::  --->  extractUsername");
        return extractClaim(token, claims->claims.getSubject());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        System.out.println("JWTService  :::::  --->  extractClaim ");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
            System.out.println("JWTService  :::::  --->  extractAllClaims ");
            try {
                return Jwts.parserBuilder()
                        .setSigningKey(getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (SignatureException e) {
                log.warn("Invalid JWT signature: {}", e.getMessage());
                throw new RuntimeException("Invalid JWT signature "+e.getMessage());
            } catch (JwtException e) {
                log.warn("JWT parsing error: {}", e.getMessage());
                throw new RuntimeException("JWT parsing error "+e.getMessage());
            }
        }


    }


