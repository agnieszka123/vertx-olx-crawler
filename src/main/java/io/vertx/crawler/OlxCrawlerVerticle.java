package io.vertx.crawler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import io.vertx.api.ResponseStructureDO;

public class OlxCrawlerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        vertx.eventBus().consumer("olxCrawler", message -> {
            ResponseStructureDO response = new ResponseStructureDO();
            String keyword = message.body().toString();

            keyword = this.replacePolishCharacters(keyword.toLowerCase());

            WebClientOptions options = new WebClientOptions()
                    .setConnectTimeout(300)
                    .setIdleTimeout(10)
                    .setMaxPoolSize(100)
                    .setSsl(true)
                    .setTrustAll(true)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.113 Safari/537.36");

            String searchParam = "/oferty/q-" + keyword + "/?spellchecker=off";
            WebClient client = WebClient.create(vertx, options);
            client.get(443, "www.olx.pl", searchParam)
                    .putHeader("Connection", "keep-alive")
                    .putHeader("Content-Type", "application/json")
                    .putHeader("Cookie", "mobile_default=desktop")
                    .putHeader("Cache-Control", "no cache")
                    .send(httpResponseAsyncResult -> {
                        if (httpResponseAsyncResult.succeeded()) {
                            Document doc = Jsoup.parse(httpResponseAsyncResult.result().body().toString());
                            Elements elements = doc.getElementsByClass("offer");
                            elements.forEach(element -> {
                                Elements info = element.getElementsByTag("strong");
                                if (info.isEmpty()) return;
                                String name = element.getElementsByTag("strong").get(0).text();
                                String id = element.getElementsByAttribute("data-id").attr("data-id");
                                String price = info.size() == 2 ? element.getElementsByTag("strong").get(1).text() : "empty";
                                price = price.equals("Za darmo") ? "For free" : price;
                                response.addElement(id, name, price);
                            });
                        }
                        message.reply(Json.encodePrettily(response));
                    });
        });
    }

    private String replacePolishCharacters(String word) {
        return
                word.replaceAll("\u0142", "%C5%82")//ł
                        .replaceAll("\u0105", "%C4%85")//ą
                        .replaceAll("\u0144", "%C5%84")//ń
                        .replaceAll("\u015B", "%C5%9B")//ś
                        .replaceAll("\u0118", "%C4%99")//ę
                        .replaceAll("\u017C", "%C5%BC")//ż
                        .replaceAll("\u0107", "%C4%87")//ć
                        .replaceAll("\u017A", "%C5%BA")//ź
                        .replaceAll("\u00F3", "%C3%B3");//ó

    }


}
