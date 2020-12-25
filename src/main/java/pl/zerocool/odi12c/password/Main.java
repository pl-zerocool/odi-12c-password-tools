package pl.zerocool.odi12c.password;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;

import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 */
public class Main {
    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Gets options parser.
     *
     * @param options options
     * @return a options parser
     */
    private static CmdLineParser getParser(Options options) {
        ParserProperties parserProperties = ParserProperties.defaults();
        parserProperties.withOptionValueDelimiter("=");
        parserProperties.withUsageWidth(80);
        return new CmdLineParser(options, parserProperties);
    }

    private static Options parseArguments(String[] args) {
        Options options = new Options();
        CmdLineParser parser = getParser(options);

        try {
            parser.parseArgument(args);
        } catch(CmdLineException e) {
            System.out.println(e.getMessage() + "!");
            System.out.println("See '-help'.");
            System.exit(0);
        }

        if(options.help) {
            printHelp(parser);
            System.exit(0);
        }

        if(!options.validate()) {
            System.out.println("Invalid parameter value!");
            System.out.println("See '-help'.");
            System.exit(0);
        }

        return options;
    }

    private static void setUpLogger(Options options) {
        Level level;
        if(options.logLevel == null) {
            level = Level.OFF;
        } else {
            level = Level.parse(options.logLevel.toUpperCase(Locale.ENGLISH));
        }

        Logger root = Logger.getLogger("");
        root.setLevel(level);
        for(Handler handler : root.getHandlers()) {
            handler.setLevel(level);
        }

        LOGGER.config("logging configured");

        /*
        LOGGER.severe("test");
        LOGGER.warning("test");
        LOGGER.info("test");
        LOGGER.config("test");
        LOGGER.fine("test");
        LOGGER.finer("test");
        LOGGER.finest("test");
        */
    }

    /**
     * Main entry.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Options opts = parseArguments(args);
        setUpLogger(opts);

        /* program flow */

        try {
            LOGGER.info("arguments parsed");
            LOGGER.info("cmd=" + opts.cmd);
            LOGGER.info("password=" + opts.password);
            LOGGER.info("salt=" + opts.salt);
            LOGGER.info("hPassword=" + opts.hPassword);
            LOGGER.info("encAlgo=" + opts.encAlgo);
            LOGGER.info("encKeyLen=" + opts.encKeyLen);
            LOGGER.info("encKey=" + opts.encKey);
            LOGGER.info("encIV=" + opts.encIV);
            LOGGER.info("encProperties=" + opts.encProperties);
            LOGGER.info("logLevel=" + opts.logLevel);

            Command command = new Command(opts);
            command.run();
        } catch(Exception e) {
            System.out.println("Critical error occurred: " + e);
            return;
        }
    }

    /**
     * Prints help.
     *
     * @param parser options parser
     */
    private static void printHelp(CmdLineParser parser) {
        System.out.println("ODI tools");
        System.out.println("In order to obtain encryption parameters run 'select ENC_ALG, ENC_KEY_LEN, ENC_KEY, ENC_IV from SNP_LOC_REP' in ODI REPO.");
        System.out.println();
        System.out.println("Usage:");
        parser.printUsage(System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("[java invocation] -help");
        System.out.println("[java invocation] -cmd=\"encode\" -password=\"test\"");
        System.out.println("[java invocation] -cmd=\"decode\" -password=\"dTyaXMpB1FwOG6.iIsGu\"");
        System.out.println("[java invocation] -cmd=\"hashWithSalt\" -password=\"test\" -salt=\"3pCaxQ3Y0OnivH1HgWenP8OjUivgjDOy/dVU02ZIfPU=\"");
        System.out.println("[java invocation] -cmd=\"encrypt\" -password=\"test\" -encAlgo=\"AES\" -encKeyLen=\"128\" -encKey=\"aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw\" -encIV=\"zcL89phw9gp5QwoOA7H+6w==\"");
        System.out.println("[java invocation] -cmd=\"decrypt\" -password=\"YZtFu8yth72YMAFmdbOziA==\" -encAlgo=\"AES\" -encKeyLen=\"128\" -encKey=\"aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw\" -encIV=\"zcL89phw9gp5QwoOA7H+6w==\"");
        System.out.println("[java invocation] -cmd=\"hash\" -password=\"test\" -encAlgo=\"AES\" -encKeyLen=\"128\" -encKey=\"aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw\" -encIV=\"zcL89phw9gp5QwoOA7H+6w==\"");
        System.out.println("[java invocation] -cmd=\"decryptSalt\" -password=\"Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=\" -encAlgo=\"AES\" -encKeyLen=\"128\" -encKey=\"aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw\" -encIV=\"zcL89phw9gp5QwoOA7H+6w==\"");
        System.out.println("[java invocation] -cmd=\"verify\" -password=\"test\" -hPassword=\"Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=\" -encAlgo=\"AES\" -encKeyLen=\"128\" -encKey=\"aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw\" -encIV=\"zcL89phw9gp5QwoOA7H+6w==\"");
        System.out.println("[java invocation] -cmd=\"encrypt\" -password=\"test\" -encProperties=\"enc.properties.example\"");
    }
}