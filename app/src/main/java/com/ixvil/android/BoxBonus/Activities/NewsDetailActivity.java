package com.ixvil.android.BoxBonus.Activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixvil.android.BoxBonus.Entities.News;
import com.ixvil.android.BoxBonus.Models.NewsModel;
import com.ixvil.android.BoxBonus.R;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

/**
 * Created by ixvil on 06.01.2017.
 */

public class NewsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

//        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        String position = getIntent().getStringExtra(EXTRA_POSITION);

        if (NewsModel.getNews() == null) {
            throw new RuntimeException("No news yet");
        }

        News one = NewsModel.getNewsById(position);
        try {
            fillOne(collapsingToolbar, one);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void fillOne(CollapsingToolbarLayout collapsingToolbar, News one) throws Exception {
        if (one == null) {
            throw new Exception("No such news");
        }

        collapsingToolbar.setTitle(one.getName());

        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(one.getDescription());

        if (!Objects.equals(one.getLogo(), "")) {
            ImageView placePicture = (ImageView) findViewById(R.id.image);
            Ion.with(placePicture)
                    .placeholder(R.drawable.a)
                    .load(getApplicationContext().getString(R.string.image_url_prefix)
                            + one.getLogo()
                    );
        }

    }


}
