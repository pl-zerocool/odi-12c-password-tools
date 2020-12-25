package pl.zerocool.odi12c.password;

import oracle.odi.core.security.cryptography.IOdiCipher;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToolsTest {
    private static IOdiCipher CIPHER;

    static  {
        try {
            CIPHER = Tools.getCipher("AES", "128", "aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw", "zcL89phw9gp5QwoOA7H+6w==");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void encode() throws Exception {
        // encode can generate different results
        assertNotNull(Tools.encode("test"));
    }

    @Test
    public void decode() throws Exception {
        assertEquals(Tools.decode(Tools.encode("test")), "test");
    }

    @Test
    public void encrypt() throws Exception {
        assertEquals(Tools.encrypt("test", CIPHER), "YZtFu8yth72YMAFmdbOziA==");
    }

    @Test
    public void decrypt() throws Exception {
        assertEquals(Tools.decrypt(Tools.encrypt("test", CIPHER), CIPHER), "test");
    }

    @Test
    public void hash() throws Exception {
        // hash can generate different results (random salt)
        assertNotNull(Tools.hash("test", CIPHER));
    }

    @Test
    public void decryptSalt() throws Exception {
        assertEquals(Tools.decryptSalt("Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=", CIPHER), "3pCaxQ3Y0OnivH1HgWenP8OjUivgjDOy/dVU02ZIfPU=");
    }

    @Test
    public void hashWithSalt() throws Exception {
        String pass = "test";

        String hPassWithSalt = Tools.hash(pass, CIPHER);
        String salt = Tools.decryptSalt(hPassWithSalt, CIPHER);

        String hPass = Tools.hashWithSalt(pass, salt);

        assertTrue(hPassWithSalt.contains(hPass));
    }

    @Test
    public void verify() throws Exception {
        String pass = "test";
        String hPassWithSalt = "Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=";

        assertTrue(Tools.verify(pass, hPassWithSalt, CIPHER));
    }
}