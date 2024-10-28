package com.philips.integrations.pitestkeycloak.model;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.RandomStringUtils;

public class EncryptionUtils {
    private static final int BLOCK_SIZE = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String DIGEST_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = RandomStringUtils.randomAlphanumeric(BLOCK_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(DIGEST_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), SECRET_KEY_ALGORITHM);
        return Base64.getEncoder().encodeToString(secret.getEncoded());
    }
    private static SecretKey restoreSecretKeyObject(String secretKey){
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(secretKeyBytes,0,secretKeyBytes.length,SECRET_KEY_ALGORITHM);
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[BLOCK_SIZE];
        // new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encryptString(String decryptedString, String secretKey){
        try {
            SecretKey secretKeyObject = restoreSecretKeyObject(secretKey)        ;
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyObject, generateIv());
            byte[] cipherText;
            cipherText = cipher.doFinal(decryptedString.getBytes());
            
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String decryptString(String encryptedString, String secretKey){
        try {
   
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKey secretKeyObject = restoreSecretKeyObject(secretKey);     
            cipher.init(Cipher.DECRYPT_MODE, secretKeyObject, generateIv());
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
            return new String(plainText);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }
        return null;
 
    }    
}
