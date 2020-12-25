package pl.zerocool.odi12c.password;

import com.sunopsis.tools.core.SnpsStringTools;
import oracle.odi.core.security.cryptography.CipherFactory;
import oracle.odi.core.security.cryptography.CryptoUtil;
import oracle.odi.core.security.cryptography.IOdiCipher;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.util.Hashtable;
import java.util.Map;

public class Tools {
    /* cipher */

    public static Map<String, String> getCipherProperties(String algo, String keyLength, String key, String iv) {
        Hashtable result = new Hashtable();

        result.put("ENC_ALGO", algo);
        result.put("ENC_KEY_LEN", keyLength);
        result.put("ENC_KEY", key);
        result.put("ENC_IV", iv);

        return result;
    }

    public static IOdiCipher getCipher(String algo, String keyLength, String key, String iv) throws Exception {
        return CipherFactory.getOdiCipherWithoutOdiInstance(getCipherProperties(algo, keyLength, key, iv));
    }

    /* tools */

    public static String encode(String password) {
        //return ObfuscatedString.obfuscate(password).toString();
        return SnpsStringTools.snpsEncode_backward_support(password.toCharArray(), (String)null);
    }

    public static String decode(String password) {
        char[] out = SnpsStringTools.snpsDecode_backward_support(password.toCharArray(), (String)null);
        return String.valueOf(out);
    }

    public static String encrypt(String password, IOdiCipher cipher) throws GeneralSecurityException, IOException {
        return new String(cipher.encryptPassword(password.toCharArray()));
    }

    public static String decrypt(String password, IOdiCipher cipher) throws GeneralSecurityException {
        return new String(cipher.decryptPassword(SnpsStringTools.replaceNVL(password).toCharArray()));
    }

    public static String hash(String password, IOdiCipher cipher) throws GeneralSecurityException, IOException {
        return new String(CryptoUtil.hashPassword(password.toCharArray(), cipher));
    }

    public static byte[] decryptSaltByte(String password, IOdiCipher cipher) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] arr = password.split(":");
        String salt = arr[0];
        String hash = arr[1];

        //byte[] s = CryptoUtil.decryptSalt(salt, cipher);

        Method m = CryptoUtil.class.getDeclaredMethod("decryptSalt", String.class, IOdiCipher.class);
        m.setAccessible(true);
        Object o = m.invoke(null, salt, cipher);

        return (byte[]) o;
    }

    public static String decryptSalt(String password, IOdiCipher cipher) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Base64.encodeBase64String(Tools.decryptSaltByte(password, cipher));
    }

    public static String hashWithSaltByte(String password, byte[] salt) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // @see CryptoUtil.hashPassword

        //byte[] hash = CryptoUtil.pbkdf2(pPass.toCharArray(), salt, 5000, 32);

        // PBKDF2WithHmacSHA256 is irreversible

        Method m = CryptoUtil.class.getDeclaredMethod("pbkdf2", char[].class, byte[].class, int.class, int.class);
        m.setAccessible(true);
        Object o = m.invoke(null, password.toCharArray(), salt, 5000, 32);

        return new String(Base64.encodeBase64((byte[]) o));
    }

    public static String hashWithSalt(String password, String salt) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Tools.hashWithSaltByte(password, Base64.decodeBase64(salt));
    }

    public static boolean verify(String password, String hashedPassword, IOdiCipher cipher) throws GeneralSecurityException {
        return CryptoUtil.verifyPassword(password.toCharArray(), hashedPassword.toCharArray(), cipher);
    }

    /* utils */

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }
}
