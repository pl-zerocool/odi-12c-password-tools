package pl.zerocool.odi12c.password;

import org.kohsuke.args4j.Option;

import java.util.Arrays;
import java.util.Locale;

/**
 * Options.
 */
public class Options {
    @Option(name = "-cmd", usage = "command name (encode, decode, hashWithSalt, encrypt, decrypt, hash, decryptSalt, verify); in case of encode, decode, hashWithSalt encryption parameters are not necessary", required = true)
    public String cmd;

    @Option(name = "-password", usage = "password (plain, encrypted, encoded, hashed, ...)", required = true)
    public String password;

    @Option(name = "-salt", usage = "salt (hashWithSalt only)", required = false)
    public String salt;

    @Option(name = "-hPassword", usage = "hashed password (verify only)", required = false)
    public String hPassword;


    /* encryption */

    @Option(name = "-encAlgo", usage = "encryption algorithm", required = false)
    public String encAlgo;

    @Option(name = "-encKeyLen", usage = "encryption key length", required = false)
    public String encKeyLen;

    @Option(name = "-encKey", usage = "encryption key", required = false)
    public String encKey;

    @Option(name = "-encIV", usage = "encryption IV (initialization vector)", required = false)
    public String encIV;

    @Option(name = "-encProperties", usage = "properties file with encryption parameters (ENC_ALG, ENC_KEY_LEN, ENC_KEY, ENC_IV)", required = false)
    public String encProperties;

    /* common */

    @Option(name = "-logLevel", usage = "specifies log level [off (def), severe, warning, info, config, fine, finer, finest, all]", required = false)
    public String logLevel;

    @Option(name = "-help", usage = "prints help", help = true)
    public boolean help;

    /* validate */

    public boolean validateLogLevel() {
        return (logLevel == null || Arrays.asList(new String[] {"OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL"}).contains(logLevel.toUpperCase(Locale.ENGLISH)));
    }

    public boolean validateEnc() {
        return ((encAlgo != null && encKeyLen != null && encKey != null && encIV != null) || encProperties != null);
    }

    public boolean validateCmdName() {
        return (Arrays.asList(new String[] {"encode", "decode", "hashWithSalt", "encrypt", "decrypt", "hash", "decryptSalt", "verify"}).contains(cmd));
    }

    public boolean validateCmd() {
        if(!validateCmdName()) {
            return false;
        }

        if(cmd.equals("encode") || cmd.equals("decode")) {
            // encode, decode -> password only (required)
            return true;
        } else if(cmd.equals("hashWithSalt")) {
            return salt != null;
        } else if(cmd.equals("encrypt") || cmd.equals("decrypt") || cmd.equals("hash") || cmd.equals("decryptSalt")) {
            return validateEnc();
        } else if(cmd.equals("verify")) {
            return validateEnc() && hPassword != null;
        }

        return false;
    }


    public boolean validate() {
        return (validateLogLevel() && validateCmd());
    }
}