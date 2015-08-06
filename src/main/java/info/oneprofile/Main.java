package info.oneprofile;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args){
        Vertx.vertx().deployVerticle(new Main(), ret -> {
            //System.out.println("Your verticle be deployed Id:" + ret.result());
            logger.debug("({}).Your verticle be deployed: [{}]", ret.succeeded(), ret.result());

            io.vertx.core.logging.Logger logger2 = io.vertx.core.logging.LoggerFactory.getLogger(Main.class);
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}
