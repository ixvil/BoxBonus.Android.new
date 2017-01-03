/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialdesigncodelab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.Activities.DetailActivity;
import com.example.android.materialdesigncodelab.Activities.GiftDetailActivity;
import com.example.android.materialdesigncodelab.Adapters.AbstractFragmentContentAdapter;
import com.example.android.materialdesigncodelab.Models.Gift;
import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

/**
 * Provides UI for the view with Tile.
 */
public class GiftsListFragment extends Fragment implements Gift.FragmentInterface {

    private GiftAdapter contentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        contentAdapter = new GiftAdapter(recyclerView.getContext());
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Gift.getFromNet(inflater.getContext(), this);

        return recyclerView;
    }

    @Override
    public void onFetchSuccess(JsonArray jsonArray) {
        Gift.setGifts(jsonArray);
        contentAdapter.updateAdapter(jsonArray);
        contentAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView id;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
            id = (TextView) itemView.findViewById(R.id.tile_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GiftDetailActivity.class);
                    intent.putExtra(GiftDetailActivity.EXTRA_POSITION, id.getText());
                    context.startActivity(intent);
                }
            });
        }
    }

    class GiftAdapter extends AbstractFragmentContentAdapter<ViewHolder> {

        GiftAdapter(Context context) {
            super(context);
        }

        @Override
        public void updateAdapter(JsonArray jsonObject) {
            mIds = new String[jsonObject.size()];
            mGifts = new String[jsonObject.size()];
            mGiftsPictures = new String[jsonObject.size()];

            for (int i = 0; i < jsonObject.size(); i++) {
                JsonObject obj = (JsonObject) jsonObject.get(i);
                JsonObject partnerAttributes = (JsonObject) obj.get("attributes");
                mGifts[i] = partnerAttributes.get("name").getAsString();
                mIds[i] = obj.get("id").getAsString();

                if (partnerAttributes.get("logo") instanceof JsonNull) {
                    mGiftsPictures[i] = null;
                } else {
                    mGiftsPictures[i] = innerContext.getString(R.string.image_url_prefix) + partnerAttributes.get("logo").getAsString();
                }
            }

            setLength(jsonObject.size());
        }

        @Override
        public GiftsListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GiftsListFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(mGifts[position]);
            Ion.with(holder.picture)
                    .placeholder(R.drawable.a)
                    .load(mGiftsPictures[position]);
            holder.id.setText(mIds[position]);
        }
    }
}