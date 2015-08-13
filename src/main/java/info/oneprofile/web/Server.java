package info.oneprofile.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final boolean IN_DEVELOPMENT_MODE = System.getProperty("app.development") != null;

    private HttpServer server;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        startWebServer();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
        stopWebServer();
    }

    private void startWebServer() {
        Router router = Router.router(vertx);

        router.route().handler(buildStaticHandler());

//        router.route().handler(routingContext -> {
//            routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
//        });

        int port = 8000;
        vertx.createHttpServer().requestHandler(router::accept).listen(port, ret -> {
            logger.info("web server running on {}", port);
            server = ret.result();
        });
    }

    private void stopWebServer() {
        server.close(ret -> {
            logger.info("web server be shutdown: {}", ret.succeeded());
        });
    }

    private StaticHandler buildStaticHandler() {
        StaticHandler handler = StaticHandler.create();
        handler.setIndexPage("index.html");

        handler.setIncludeHidden(false);
        handler.setFilesReadOnly(false);
        handler.setCachingEnabled(false);

        if (IN_DEVELOPMENT_MODE) {
            handler.setWebRoot("src/main/resources/webroot");
        }

        return handler;
    }

}
