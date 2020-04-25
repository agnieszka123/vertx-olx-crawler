package io.vertx.crawler;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {
    private Vertx vertx;

    private WebClient webClient;

    @Before
    public void setUp(TestContext tc) {
        vertx = Vertx.vertx();
        webClient = WebClient.create(vertx);
        vertx.deployVerticle(new MainVerticle());
    }

    @After
    public void tearDown(TestContext tc) {
        vertx.close(tc.asyncAssertSuccess());
    }

    @Test
    public void testThatTheServerIsStarted(TestContext tc) {
        Async async = tc.async();
        webClient.get(8082, "localhost", "/offers/olx:kotek").send(response -> {
            System.out.println(response);
            tc.assertEquals(response.result().statusCode(), 200);
            tc.assertFalse(response.result().body().toString().contains("myszojeleń"));
            tc.assertTrue(response.result().body().toString().contains("kotek"));
            async.complete();
        });


    }
}
