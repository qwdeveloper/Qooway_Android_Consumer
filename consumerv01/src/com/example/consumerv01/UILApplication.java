package com.example.consumerv01;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class UILApplication extends Application {
        @SuppressWarnings("unused")
        @Override
        public void onCreate() {


                super.onCreate();

                initImageLoader(getApplicationContext());
        }

        public static void initImageLoader(Context context) {
                // This configuration tuning is custom. You can tune every option, you may tune some of them,
                // or you can create default configuration by
                //  ImageLoaderConfiguration.createDefault(this);
                // method.
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                                .threadPriority(Thread.NORM_PRIORITY - 2)
                                .denyCacheImageMultipleSizesInMemory()
                                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                                .tasksProcessingOrder(QueueProcessingType.LIFO)
                                .writeDebugLogs() // Remove for release app
								.memoryCacheSize(41943040)
								.discCacheSize(104857600)
								.threadPoolSize(10)
                                .build();
                // Initialize ImageLoader with configuration.

                ImageLoader.getInstance().init(config);
        }
}