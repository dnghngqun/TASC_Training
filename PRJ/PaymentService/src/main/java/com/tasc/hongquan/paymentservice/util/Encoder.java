package com.tasc.hongquan.paymentservice.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Formatter;
@Slf4j
public class Encoder {
    private static final char[] HEX_CHARS ="0123456789ABCDEF".toCharArray();
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static Logger logger = LoggerFactory.getLogger(Encoder.class);
    @SuppressWarnings("resource")
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for(byte b : bytes) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }

    public static String signHmacSHA256(String data, String secrecKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secrecKey.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return toHexString(rawHmac);
    }
    public static String getSHA(String data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] ba = md.digest();
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for(int i = 0; i < ba.length; i++) {
            sb.append(HEX_CHARS[(((int) ba[i] & 0xFF)/16)& 0x0F]);
            sb.append(HEX_CHARS[((int) ba[i] & 0xFF) % 16]);
        }
        return sb.toString();
    }

    public static String decode64(String s){
        try {
            byte[] valueDecoded = Base64.decode(s.getBytes());
            return new String(valueDecoded);
        }catch (Exception e) {
            return "";
        }
    }
    public static String encode64(String s){
        byte[] bytesEncoded = Base64.encode(s.getBytes());
        return new String(bytesEncoded);
    }

    public static String hashSHA256(String input) throws Exception{
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(input.getBytes());
            BigInteger dis = new BigInteger(1, sha.digest());
            String result = dis.toString(16);
            if(!result.startsWith("0") && result.length() < 64){
                result = '0' + result;
            }
            return result.toUpperCase();
        }catch (NoSuchAlgorithmException ex){
            throw new Exception("Hash SHA-256 error");
        }
    }

    public static String hmacSha1(String value, String key) throws Exception{
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(value.getBytes());
        return new String(Base64.encode(rawHmac));
    }

    public static String encryptRSA(byte[] dataBytes, String publicKey) throws Exception{
        PublicKey pubk;
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

        byte[] publicKeyBytes = decoder.decode(publicKey);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        pubk = keyFactory.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubk);
        return encoder.encodeToString(cipher.doFinal(dataBytes)).replace("\r", "");
    }

    public static String decryptRSA(String encryptData, String privateKey){
        try{
            byte[] privateKeyBytes = Base64.decode(privateKey);;
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey prvk = keyFactory.generatePrivate(privateKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prvk);
            return new String(cipher.doFinal(Base64.decode(encryptData)));
        }catch (Exception e) {
            logger.error("Error when decrypt RSA: " + e.getMessage());
            return "";
        }

    }
}

