package pl.zerocool.odi12c.password;

import oracle.odi.core.security.cryptography.IOdiCipher;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Command {
    private static Logger LOGGER = Logger.getLogger(Command.class.getName());

    private final Options opts;

    public Command(Options opts) {
        this.opts = opts;
    }

    public static Properties readEncProperties(String filePath) throws Exception {
        Properties prop = new Properties();
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        prop.load(inputStream);

        return prop;
    }

    public void run() throws Exception {
        String out;
        if(opts.cmd.equals("encode")) {
            out = Tools.encode(opts.password);
        } else if(opts.cmd.equals("decode")) {
            out = Tools.decode(opts.password);
        } else if(opts.cmd.equals("hashWithSalt")) {
            out = Tools.hashWithSalt(opts.password, opts.salt);
        } else {
            // cipher
            IOdiCipher cipher;

            if(opts.encProperties != null) {
                LOGGER.info("reading encryption properies from file " + opts.encProperties);

                Properties prop = Command.readEncProperties(opts.encProperties);

                String algo = prop.getProperty("ENC_ALG");
                String keyLength = prop.getProperty("ENC_KEY_LEN");
                String key = prop.getProperty("ENC_KEY");
                String iv = prop.getProperty("ENC_IV");

                LOGGER.info("ENC_ALG=" + algo);
                LOGGER.info("ENC_KEY_LEN=" + keyLength);
                LOGGER.info("ENC_KEY=" + key);
                LOGGER.info("ENC_IV=" + iv);

                if(algo == null || keyLength == null || key == null || iv == null) {
                    throw new Exception("encryption properties file is malformed");
                }

                cipher = Tools.getCipher(algo, keyLength, key, iv);
            } else {
                cipher = Tools.getCipher(opts.encAlgo, opts.encKeyLen, opts.encKey, opts.encIV);
            }

            if(opts.cmd.equals("encrypt")) {
                out = Tools.encrypt(opts.password, cipher);
            } else if(opts.cmd.equals("decrypt")) {
                out = Tools.decrypt(opts.password, cipher);
            } else if(opts.cmd.equals("hash")) {
                out = Tools.hash(opts.password, cipher);
            } else if(opts.cmd.equals("decryptSalt")) {
                out = Tools.decryptSalt(opts.password, cipher);
            } else if(opts.cmd.equals("verify")) {
                out = String.valueOf(Tools.verify(opts.password, opts.hPassword, cipher));
            } else {
                out = "ERROR: unsupported command";
            }
        }

        System.out.println(out);
    }
}
