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

package com.ixvil.android.BoxBonus.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ixvil.android.BoxBonus.Activities.DetailActivity;
import com.ixvil.android.BoxBonus.Adapters.AbstractFragmentContentAdapter;
import com.ixvil.android.BoxBonus.Models.Shop;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;

/**
 * Provides UI for the view with Cards.
 *
 * @deprecated TODO: 23.08.2017 refactor and create entity
 */
public class ShopsListFragment extends Fragment {

    private ShopAdapter contentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        contentAdapter = new ShopAdapter(recyclerView.getContext());
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.getAllShops(inflater, container, savedInstanceState);

        return recyclerView;
    }

    class ShopAdapter extends AbstractFragmentContentAdapter<ViewHolder> {

        ShopAdapter(Context context) {
            super(context);
        }

        @Override
        public void updateAdapter(JsonArray jsonObject) throws IOException {
            mGifts = new String[jsonObject.size()];
            mPlaceDesc = new String[jsonObject.size()];
            mGiftsPictures = new String[jsonObject.size()];

            for (int i = 0; i < jsonObject.size(); i++) {
                JsonObject obj = (JsonObject) jsonObject.get(i);
                JsonObject partnerAttributes = (JsonObject) obj.get("attributes");
                mGifts[i] = partnerAttributes.get("name").getAsString();
                mPlaceDesc[i] = partnerAttributes.get("description").getAsString();
                mGiftsPictures[i] = innerContext.getString(R.string.image_url_prefix) + partnerAttributes.get("logo").getAsString();
            }

            setLength(jsonObject.size());
        }

        @Override
        public ShopsListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopsListFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.name.setText(mGifts[position]);
            holder.description.setText(mPlaceDesc[position]);
            Ion.with(holder.picture)
                    .placeholder(R.drawable.a)
                    .load(mGiftsPictures[position]);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button) itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Action is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Added to Favorite",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Share article",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }


    public void getAllShops(final LayoutInflater inflater, final ViewGroup container,
                            Bundle savedInstanceState) {

        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);


        try {
            Ion.with(recyclerView.getContext())
                    .load(inflater.getContext().getResources().getString(R.string.hostname) + "/json/getpartners")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonArray partnersJson = result.getAsJsonArray("data");
                                if (partnersJson != null) {
                                    try {
                                        onFetchSuccess(partnersJson);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {
                                    onFetchFailed(result.get("message").getAsString(), recyclerView.getContext());
                                }
                            } else {
                                onFetchFailed(e.getMessage().toString(), recyclerView.getContext());
                            }
                        }
                    });
        } catch (Exception e) {
            onFetchFailed(e.getMessage().toString(), recyclerView.getContext());
        }

    }

    private void onFetchSuccess(JsonArray jsonObject) throws IOException {
        Shop.shops = jsonObject;
        contentAdapter.updateAdapter(jsonObject);
        contentAdapter.notifyDataSetChanged();
    }

    private void onFetchFailed(String message, Context context) {
        if (message != "") {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
