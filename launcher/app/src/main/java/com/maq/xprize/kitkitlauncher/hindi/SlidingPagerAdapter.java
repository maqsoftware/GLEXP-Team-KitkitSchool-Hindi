package com.maq.xprize.kitkitlauncher.hindi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maq.kitkitProvider.KitkitDBHandler;
import com.maq.kitkitProvider.User;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class SlidingPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<User> mData;
    private LayoutInflater layoutInflater;
    Dialog dialog;


    public SlidingPagerAdapter(@NonNull Context context, ArrayList<User> users) {

        mContext = context;
        mData = users;

        if (mData != null && mData.get(0).getUserName().equals("user0")) {
            User user0 = mData.remove(0);
            mData.add(user0);
        }



    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        User item = mData.get(position);
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliding_images,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.finaluserimage);
        TextView username = (TextView) view.findViewById(R.id.userN);
        TextView english = (TextView) MultiUserActivity.selectUserDialog.findViewById(R.id.engprogress);
        TextView math = (TextView) MultiUserActivity.selectUserDialog.findViewById(R.id.mathprogress);


        imageView.setImageBitmap(converToBitmap(item.getImage()));
        username.setText(item.getDisplayName());
        english.setText(String.valueOf(item.getGamesClearedInTotal_L()));
        math.setText(String.valueOf(item.getGamesClearedInTotal_M()));
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }

    public Bitmap converToBitmap(byte[] image){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }

}
