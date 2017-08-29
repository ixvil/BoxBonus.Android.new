package com.ixvil.android.BoxBonus.Entities;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * Created by ixvil on 23.08.2017.
 */

public class Gift extends AbstractEntity<Gift> {

    private String name;
    private String description;
    private String logo;
    private int price;
    private int partnerId;

    @Override
    public Gift factory(JsonObject oneObject) {

        Gift one = new Gift();

        if (oneObject.get("id") instanceof JsonNull) {
            return null;
        }
        int id = oneObject.get("id").getAsInt();
        one.setId(id);

        JsonObject attributes = oneObject.get("attributes").getAsJsonObject();
        String newsName;
        if (attributes.get("name") instanceof JsonNull) {
            newsName = "";
        } else {
            newsName = attributes.get("name").getAsString();
        }
        one.setName(newsName);

        String description;
        if (attributes.get("description") instanceof JsonNull) {
            description = "";
        } else {
            description = attributes.get("description").getAsString();
        }
        one.setDescription(description);

        String logo;
        if (attributes.get("logo") instanceof JsonNull) {
            logo = "";
        } else {
            logo = attributes.get("logo").getAsString();
        }
        one.setLogo(logo);

        int partner;
        if (attributes.get("partner") instanceof JsonNull) {
            partner = 0;
        } else {
            JsonObject partnerJson = attributes.get("partner").getAsJsonObject();
            if(partnerJson.get("id") instanceof JsonNull){
                partner = 0;
            } else {
                partner = partnerJson.get("id").getAsInt();
            }
        }
        one.setPartnerId(partner);

        int price;
        if (attributes.get("price") instanceof JsonNull) {
            price = 0;
        } else {
            price  = attributes.get("price").getAsInt();
        }
        one.setPrice(price);


        return one;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
