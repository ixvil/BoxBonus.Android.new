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

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixvil.android.BoxBonus.Entities.Gift;
import com.ixvil.android.BoxBonus.Models.GiftsModel;
import com.ixvil.android.BoxBonus.R;
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

        String position = getIntent().getStringExtra(EXTRA_POSITION);

        if (GiftsModel.entities == null) {
            throw new RuntimeException("No shops");
        }

        Gift gift = GiftsModel.getGiftById(position);
        fillGift(collapsingToolbar, gift);
    }

    private void fillGift(CollapsingToolbarLayout collapsingToolbar, Gift gift) {
        if (gift == null) {
            throw new RuntimeException("No such gift");
        }

        collapsingToolbar.setTitle(gift.getName());

        if (gift.getDescription() != null) {
            TextView placeDetail = (TextView) findViewById(R.id.place_detail);
            placeDetail.setText(gift.getDescription());
        }

        if (gift.getLogo() != null) {
            ImageView placePicture = (ImageView) findViewById(R.id.image);
            Ion.with(placePicture)
                    .placeholder(R.drawable.a)
                    .load(getApplicationContext().getString(R.string.image_url_prefix)
                            + gift.getLogo()
                    );
        }
        int id = gift.getId();
    }

}
