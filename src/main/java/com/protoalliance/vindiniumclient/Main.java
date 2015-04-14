package com.protoalliance.vindiniumclient;

import com.protoalliance.vindiniumclient.bot.advanced.AdvancedBot;
import com.protoalliance.vindiniumclient.bot.advanced.AdvancedBotRunner;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBotRunner;
import com.protoalliance.vindiniumclient.bot.simple.SimpleBot;
import com.protoalliance.vindiniumclient.bot.simple.SimpleBotRunner;
import com.protoalliance.vindiniumclient.dto.ApiKey;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CLI program for launching a bot
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final int NUM_RUNS = 1;

    public static void main(String args[]) throws Exception {

        final String key = args[0];
        final String arena = args[1];
        String map = args[2];
        String botType = args[2];
        String botClass = args[3];

        //Move botType and botClass over one if a map parameter is specified
        if (map.equals("m1") || map.equals("m2") || map.equals("m3") ||
                map.equals("m4") || map.equals("m5") || map.equals("m6") || map.equals("m*")) {
            botType = args[3];
            botClass = args[4];
        }

        final GenericUrl gameUrl;

        if ("TRAINING".equals(arena)) {
            gameUrl = VindiniumUrl.getTrainingUrl();
        }
        else if ("COMPETITION".equals(arena)) {
            gameUrl = VindiniumUrl.getCompetitionUrl();
        }
        else {
            gameUrl = new VindiniumUrl(arena);
        }

        for (int i = 0; i < NUM_RUNS; i++) {
            System.out.println("Beginning run " + (i + 1) + " of " + NUM_RUNS);
            logger.info("Beginning run " + (i + 1) + " of " + NUM_RUNS);
            switch (botType) {
                case "simple":
                    runSimpleBot(key, map, gameUrl, botClass);
                    break;
                case "advanced":
                    runAdvancedBot(key, map, gameUrl, botClass);
                    break;
                case "proto":
                    runProtoBot(key, map, gameUrl, botClass);
                    break;
                default:
                    throw new RuntimeException("The bot type must be simple or advanced and must match the type of the bot.");
            }
        }
    }

    private static void runProtoBot(String key, String map, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends ProtoBot> botClazz = clazz.asSubclass(ProtoBot.class);
        ProtoBot bot = botClazz.newInstance();

        ApiKey apiKey;

        //Ex. no map specified
        if (map.equals("m1") || map.equals("m2") || map.equals("m3") ||
                map.equals("m4") || map.equals("m5") || map.equals("m6")) { //Load specified map
            System.out.println("Loading specified map " + map + "...");
            apiKey = new ApiKey(key, map);
            ProtoBotRunner runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }  else if (!map.equals("m*")){ //Ex. m1, m2, m3, m4, m5, m6
            System.out.println("Loading random map...");
            apiKey = new ApiKey(key, "");
            ProtoBotRunner runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();
        } else { //m*
            System.out.println("Loading all maps...");
            apiKey = new ApiKey(key, "m1");
            ProtoBotRunner runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m2");
            runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m3");
            runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m4");
            runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m5");
            runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m6");
            runner = new ProtoBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }


    }

    private static void runAdvancedBot(String key, String map, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends AdvancedBot> botClazz = clazz.asSubclass(AdvancedBot.class);
        AdvancedBot bot = botClazz.newInstance();
        ApiKey apiKey;

        //Ex. no map specified
        if (map.equals("m1") || map.equals("m2") || map.equals("m3") ||
                map.equals("m4") || map.equals("m5") || map.equals("m6")) { //Load specified map
            System.out.println("Loading specified map " + map + "...");
            apiKey = new ApiKey(key, map);
            AdvancedBotRunner runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }  else if (!map.equals("m*")){ //Ex. m1, m2, m3, m4, m5, m6
            System.out.println("Loading random map...");
            apiKey = new ApiKey(key, "");
            AdvancedBotRunner runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();
        } else { //m*
            System.out.println("Loading all maps...");
            apiKey = new ApiKey(key, "m1");
            AdvancedBotRunner runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m2");
            runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m3");
            runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m4");
            runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m5");
            runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m6");
            runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }
    }
    private static void runSimpleBot(String key, String map, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends SimpleBot> botClazz = clazz.asSubclass(SimpleBot.class);
        SimpleBot bot = botClazz.newInstance();
        ApiKey apiKey;

        //Ex. no map specified
        if (map.equals("m1") || map.equals("m2") || map.equals("m3") ||
                map.equals("m4") || map.equals("m5") || map.equals("m6")) { //Load specified map
            System.out.println("Loading specified map " + map + "...");
            apiKey = new ApiKey(key, map);
            SimpleBotRunner runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }  else if (!map.equals("m*")){ //Ex. m1, m2, m3, m4, m5, m6
            System.out.println("Loading random map...");
            apiKey = new ApiKey(key, "");
            SimpleBotRunner runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();
        } else { //m*
            System.out.println("Loading all maps...");
            apiKey = new ApiKey(key, "m1");
            SimpleBotRunner runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m2");
            runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m3");
            runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m4");
            runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m5");
            runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();

            apiKey = new ApiKey(key, "m6");
            runner = new SimpleBotRunner(apiKey, gameUrl, bot);
            runner.call();
        }
    }

    /**
     * Represents the endpoint URL
     */
    public static class VindiniumUrl extends GenericUrl {
        private final static String TRAINING_URL = "http://vindinium.org/api/training";
        private final static String COMPETITION_URL = "http://vindinium.org/api/arena";

        public VindiniumUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public static VindiniumUrl getCompetitionUrl() {
            return new VindiniumUrl(COMPETITION_URL);
        }

        public static VindiniumUrl getTrainingUrl() {
            return new VindiniumUrl(TRAINING_URL);
        }
    }
}
