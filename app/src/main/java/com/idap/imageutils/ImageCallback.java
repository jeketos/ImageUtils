package com.idap.imageutils;

/**
 * Created by eugene.kotsogub on 9/13/16.
 *
 */
public interface ImageCallback {

    void onResult(String imagePath);
    void onError(Throwable error);

}
