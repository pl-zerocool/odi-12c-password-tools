package pl.zerocool.odi12c.password;

import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {
    @Test
    public void validate1() throws Exception {
        Options opts = new Options();

        opts.cmd = "fail";
        opts.password = "pass";

        assertFalse(opts.validate());
    }

    @Test
    public void validate2() throws Exception {
        Options opts = new Options();

        opts.cmd = "encrypt";
        opts.password = "pass";
        opts.encAlgo = "AES";

        assertFalse(opts.validate());
    }

    @Test
    public void validate3() throws Exception {
        Options opts = new Options();

        opts.cmd = "encrypt";
        opts.password = "pass";
        opts.encProperties = "enc.properties.example";

        assertTrue(opts.validate());
    }

    @Test
    public void validate4() throws Exception {
        Options opts = new Options();

        opts.cmd = "verify";
        opts.password = "pass";

        assertFalse(opts.validate());
    }
}