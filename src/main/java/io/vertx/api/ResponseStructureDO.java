package io.vertx.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponseStructureDO {

    List<ResponseElement> offers = new ArrayList<>();

    public void addElement(String id, String name, String price) {
        offers.add(new ResponseElement(id, name, price));

    }

    public List<ResponseElement> getOffers() {
        return Collections.unmodifiableList(offers);
    }

    public void setOffers(final List<ResponseElement> offers) {
        this.offers = offers;
    }


    private class ResponseElement {

        String id;

        String name;

        String price;

        ResponseElement(String id, String name, String price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "ResponseElement{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }
}
