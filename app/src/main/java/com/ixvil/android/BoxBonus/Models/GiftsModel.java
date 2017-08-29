package com.ixvil.android.BoxBonus.Models;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.ixvil.android.BoxBonus.Entities.Gift;
import com.ixvil.android.BoxBonus.Entities.News;
import com.ixvil.android.BoxBonus.Fragments.FetchSuccessFragmentInterface;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ixvil on 02.01.2017.
 */

public class GiftsModel extends AbstractModel {

    public static void setEntities(JsonArray giftsJson) {
        entities = new Gift[giftsJson.size()];
        int i = 0;
        for (JsonElement oneElement : giftsJson) {
            JsonObject oneObject = (JsonObject) oneElement;
            Gift one = (new Gift()).factory(oneObject);
            entities[i] = one;
            i++;
        }
    }

    public static Gift getGiftById(String id) {
        for (Gift gift : (Gift[]) entities) {
            if (gift != null) {
                if (gift.getId() == Integer.parseInt(id)) {
                    return gift;
                }
            }
        }
        return null;
    }

    public static ArrayList<Gift> getGiftByPartnerId(int partnerId) {
        ArrayList<Gift> gifts = new ArrayList<>();

        for (Gift gift : (Gift[]) entities) {
            if (gift != null) {
                if (gift.getPartnerId() == partnerId) {
                    gifts.add(gift);
                }
            }
        }
        return gifts;
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
        GiftsModel.setEntities(giftsJson);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void onFetchFailed(String message, Context context) {
        if (!Objects.equals(message, "")) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
