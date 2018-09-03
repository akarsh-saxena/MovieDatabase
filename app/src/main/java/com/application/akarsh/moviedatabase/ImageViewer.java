package com.application.akarsh.moviedatabase;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import me.relex.photodraweeview.PhotoDraweeView;

public class ImageViewer extends AppCompatActivity {

    //ImageView largeImage;
//    ProgressBar progressBar;
    String imagePath;
    PhotoDraweeView mPhotoDraweeView;
 //   com.github.chrisbanes.photoview.PhotoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_image_viewer);

        imagePath = getIntent().getExtras().getString("Image_Path");
        Log.d("URL", imagePath);
//        imageView = (com.github.chrisbanes.photoview.PhotoView) findViewById(R.id.imageView);
        mPhotoDraweeView = (PhotoDraweeView) findViewById(R.id.mPhotoDraweeView);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setVisibility(ProgressBar.VISIBLE);

        /*Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                return;
            }
        });
        *//*builder.build().load("http://image.tmdb.org/t/p/original" + imagePath).into(largeImage);
        largeImage.setVisibility(View.GONE);*//*
        builder.build().load("http://image.tmdb.org/t/p/original" + imagePath).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                largeImage.setVisibility(View.VISIBLE);
            }
        });*/

        Uri uri = Uri.parse("http://image.tmdb.org/t/p/original" + imagePath);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        mPhotoDraweeView = (PhotoDraweeView) findViewById(R.id.mPhotoDraweeView);
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
    //    controller.setUri(uri);
        controller.setImageRequest(request);
        controller.setOldController(mPhotoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null) {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mPhotoDraweeView.setController(controller.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FetchBitmap fetchBitmap = new FetchBitmap();
        fetchBitmap.execute(imagePath);

        return super.onOptionsItemSelected(item);
    }

    public class FetchBitmap extends AsyncTask<String, String, String> {

        String file_path;

        @Override
        protected String doInBackground(String... params) {
            try {
                String imagePath = params[0];
                URL url = new URL("https://image.tmdb.org/t/p/original" + imagePath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/The Movie Database");
                if (!file.exists())
                    file.mkdir();
                file_path = file.getPath() + "/" + DateFormat.getDateTimeInstance().format(new Date()) + ".jpeg";
                FileOutputStream ostream = new FileOutputStream(file_path);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file_path;
        }

        @Override
        public void onPostExecute(String file_name) {
            File final_file = new File(file_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.fromFile(final_file);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                sendBroadcast(intent);
            }
            Toast.makeText(ImageViewer.this, "Image saved to: "+Environment.getExternalStorageDirectory().getPath() + "/The Movie Database", Toast.LENGTH_LONG).show();
        }
    }
}