package pl.zerocool.odi12c.password;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class CommandTest {
    @Test
    public void readEncProperties() throws Exception {
        Properties prop = Command.readEncProperties("enc.properties.example");
        assertEquals(prop.getProperty("ENC_ALG"), "AES");
        assertEquals(prop.getProperty("ENC_KEY_LEN"), "128");
        assertEquals(prop.getProperty("ENC_KEY"), "aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw");
        assertEquals(prop.getProperty("ENC_IV"), "zcL89phw9gp5QwoOA7H+6w==");
    }
}