package pl.zerocool.odi12c.password.unused;

import oracle.odi.commandline.support.OdiParams;
import oracle.odi.commandline.support.ParsingUtil;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.OdiConfigurationException;
import oracle.odi.param.CliParameterReader;
import oracle.odi.param.ParameterHelper;
import org.apache.commons.cli.ParseException;

/**
 * Class based on oracle.odi.Encode (odi-standalone-agent).
 * Displays connection parameters from local ODI agent.
 *
 * Usage:
 *  cd <ORACLE_HOME>/user_projects/domains/odi_agent/bin
 *  edit 'encode.sh':
 *      ${ODI_JAVA_START} -DLOG_FILE=encode.log -classpath "${ODI_CLASSPATH}:." pl.zerocool.odi12c.password.unused.PrintAgentProperties ${ODI_REPOSITORY_PARAMS} "$@"
 *  run './encode.sh -INSTANCE=OracleDIAgent1'
 *
 *  '${DOMAIN_HOME}/bin/setODIDomainEnv.sh' in 'encode.sh' sets variables such as 'oracle.odi.home', 'domain.home' and classpath making it possible to read local connection parameters.
 */
public class PrintAgentProperties {
    private static void printProperties() throws ParsingUtil.CommandLineParsingException {
        String masterDriver = OdiParams.getMasterDbJdbcDriver();
        String masterUrl = OdiParams.getMasterDbJdbcUrl();
        String masterDbUser = OdiParams.getMasterDbJdbcUser();
        String masterDbClearPassword = OdiParams.getMasterDbJdbcPassword();
        String workRepName = OdiParams.getWorkRepositoryName();
        int retryCount = OdiParams.getReposRetryCount();
        int retryDelay = OdiParams.getReposRetryDelay();

        System.out.println("masterDriver=" + masterDriver);
        System.out.println("masterUrl=" + masterUrl);
        System.out.println("masterDbUser=" + masterDbUser);
        System.out.println("masterDbClearPassword=" + masterDbClearPassword);
        System.out.println("workRepName=" + workRepName);
        System.out.println("retryCount=" + retryCount);
        System.out.println("retryDelay=" + retryDelay);
    }

    private static OdiInstance getOdiInstance(String[] args) throws Exception {
        OdiInstance odiInstance = null;
        try {
            CliParameterReader pReader = new CliParameterReader(args);
            OdiParams.setReader(pReader);
        } catch (ParseException e1) {
            throw new RuntimeException(e1);
        }
        try {
            odiInstance = OdiInstance.createInstance(OdiParams.getOdiInstanceConfig(false));

            // print properties
            printProperties();
        } catch (OdiConfigurationException cex) {
            throw cex;
        } catch (oracle.odi.commandline.support.ParsingUtil.CommandLineParsingException e) {
            throw e;
        }
        return odiInstance;
    }

    public static void main(String[] args) {
        OdiInstance odiInstance = null;
        if (ParameterHelper.isUsage(args)) {
            ParameterHelper.usage(ParameterHelper.HelpKey.encode);
            System.exit(0);
        }
        try {
            odiInstance = getOdiInstance(args);
            // [...]
        } catch (Throwable exc) {
            exc.printStackTrace();
            ParameterHelper.usage(ParameterHelper.HelpKey.encode);
            System.exit(1);
        } finally {
            if (odiInstance != null)
                odiInstance.close();
        }
    }
}
