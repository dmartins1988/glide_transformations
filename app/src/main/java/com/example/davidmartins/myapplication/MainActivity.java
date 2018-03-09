package com.example.davidmartins.myapplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageCrop);


        RequestOptions options = new RequestOptions();

        final RequestOptions transform = options
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(new CropTransformation(getBaseContext(), CropTransformation.CROP_BOTTOM));

        Glide.with(getBaseContext())
                .load(R.drawable.unnamed)
                .apply(transform)
                .into(mImageView);

    }
}
