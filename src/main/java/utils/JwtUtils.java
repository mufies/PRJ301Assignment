package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtUtils {
    private static final String SECRET_KEY = "oY8vQ3wW9zT4nA1sL6pR2dF7uJ0xC5hB";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final String ENCRYPTION_KEY = "aX9bK4mP2nL7vQ5s"; // 16 bytes for AES-128

    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    public static String generateToken(int id,String role) {
        return encrypt(Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +  30*60 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
        );
    }

    public static boolean validateToken(String token, String id) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(decrypt(token))
                    .getBody();

            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            Date now = new Date();

            // Kiểm tra subject và thời hạn token
            return subject.equals(id) && expiration.after(now);

        } catch (ExpiredJwtException e) {
            // Token đã hết hạn
            System.out.println("Token expired: " + e.getMessage());
            return false;
        } catch (JwtException e) {
            // Token không hợp lệ (signature sai, format sai, etc.)
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Lỗi khác (decrypt failed, etc.)
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    public static String getIDFromToken(String token1) {
        if (token1 == null || token1.trim().isEmpty()) {
            return null;
        }
        String token = decrypt(token1);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getRoleFromToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return null;
            }

            String decryptedToken = decrypt(token); // Decrypt before parsing
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(decryptedToken)
                    .getBody()
                    .get("role", String.class);

        } catch (JwtException e) {
            System.err.println("JWT Error: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid token format: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error getting role from token: " + e.getMessage());
            return null;
        }
    }




}
