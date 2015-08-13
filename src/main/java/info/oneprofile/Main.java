package info.oneprofile;

import info.oneprofile.web.Server;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private String webserverDeploymentId;

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Main(), ret -> {
            logger.debug("({}).Your verticle be deployed: [{}]", ret.succeeded(), ret.result());
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.deployVerticle(new Server(), ret -> {
            webserverDeploymentId = ret.result();
            logger.info("[{}] - Web server deployed: {}", ret.succeeded(), webserverDeploymentId);

            if (ret.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.failed();
            }
        });

        registerShutdownHandler(vertx);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        vertx.undeploy(webserverDeploymentId, ret -> {
            logger.info("Web server be un-deployed: {}", ret.succeeded());

            if (ret.succeeded()) {
                stopFuture.complete();
            } else {
                stopFuture.failed();
            }
        });
    }

    private void registerShutdownHandler(final Vertx vertx) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                final CountDownLatch cdl = new CountDownLatch(1);

                vertx.close(ar -> {
                    if (ar.succeeded()) {
                        logger.debug("Your vert.x application is stopped!");
                    } else {
                        logger.debug("Failure in stopping vert.x application.");
                    }
                    cdl.countDown();
                });


                try {
                    if (!cdl.await(2, TimeUnit.MINUTES)) {
                        logger.error("Timed out waiting to un-deploy all.");
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

}
