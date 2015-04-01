package com.protoalliance.vindiniumclient.bot.proto;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.dto.ApiKey;
import com.protoalliance.vindiniumclient.dto.GameState;
import com.protoalliance.vindiniumclient.dto.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class ProtoBotRunner implements Callable<GameState> {
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
    private static final Logger logger = LogManager.getLogger(ProtoBotRunner.class);

    private final ApiKey apiKey;
    private final GenericUrl gameUrl;
    private final ProtoBot bot;

    public ProtoBotRunner(ApiKey apiKey, GenericUrl gameUrl, ProtoBot bot) {
        this.apiKey = apiKey;
        this.gameUrl = gameUrl;
        this.bot = bot;
    }

    @Override
    public GameState call() throws Exception {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState = null;
        ProtoGameState protoGameState;

        try {
            // Initial request
            logger.info("Sending initial request...");
            content = new UrlEncodedContent(apiKey);
            request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
            request.setReadTimeout(0); // Wait forever to be assigned to a game
            response = request.execute();
            gameState = response.parseAs(GameState.class);
            logger.info("Game URL: {", gameState.getViewUrl() + "}");
            System.out.println("Game URL: {" + gameState.getViewUrl() + "}");
            protoGameState = new ProtoGameState(gameState);

            // Game loop
            while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {

                logger.info("Taking turn " + gameState.getGame().getTurn());
                BotMove direction = bot.move(protoGameState);
                Move move = new Move(apiKey.getKey(), direction.toString());


                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);
                protoGameState = new ProtoGameState(protoGameState, gameState);
            }

        } catch (Exception e) {
            logger.error("Error during game play", e);
            e.printStackTrace();
        }

        logger.info("Game over");
        return gameState;
    }
}