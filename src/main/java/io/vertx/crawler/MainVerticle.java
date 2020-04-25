package io.vertx.crawler;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
    public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";


    @Override
    public void init(Vertx vertx, Context context) {
        vertx = Vertx.vertx(new VertxOptions().setAddressResolverOptions(null));
        super.init(vertx, context);
    }

    @Override
    public void start(Future<Void> future) {
        vertx.deployVerticle(new OlxCrawlerVerticle());

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/offers/olx:keyword").handler(this::crawlOlx);

        int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 8082);
        server
                .requestHandler(router)
                .listen(portNumber, ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("HTTP server running on port " + portNumber);
                        future.complete();
                    } else {
                        LOGGER.error("Could not start a HTTP server", ar.cause());
                        future.fail(ar.cause());
                    }
                });

    }

    private void crawlOlx(RoutingContext routingContext) {
        String keyword = routingContext.request().getParam("keyword");

        vertx.eventBus().request("olxCrawler", keyword, reply -> {
            if (reply.succeeded()) {
                routingContext.response()
                        .putHeader("Content-type", "application/json; charset=UTF-8").end(reply.result().body().toString());
            }
        });
    }


}
