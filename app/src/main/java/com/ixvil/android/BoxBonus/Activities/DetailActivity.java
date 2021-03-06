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

package com.ixvil.android.BoxBonus.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ixvil.android.BoxBonus.Entities.Gift;
import com.ixvil.android.BoxBonus.Models.GiftsModel;
import com.ixvil.android.BoxBonus.Models.Shop;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_MODEL = "shops";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        if (Shop.shops == null) {
            throw new RuntimeException("No shops");
        }

        JsonObject obj = (JsonObject) Shop.shops.get(position);
        fillShop(collapsingToolbar, obj);


    }

    private void fillShop(CollapsingToolbarLayout collapsingToolbar, JsonObject obj) {
        if (obj == null) {
            throw new RuntimeException("No such shop");
        }

        JsonObject partnerAttributes = (JsonObject) obj.get("attributes");

        Resources resources = getResources();
        collapsingToolbar.setTitle(partnerAttributes.get("name").getAsString());


        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(partnerAttributes.get("description").getAsString());


        TextView placeLocation = (TextView) findViewById(R.id.place_location);
        placeLocation.setText(partnerAttributes.get("location").getAsString());

        ImageView placePicture = (ImageView) findViewById(R.id.image);
        Ion.with(placePicture)
                .placeholder(R.drawable.a)
                .load(getApplicationContext().getString(R.string.image_url_prefix)
                        + partnerAttributes.get("logo").getAsString()
                );

        int id = (int) obj.get("id").getAsInt();

        getGifts(id);

    }

    private void getGifts(int id) {
        ArrayList<Gift> gifts = GiftsModel.getGiftByPartnerId(id);
        if (gifts == null) {
            return;
        }
        LinearLayout giftsList = (LinearLayout) findViewById(R.id.gifts_list);
        for (Gift gift : gifts) {
            RelativeLayout giftView = getGiftView(gift);
            giftsList.addView(giftView);
        }

    }

    private RelativeLayout getGiftView(final Gift gift) {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.item_tile, null);

        TextView tileTitle = (TextView) relativeLayout.findViewById(R.id.tile_title);
        tileTitle.setText(gift.getName());

        if (gift.getLogo() != null) {
            String logo = gift.getLogo();
            ImageView tilePicture = (ImageView) relativeLayout.findViewById(R.id.tile_picture);
            Ion.with(tilePicture)
                    .placeholder(R.drawable.a)
                    .load(getApplicationContext().getString(R.string.image_url_prefix) + logo);
            ;
        }

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, GiftDetailActivity.class);
                intent.putExtra(GiftDetailActivity.EXTRA_POSITION, String.valueOf(gift.getId()));
                context.startActivity(intent);
            }
        });

        return relativeLayout;
    }

}
