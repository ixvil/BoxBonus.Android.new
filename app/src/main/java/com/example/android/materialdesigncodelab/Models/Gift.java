package com.example.android.materialdesigncodelab.Models;

import android.content.Context;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;

/**
 * Created by ixvil on 02.01.2017.
 */

public class Gift {
    public static JsonArray[] gifts;

    public static void setGifts(JsonArray jsonGifts) {

        if (Shop.shops != null) {
            gifts = new JsonArray[Shop.shops.size() * 5];
            //TODO:: really full fun
        } else {
            gifts = new JsonArray[1000];
            //TODO:: What a fun ?
        }

        for (int i = 0; i < jsonGifts.size(); i++) {
            JsonObject gift = (JsonObject) jsonGifts.get(i);
            JsonObject giftAttributes = (JsonObject) gift.get("attributes");
            JsonObject partner = (JsonObject) giftAttributes.get("partner");
            int partnerId = partner.get("id").getAsInt();
            if (gifts[partnerId] == null) {
                gifts[partnerId] = new JsonArray();
            }
            gifts[partnerId].add(gift);
        }


    }


    public static JsonArray getGiftsForShop(int id) {
        if (null == gifts) {
            return null;
        }
        if (null == gifts[id]) {
            return null;
        }

        return gifts[id];
    }

    public static void getGiftsFromNet(final Context context) {

        try {
            Ion.with(context)
                    .load(context.getResources().getString(R.string.hostname) + "/json/getgifts")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonArray giftsJson = result.getAsJsonArray("data");
                                if (giftsJson != null) {
                                    onFetchSuccess(giftsJson);
                                } else {
                                    onFetchFailed(result.get("message").getAsString(), context);
                                }
                            } else {
                                onFetchFailed(e.getMessage(), context);
                            }
                        }
                    });
        } catch (Exception e) {
            onFetchFailed(e.getMessage(), context);
        }
    }

    private static void onFetchSuccess(JsonArray giftsJson) {
        Gift.setGifts(giftsJson);
    }

    private static void onFetchFailed(String message, Context context) {
        if (message != "") {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
