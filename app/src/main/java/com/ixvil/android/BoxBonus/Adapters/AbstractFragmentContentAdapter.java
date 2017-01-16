package com.ixvil.android.BoxBonus.Adapters;

/**
 * Created by ixvil on 17.12.2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;

import java.io.IOException;

/**
 * Adapter to display recycler view.
 */
public abstract class AbstractFragmentContentAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    // Set numbers of Card in RecyclerView.
    private int length;

    protected String[] mIds;
    protected String[] mGifts;
    protected String[] mPlaceDesc;
    protected String[] mGiftsPictures;
    protected String[] mDescriptions;
    protected final Context innerContext;


    protected AbstractFragmentContentAdapter(Context context) {
        innerContext = context;

        Resources resources = innerContext.getResources();
        setLength(0);

    }

    abstract public void updateAdapter(JsonArray jsonObject) throws IOException;



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