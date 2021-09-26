package com.wzj.lib_common.util

import Decoder.BASE64Decoder
import Decoder.BASE64Encoder
import android.annotation.SuppressLint
import com.wzj.lib_common.http.rsa.RSAKey
import com.ydph.lib_common.log.Logs
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec


object DesUtil {
    private const val DES = "DES"
    private const val ENCODE = "GBK"
    private const val defaultKey = "netwxactive"

    /**
     * 使用 默认key 加密
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(data: String): String {
        val bt = encrypt(data.toByteArray(charset(ENCODE)), defaultKey.toByteArray(charset(ENCODE)))
        return BASE64Encoder().encode(bt)
    }

    /**
     * 使用 默认key 解密
     * @param data 待解密数据
     * @return
     * @throws IOException
     * @throws Exception
     */
    @Throws(IOException::class, Exception::class)
    fun decrypt(data: String?): String? {
        if (data == null) return null
        val decoder = BASE64Decoder()
        val buf: ByteArray = decoder.decodeBuffer(data)
        val bt = decrypt(buf, defaultKey.toByteArray(charset(ENCODE)))
        return String(bt, Charset.forName(ENCODE))
    }

    /**
     * Description 根据键值进行加密
     * @param data 待加密数据
     * @param key 密钥
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(data: String, key: String): String {
        val bt = encrypt(data.toByteArray(charset(ENCODE)), key.toByteArray(charset(ENCODE)))
        return BASE64Encoder().encode(bt)
    }

    /**
     * 根据键值进行解密
     * @param data 待解密数据
     * @param key    密钥
     * @return
     * @throws IOException
     * @throws Exception
     */
    @Throws(IOException::class, Exception::class)
    fun decrypt(data: String?, key: String): String? {
        if (data == null) return null
        val decoder = BASE64Decoder()
        val buf: ByteArray = decoder.decodeBuffer(data)
        val bt = decrypt(buf, key.toByteArray(charset(ENCODE)))
        return String(bt, Charset.forName(ENCODE))
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     * 加密键byte数组
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        // 生成一个可信任的随机数源
        val sr = SecureRandom()
        // 从原始密钥数据创建DESKeySpec对象
        val dks = DESKeySpec(key)
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        val keyFactory = SecretKeyFactory.getInstance(DES)
        val securekey = keyFactory.generateSecret(dks)
        // Cipher对象实际完成加密操作
        val cipher = Cipher.getInstance(DES)
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr)
        return cipher.doFinal(data)
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
        // 生成一个可信任的随机数源
        val sr = SecureRandom()

        // 从原始密钥数据创建DESKeySpec对象
        val dks = DESKeySpec(key)

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        val keyFactory = SecretKeyFactory.getInstance(DES)
        val securekey = keyFactory.generateSecret(dks)

