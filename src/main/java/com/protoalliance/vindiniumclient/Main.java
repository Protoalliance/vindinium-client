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
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Logger gameStateLogger = LogManager.getLogger("gameStateLogger");

    public static void main(String args[]) throws Exception {

        final String key = args[0];
        final String arena = args[1];
        final String botType = args[2];
        final String botClass = args[3];

        final GenericUrl gameUrl;

        if ("TRAINING".equals(arena))
            gameUrl = VindiniumUrl.getTrainingUrl();
        else if ("COMPETITION".equals(arena))
            gameUrl = VindiniumUrl.getCompetitionUrl();
        else
            gameUrl = new VindiniumUrl(arena);


        switch(botType) {
            case "simple":
                runSimpleBot(key, gameUrl, botClass);
                break;
            case "advanced":
                runAdvancedBot(key, gameUrl, botClass);
                break;
            case "proto":
                runProtoBot(key, gameUrl, botClass);
                break;
            default:
                throw new RuntimeException("The bot type must be simple or advanced and must match the type of the bot.");
        }
    }

    private static void runProtoBot(String key, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends ProtoBot> botClazz = clazz.asSubclass(ProtoBot.class);
        ProtoBot bot = botClazz.newInstance();
        ApiKey apiKey = new ApiKey(key);
        ProtoBotRunner runner = new ProtoBotRunner(apiKey, gameUrl, bot);
        runner.call();
    }


    private static void runAdvancedBot(String key, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends AdvancedBot> botClazz = clazz.asSubclass(AdvancedBot.class);
        AdvancedBot bot = botClazz.newInstance();
        ApiKey apiKey = new ApiKey(key);
        AdvancedBotRunner runner = new AdvancedBotRunner(apiKey, gameUrl, bot);
        runner.call();
    }
    private static void runSimpleBot(String key, GenericUrl gameUrl, String botClass) throws Exception {
        Class<?> clazz = Class.forName(botClass);
        Class<? extends SimpleBot> botClazz = clazz.asSubclass(SimpleBot.class);
        SimpleBot bot = botClazz.newInstance();
        ApiKey apiKey = new ApiKey(key);
        SimpleBotRunner runner = new SimpleBotRunner(apiKey, gameUrl, bot);
        runner.call();
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