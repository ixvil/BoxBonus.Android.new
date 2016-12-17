package com.example.android.materialdesigncodelab.Adapters;

/**
 * Created by ixvil on 17.12.2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Adapter to display recycler view.
 */
public abstract class AbstractShopAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    // Set numbers of Card in RecyclerView.
    int length;

    protected String[] mPlaces;
    protected String[] mPlaceDesc;
    protected String[] mPlacePictures;
    protected final Context innerContext;


    protected AbstractShopAdapter(Context context) {
        innerContext = context;

        Resources resources = innerContext.getResources();
        setLength(0);

    }

    public void updateAdapter(JsonArray jsonObject) throws IOException {
        mPlaces = new String[jsonObject.size()];
        mPlaceDesc = new String[jsonObject.size()];
        mPlacePictures = new String[jsonObject.size()];

        for (int i = 0; i < jsonObject.size(); i++) {
            JsonObject obj = (JsonObject) jsonObject.get(i);
            JsonObject partnerAttributes = (JsonObject) obj.get("attributes");
            mPlaces[i] = partnerAttributes.get("name").getAsString();
            mPlaceDesc[i] = partnerAttributes.get("description").getAsString();
            mPlacePictures[i] = innerContext.getString(R.string.image_url_prefix) + partnerAttributes.get("logo").getAsString();
        }

        setLength(jsonObject.size());
    }


    @Override
    public int getItemCount() {
        return getLength();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


}