        // Cipher对象实际完成解密操作
        val cipher = Cipher.getInstance(DES)

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr)
        return cipher.doFinal(data)
    }


    /**
     * RSA 加密
     * @param str 需要加密的字符串
     * publicKey  加密的KEY
     * @return
     * @throws Exception
     */
    @SuppressLint("NewApi")
    fun encrypt(str: ByteArray?): ByteArray? {
        try {
            // base64编码的公钥
            val decoded: ByteArray =Base64.getEncoder().encode(RSAKey.publicKey.toByteArray(StandardCharsets.UTF_8))
            val pubKey: RSAPublicKey = KeyFactory.getInstance(TRANSFORMATION).generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
            // RSA加密
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            val  dd = cipher.doFinal(str)
            Logs.e("DesUtil---加密", String(dd,StandardCharsets.UTF_8))
            return dd
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }

    /**
     * RSA 解密
     * @param str 需要解密的字符串
     * privateKey  解密的key
     * @return
     * @throws Exception
     */
    @SuppressLint("NewApi")
    fun decrypt(str: ByteArray?): ByteArray? {
        try {// base64编码的私钥

            val decoded: ByteArray = Base64.getDecoder().decode(RSAKey.privateKey)
            val priKey: RSAPrivateKey = KeyFactory.getInstance(TRANSFORMATION)
                .generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
            // RSA解密
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, priKey)
            val ddd = cipher.doFinal(str)
            Logs.e("DesUtil---解密", String(ddd,StandardCharsets.UTF_8))
            return ddd
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }


    /**RSA算法 */
    const val RSA = "RSA"
    /**加密方式，android的*/
//  public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**加密方式，android的 */ //  public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**加密方式，标准jdk的 */
    const val TRANSFORMATION = "RSA/None/PKCS1Padding"

    /** 使用公钥加密  */

    fun encryptByPublicKey(data: ByteArray?): ByteArray? {
        try {// 得到公钥对象
            val keySpec = X509EncodedKeySpec(RSAKey.publicKey.toByteArray(StandardCharsets.UTF_8))
            val keyFactory = KeyFactory.getInstance("RSA")
            val pubKey: PublicKey = keyFactory.generatePublic(keySpec)
            // 加密数据
            val cp = Cipher.getInstance(TRANSFORMATION)
            cp.init(Cipher.ENCRYPT_MODE, pubKey)
            return cp.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }

    /** 使用私钥解密  */

    fun decryptByPrivateKey(encrypted: ByteArray?): ByteArray? {
        try {// 得到私钥对象
            val keySpec = PKCS8EncodedKeySpec(RSAKey.privateKey.toByteArray(StandardCharsets.UTF_8))
            val kf = KeyFactory.getInstance(RSA)
            val keyPrivate: PrivateKey = kf.generatePrivate(keySpec)
            // 解密数据
            val cp = Cipher.getInstance(TRANSFORMATION)
            cp.init(Cipher.DECRYPT_MODE, keyPrivate)
            return cp.doFinal(encrypted)
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }


    /**秘钥默认长度 */
    const val DEFAULT_KEY_SIZE = 1024

    /**加密的数据最大的字节数，即117个字节 */
    const val DEFAULT_BUFFERSIZE = DEFAULT_KEY_SIZE / 8 - 11

    /**当加密的数据超过DEFAULT_BUFFERSIZE，则使用分段加密 */
    val DEFAULT_SPLIT = "#PART#".toByteArray()

    /** 使用公钥分段加密  */
    fun encryptByPublicKeyForSpilt(data: ByteArray?): ByteArray? {
        try {
            val dataLen = data?.size!!
            if (dataLen <= DEFAULT_BUFFERSIZE) {
                return encryptByPublicKey(data)
            }
            val allBytes: MutableList<Byte> = ArrayList(2048)
            var bufIndex = 0
            var subDataLoop = 0
            var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
            for (i in 0 until dataLen) {
                buf!![bufIndex] = data[i]
                if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                    subDataLoop++
                    if (subDataLoop != 1) {
                        for (b in DEFAULT_SPLIT) {
                            allBytes.add(b)
                        }
                    }
                    val encryptBytes = encryptByPublicKey(buf)
                    for (b in encryptBytes?.iterator()!!) {
                        allBytes.add(b)
                    }
                    bufIndex = 0
                    buf = if (i == dataLen - 1) {
                        null
                    } else {
                        ByteArray(
                            Math
                                .min(DEFAULT_BUFFERSIZE, dataLen - i - 1)
                        )
                    }
                }
            }
            val bytes = ByteArray(allBytes.size)
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
            return bytes
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }

    /** 使用私钥分段解密  */
    fun decryptByPrivateKeyForSpilt(encrypted: ByteArray?): ByteArray? {
        try {
            val splitLen = DEFAULT_SPLIT.size
            if (splitLen <= 0) {
                return decryptByPrivateKey(encrypted)
            }
            val dataLen = encrypted?.size!!
            val allBytes: MutableList<Byte> = ArrayList(1024)
            var latestStartIndex = 0
            run {
                var i = 0
                while (i < dataLen) {
                    val bt = encrypted[i]
                    var isMatchSplit = false
                    if (i == dataLen - 1) {
                        // 到data的最后了
                        val part = ByteArray(dataLen - latestStartIndex)
                        System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                        val decryptPart = decryptByPrivateKey(part)
                        for (b in decryptPart!!) {
                            allBytes.add(b)
                        }
                        latestStartIndex = i + splitLen
                        i = latestStartIndex - 1
                    } else if (bt == DEFAULT_SPLIT[0]) {
                        // 这个是以split[0]开头
                        if (splitLen > 1) {
                            if (i + splitLen < dataLen) {
                                // 没有超出data的范围
                                for (j in 1 until splitLen) {
                                    if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                        break
                                    }
                                    if (j == splitLen - 1) {
                                        // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                        isMatchSplit = true
                                    }
                                }
                            }
                        } else {
                            // split只有一位，则已经匹配了
                            isMatchSplit = true
                        }
                    }
                    if (isMatchSplit) {
                        val part = ByteArray(i - latestStartIndex)
                        System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                        val decryptPart = decryptByPrivateKey(part)
                        for (b in decryptPart!!) {
                            allBytes.add(b)
                        }
                        latestStartIndex = i + splitLen
                        i = latestStartIndex - 1
                    }
                    i++
                }
            }
            val bytes = ByteArray(allBytes.size)
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
            return bytes
        } catch (e: Exception) {
            e.printStackTrace()
            Logs.e("", e.message)
            return null
        }
    }
}