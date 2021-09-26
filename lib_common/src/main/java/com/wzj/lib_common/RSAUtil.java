package com.wzj.lib_common;


import com.wzj.lib_common.http.rsa.RSAKey;
import com.wzj.lib_common.util.Base64Util;
import com.ydph.lib_common.log.Logs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import kotlin.text.Charsets;

/**
 * @author: 程龙 on 2019/3/13.
 * bolg:  https://me.csdn.net/qq_25749749
 * RSA算法，实现数据的加密解密。
 */
public class RSAUtil {

    /**
     * 使用私钥解密
     *
     * @param content
     * @param private_key
     * @param input_charset
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String private_key) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        try  {

            byte[] decoded = base64decode(RSAKey.INSTANCE.getPrivateKey());
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            int keySize = priKey.getModulus().bitLength();
            int maxBlock = keySize / 8;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = base64decode(content);
            int inputLen = b.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlock) {
                    cache = cipher.doFinal(b, offSet, maxBlock);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlock;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, Charsets.UTF_8);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 使用私钥解密
     * 分段解密
     * @param content
     * @param private_key
     * @param input_charset
     * @return
     * @throws Exception
     */
    public static String decrypt(String content) {

        try  {
            byte[] decoded = base64decode(RSAKey.INSTANCE.getPrivateKey());
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            int keySize = priKey.getModulus().bitLength();
            int maxBlock = keySize / 8;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = Base64Util.decode(content);
//            byte[] b = content.getBytes(StandardCharsets.UTF_8);
            int inputLen = b.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlock) {
                    cache = cipher.doFinal(b, offSet, maxBlock);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlock;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, Charsets.UTF_8);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static byte[] getMaxResultDecrypt(String str, Cipher cipher) {
        Logs.e("加密解密", str);
//        byte[] inputArray = base64decode(str);
        byte[] inputArray = str.getBytes(StandardCharsets.UTF_8);
        int inputLength = inputArray.length;
        ByteArrayOutputStream out = null;
        out = new ByteArrayOutputStream();
        try {
            // 最大解密字节数，超出最大字节数需要分组加密
            int MAX_ENCRYPT_BLOCK = 64;
            // 标识
            int offSet = 0;
            int i = 0;
            byte[] cache = {};
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;

//                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
//                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
            }
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    /**
     * 获得私钥
     *
     * @param key 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = base64decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    /**
     * 得到公钥
     *
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = base64decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(x509);
    }


    /**
     * base64加密
     *
     * @param bstr
     * @return
     */
    private static String base64encode(byte[] bstr) {
        String str = new BASE64Encoder().encode(bstr);
        str = str.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
        return str;
    }

    /**
     * base64解密
     *
     * @param str
     * @return byte[]
     */
    private static byte[] base64decode(String str) {
        byte[] bt = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            bt = decoder.decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bt;
    }

    /**
     * 使用公钥加密
     *
     * @param content 密文
     * @param pub_key 公钥
     * @return
     */
    public static String encryptByPublic(String content, String pub_key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(pub_key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            return base64encode(output);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用公钥加密
     *
     * @param content 密文
     * @param pub_key 公钥
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(RSAKey.INSTANCE.getPublicKey());

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes(StandardCharsets.UTF_8);
            byte[] output = cipher.doFinal(plaintext);

            return base64encode(output);

        } catch (Exception e) {
            return null;
        }
    }


}