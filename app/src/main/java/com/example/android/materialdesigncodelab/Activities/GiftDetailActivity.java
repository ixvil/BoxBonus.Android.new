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

package com.example.android.materialdesigncodelab.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.Models.Gift;
import com.example.android.materialdesigncodelab.Models.Shop;
import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class GiftDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

//        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        String position = getIntent().getStringExtra(EXTRA_POSITION);

        if (Gift.gifts == null) {
            throw new RuntimeException("No shops");
        }

        JsonObject obj = Gift.getGiftById(position);
        fillGift(collapsingToolbar, obj);
    }

    private void fillGift(CollapsingToolbarLayout collapsingToolbar, JsonObject obj) {
        if (obj == null) {
            throw new RuntimeException("No such gift");
        }

        JsonObject partnerAttributes = (JsonObject) obj.get("attributes");

        Resources resources = getResources();
        collapsingToolbar.setTitle(partnerAttributes.get("name").getAsString());


        if (!(partnerAttributes.get("description") instanceof JsonNull)) {
            TextView placeDetail = (TextView) findViewById(R.id.place_detail);
            placeDetail.setText(partnerAttributes.get("description").getAsString());
        }

//        TextView placeLocation = (TextView) findViewById(R.id.place_location);

        if (!(partnerAttributes.get("logo") instanceof JsonNull)) {
            ImageView placePicture = (ImageView) findViewById(R.id.image);
            Ion.with(placePicture)
                    .placeholder(R.drawable.a)
                    .load(getApplicationContext().getString(R.string.image_url_prefix)
                            + partnerAttributes.get("logo").getAsString()
                    );
        }
        int id = (int) obj.get("id").getAsInt();
    }

}
