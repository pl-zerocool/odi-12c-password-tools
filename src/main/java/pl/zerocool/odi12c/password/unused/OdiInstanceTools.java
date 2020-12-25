package pl.zerocool.odi12c.password.unused;

import com.sunopsis.dwg.DwgObject;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.security.cryptography.CipherFactory;
import oracle.odi.core.security.cryptography.CryptoUtil;
import oracle.odi.runtime.agent.utils.TimeoutUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * In order to use setting odi classpath is required (see <ORACLE_HOME>/user_projects/domains/odi_agent/bin/encode.sh -> ${ODI_CLASSPATH}).
 */
public class OdiInstanceTools {
    /* connection params */

    private static class ConnectionParams {
        private final String masterDriver;
        private final String masterUrl;
        private final String masterDbUser;
        private final String masterDbClearPassword;

        public ConnectionParams(String masterDriver, String masterUrl, String masterDbUser, String masterDbClearPassword) {
            this.masterDriver = masterDriver;
            this.masterUrl = masterUrl;
            this.masterDbUser = masterDbUser;
            this.masterDbClearPassword = masterDbClearPassword;
        }
    }

    /* odi instance */

    private static int getUserParamConnectionTimeOut() {
        return TimeoutUtils.getTimeoutFromUserParams();
    }

    public static OdiInstanceConfig getOdiInstanceConfig(
            String masterDriver,
            String masterUrl,
            String masterDbUser,
            String masterDbClearPassword
    ) {
        MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(masterUrl, masterDriver, masterDbUser, masterDbClearPassword.toCharArray(), new PoolingAttributes());
        WorkRepositoryDbInfo workInfo = null;

        OdiInstanceConfig odiInstanceConfig = new OdiInstanceConfig(masterInfo, workInfo, getUserParamConnectionTimeOut(), (String)null, true);
        return odiInstanceConfig;
    }

    public static OdiInstance getOdiInstance(ConnectionParams conn) {
        String masterDriver = conn.masterDriver;
        String masterUrl = conn.masterUrl;
        String masterDbUser = conn.masterDbUser;
        String masterDbClearPassword = conn.masterDbClearPassword;

        OdiInstanceConfig odiInstanceConfig = getOdiInstanceConfig(masterDriver, masterUrl, masterDbUser, masterDbClearPassword);
        OdiInstance odiInstance = OdiInstance.createInstance(odiInstanceConfig);

        return odiInstance;
    }

    /* tools */

    public static String encrypt(String password, OdiInstance odiInstance) {
        return DwgObject.snpsEncode(password, odiInstance);
    }

    public static String decrypt(String password, OdiInstance odiInstance) {
        return DwgObject.snpsDecypher(password, odiInstance);
    }

    public static String hash(String password, OdiInstance odiInstance) throws IOException {
        return DwgObject.snpsHashPassword(password, odiInstance);
    }

    public static boolean verify(String password, String hashedPassword, OdiInstance pOdiInstance) throws GeneralSecurityException {
        return CryptoUtil.verifyPassword(password.toCharArray(), hashedPassword.toCharArray(), CipherFactory.getCipherFactory(pOdiInstance.getSecurityManager()).getDefaultOdiCipher());
    }

     /* main */

    public static void main(String[] args) throws Exception {
        /*
        ConnectionParams conn = new ConnectionParams(
                "oracle.jdbc.OracleDriver",
                "jdbc:oracle:thin:@//192.168.1.23:1521/ORCLPDB1.LOCALDOMAIN",
                "DEV_ODI_REPO",
                "qazwsx123"
        );
        */

        ConnectionParams conn = new ConnectionParams(
                "oracle.jdbc.OracleDriver",
                args[0],
                args[1],
                args[2]
        );

        OdiInstance odiInstance = getOdiInstance(conn);

        System.out.println(OdiInstanceTools.encrypt("test", odiInstance));
        System.out.println(OdiInstanceTools.decrypt("YZtFu8yth72YMAFmdbOziA==", odiInstance));

        System.out.println(OdiInstanceTools.hash("qazwsx123", odiInstance));
        System.out.println(OdiInstanceTools.verify("qazwsx123", "yDw58aoKQuJxuPK+F2PUmwLRDFw+BFEiXCcg8rU9sM8gs6Z/yfaQYz6NnzVotr55:B8fye3qYxfEJYjJ1dT7MutAmirQJwKEvAisEhHkIWTY=", odiInstance));
    }
}
