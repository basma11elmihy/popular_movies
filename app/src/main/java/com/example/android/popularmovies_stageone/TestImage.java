package com.example.android.popularmovies_stageone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class TestImage implements LoaderManager.LoaderCallbacks<String> {
    String ImageUrl,image_path;
    Context con;

    public TestImage(Context con) {
        this.con = con;
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(con) {
            @Nullable
            @Override
            public String loadInBackground() {
                //how to download an image from:
                //https://stackoverflow.com/questions/8423987/download-image-for-imageview-on-android
                URL url;
                BufferedOutputStream out;
                InputStream in;
                BufferedInputStream buf;

                try{
                    url = new URL (ImageUrl);
                    in = url.openStream();
                    buf = new BufferedInputStream(in);

                    Bitmap bitmap = BitmapFactory.decodeStream(buf);
                    if (in != null){
                        in.close();
                    }
                    if (buf != null){
                        buf.close();
                    }

                    // how to use external storage and save the bitmap
                    //https://stackoverflow.com/questions/12559974/save-images-from-drawable-to-internal-file-storage-in-android
                    //https://stackoverflow.com/questions/28576379/download-image-and-save-in-gallery
                    String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+ File.separator + con.getResources().getString(R.string.app_name);
                    File f = new File(extStorageDirectory);
                    if (!f.exists())
                    {
                        f.mkdirs();
                    }

                    //https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java
                    String[] parts = ImageUrl.split("500",2);
                    File file = new File (f,parts[1]);
                    if (!file.exists())
                    {
                        file.createNewFile();
                    }
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

                    String path = file.toString();

                    outputStream.flush();
                    outputStream.close();
                    return path;
                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("ImageLoader","The Responce Image poster isn't downloaded correctly");
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        setImage_path(s);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
