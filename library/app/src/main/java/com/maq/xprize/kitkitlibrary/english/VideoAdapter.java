package com.maq.xprize.kitkitlibrary.english;

import android.view.ViewGroup;

interface VideoAdapter {
    MainActivity.VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    void onBindViewHolder(MainActivity.VideoAdapter.ViewHolder holder, int position);

    int getItemCount();
}
