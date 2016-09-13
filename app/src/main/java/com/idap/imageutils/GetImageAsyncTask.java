package com.idap.imageutils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by eugene.kotsogub on 9/13/16.
 *
 */
public class GetImageAsyncTask extends AsyncTask<Void, Void, String> {

    private final Context context;
    private final Uri image;
    private final ImageCallback callback;

    public GetImageAsyncTask(Context context, Uri image, ImageCallback callback){
        this.context = context;
        this.image = image;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return ImageUtils.getImageFile(context, image);
    }

    @Override
    protected void onPostExecute(String path) {
        callback.onResult(path);
    }
}
