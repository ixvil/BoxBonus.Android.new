package com.example.android.materialdesigncodelab.Models;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.Fragments.FetchSuccessFragmentInterface;
import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

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

    public static JsonObject getGiftById(String id) {
        for (JsonArray giftsArray : gifts) {
            if (giftsArray != null) {
                for (JsonElement element : giftsArray) {
                    JsonObject object = (JsonObject) element;
                    if (object.get("id").getAsString().equals(id)) {
                        return object;
                    }
                }
            }
        }
        return null;
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

    public static void getFromNet(final Context context) {
        getFromNet(context, null);
    }

    public static void getFromNet(final Context context, final FetchSuccessFragmentInterface fragment) {

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
                                    if (fragment != null) {
                                        fragment.onFetchSuccess(giftsJson);
                                    }
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void onFetchFailed(String message, Context context) {
        if (!Objects.equals(message, "")) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
