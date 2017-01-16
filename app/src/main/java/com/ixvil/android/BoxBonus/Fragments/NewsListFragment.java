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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixvil.android.BoxBonus.Activities.NewsDetailActivity;
import com.ixvil.android.BoxBonus.Adapters.AbstractFragmentContentAdapter;
import com.ixvil.android.BoxBonus.Entities.News;
import com.ixvil.android.BoxBonus.Models.NewsModel;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonArray;
import com.koushikdutta.ion.Ion;

/**
 * Provides UI for the view with List.
 */
public class NewsListFragment extends Fragment implements FetchSuccessFragmentInterface {

    private NewsAdapter contentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        contentAdapter = new NewsAdapter(recyclerView.getContext());
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        NewsModel.getFromNet(inflater.getContext(), this);

        return recyclerView;
    }

    @Override
    public void onFetchSuccess(JsonArray jsonArray) {
        NewsModel.setNews(jsonArray);
        contentAdapter.updateAdapter(NewsModel.getNews());
        contentAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView id;
        public TextView desc;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            desc = (TextView) itemView.findViewById(R.id.list_desc);
            id = (TextView) itemView.findViewById(R.id.list_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra(NewsDetailActivity.EXTRA_POSITION, id.getText());
                    context.startActivity(intent);
                }
            });
        }
    }

    class NewsAdapter extends AbstractFragmentContentAdapter<ViewHolder> {

        NewsAdapter(Context context) {
            super(context);
        }

        public void updateAdapter(News[] news) {
            mIds = new String[news.length];
            mGifts = new String[news.length];
            mGiftsPictures = new String[news.length];
            mDescriptions = new String[news.length];
            for (int i = 0; i < news.length; i++) {
                mGifts[i] = news[i].getName();
                mIds[i] = String.valueOf(news[i].getId());
                mGiftsPictures[i] = news[i].getLogo();
                mDescriptions[i] = news[i].getDescription();
            }
            setLength(news.length);
        }

        /**
         * @deprecated
         */
        @Override
        public void updateAdapter(JsonArray jsonObject) {

        }

        @Override
        public NewsListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NewsListFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(mGifts[position]);
            Ion.with(holder.picture)
                    .placeholder(R.drawable.a)
                    .load(getContext().getString(R.string.image_url_prefix) + mGiftsPictures[position]);
            holder.desc.setText(mDescriptions[position]);
            holder.id.setText(mIds[position]);
        }
    }

}
