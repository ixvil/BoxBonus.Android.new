package com.ixvil.android.BoxBonus.Models;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.ixvil.android.BoxBonus.Entities.News;
import com.ixvil.android.BoxBonus.Fragments.FetchSuccessFragmentInterface;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

/**
 * Created by ixvil on 05.01.2017.
 */

public class NewsModel {
    private static News[] news;

    public static void getFromNet(Context applicationContext) {
        getFromNet(applicationContext, null);
    }

    public static void getFromNet(final Context context, final FetchSuccessFragmentInterface fragment) {

        try {
            Ion.with(context)
                    .load(context.getResources().getString(R.string.hostname) + "/json/getnews")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonArray newsJson = result.getAsJsonArray("data");
                                if (newsJson != null) {
                                    if (fragment != null) {
                                        fragment.onFetchSuccess(newsJson);
                                    }
                                    onFetchSuccess(newsJson);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void onFetchFailed(String message, Context context) {
        if (!Objects.equals(message, "")) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private static void onFetchSuccess(JsonArray newsJson) {
        setNews(newsJson);
    }


    public static void setNews(JsonArray newsJson) {
        news = new News[newsJson.size()];
        int i = 0;
        for (JsonElement oneElement : newsJson) {
            JsonObject oneObject = (JsonObject) oneElement;
            News one = (new News()).factory(oneObject);
            news[i] = one;
            i++;
        }
    }

    public static News[] getNews() {
        return news;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static News getNewsById(String id) {
        for (News one : getNews()) {
            if (Objects.equals(String.valueOf(one.getId()), id)) {
                return one;
            }
        }
        return null;
    }
}
