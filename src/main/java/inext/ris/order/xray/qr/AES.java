package inext.ris.order.xray.qr;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret)
    {
        {
            MessageDigest md = null;
            byte[] digestOfPassword = null;

            try
            {
                md = MessageDigest.getInstance("md5");
                digestOfPassword = md.digest(secret.getBytes("UTF-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
                for (int j = 0, k = 16; j < 8; )
                {
                    keyBytes[k++] = keyBytes[j++];
                }

                SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");
                IvParameterSpec iv = new IvParameterSpec(new byte[8]);
                Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
                byte[] plainTextBytes = strToEncrypt.getBytes("UTF-8");
                byte[] cipherText = cipher.doFinal(plainTextBytes);
                //return cipherText;
                return Base64.encodeBase64String(cipherText);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (InvalidKeyException e)
            {
                e.printStackTrace();
            }
            catch (InvalidAlgorithmParameterException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        {

            MessageDigest md = null;
            byte[] digestOfPassword = null;

            try
            {
                byte[] message = Base64.decodeBase64(Arrays.toString(strToDecrypt.getBytes("UTF-8")));
                /**
                 * make md5
                 */
                md = MessageDigest.getInstance("md5");
                digestOfPassword = md.digest(secret.getBytes("UTF-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
                for (int j = 0, k = 16; j < 8; )
                {
                    keyBytes[k++] = keyBytes[j++];
                }

                SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");
                IvParameterSpec iv = new IvParameterSpec(new byte[8]);
                Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
                byte[] cipherText = cipher.doFinal(message);

                return new String(cipherText, "UTF-8");
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (InvalidKeyException e)
            {
                e.printStackTrace();
            }
            catch (InvalidAlgorithmParameterException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            return "";
        }
    }

   /* public static String encryptAES(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytesEncoded = Base64.encodeBase64(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            return new String(bytesEncoded);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }*/

    public static String decryptAES(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] valueDecoded = cipher.doFinal( Base64.decodeBase64(Arrays.toString(strToDecrypt.getBytes("UTF-8"))));
            return new String(valueDecoded);
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    private static char[] encodeHex(byte[] data) {

        final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

    private static byte[] decodeHex(char[] data) throws Exception {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {

            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    private static int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static void main(String[] args)
    {
        final String secretKey = "pkvictoriaqrcode2022";

        String originalString = "073377^y4MVhQDt";
        String encryptedString = AES.encrypt(originalString, secretKey) ;
        String decryptedString = AES.decrypt(encryptedString, secretKey) ;

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
}