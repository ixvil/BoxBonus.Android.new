package com.ixvil.android.BoxBonus.Entities;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * Created by ixvil on 05.01.2017.
 */

public class News extends AbstractEntity<News> {
    private String name;
    private String description;
    private String text;
    private String logo;

    /**
     * Factory for News entity
     *
     * @param oneObject
     * @return News
     */
    @Override
    public News factory(JsonObject oneObject) {
        News one = new News();

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

        String text;
        if (attributes.get("text") instanceof JsonNull) {
            text = "";
        } else {
            text = attributes.get("text").getAsString();
        }
        one.setText(text);

        String logo;
        if (attributes.get("logo") instanceof JsonNull) {
            logo = "";
        } else {
            logo = attributes.get("logo").getAsString();
        }
        one.setLogo(logo);


        return one;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name Имя
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
