package com.idap.imageutils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eugene.kotsogub on 9/13/16.
 *
 */
public class ImageUtilsActivity extends AppCompatActivity {

    private static final String TMP_DIR = "tmp";
    public static final int REQUEST_CAMERA = 101;
    public static final int REQUEST_GALLERY = 102;
    public static final String PHOTO_PATH = "photo_path";
    private String photoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setTheme(R.style.Theme_Transparent);
        if(savedInstanceState != null){
            photoPath = savedInstanceState.getString(PHOTO_PATH);
        } else {
            Bundle extras = getIntent().getExtras();
            String action = extras.getString("action");
            if (!TextUtils.isEmpty(action)) {
                if (action.equals("take_photo")) {
                    takePhoto();
                } else if (action.equals("gallery")) {
                    getImageFromDevice();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PHOTO_PATH, photoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            setResult(RESULT_CANCELED);
            return;
        }
        String path;
        if (requestCode == REQUEST_CAMERA){
//            path = getImageFile( Uri.fromFile(new File(photoPath)));
//            proceedResult(path);
            proceedResult(photoPath);
        } else if (requestCode == REQUEST_GALLERY ){
            path = getImageFile(data.getData());
            proceedResult(path);
        }
        setResult(RESULT_CANCELED);

    }

    private void proceedResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IMAGE_PATH_KEY, path);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void takePhoto() {
        File photoFile = createImageFile();
        photoPath = photoFile.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile));
        startActivityForResult(Intent.createChooser(intent, ""), REQUEST_CAMERA);
    }

    private void getImageFromDevice() {
        Intent intent = new Intent(
                Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, ""),
                REQUEST_GALLERY);

    }

    private String generateFilename() {
        return new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US).format(new Date()) + ".jpg";
    }

    @NonNull
    private File getTempDirectory() {
        File externalDir = getExternalFilesDir(null);

        if (externalDir == null) {
            throw new IllegalStateException("Media storage isn't available");
        }

        return new File(externalDir, TMP_DIR);
    }

    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            cursor.close();
            return string;
        }
        return uri.getPath();
    }

    private String getImageFile(Uri selectedImageUri) {
        Bitmap bitmap = null;
        try {
            InputStream is = getContentResolver().openInputStream(selectedImageUri);
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null) {
            return null;
        }
        File destFile = createImageFile();
        String path = getPath(selectedImageUri);
        //todo next feature
//        if (path != null) {
//            bitmap = fixImageOrientation(path, bitmap);
//        }
        try {
            FileOutputStream fOut = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
            return destFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File createImageFile() {
        String pictureFileName = generateFilename();
        File dstDirectory = getTempDirectory();
        //noinspection ResultOfMethodCallIgnored
        dstDirectory.mkdirs();
        File file = new File(dstDirectory, pictureFileName);
        return file;
    }

    private Bitmap fixImageOrientation(String photoPath,@NonNull Bitmap bitmap) {
        ExifInterface exifInterface = null;
//        Bitmap bitmap;
        try {
            Log.d("image__", "path to exif - " + photoPath);
            exifInterface = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        if (b == null) {
//            bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
//        } else {
//            bitmap = b;
//        }
//        Bitmap photoBitmap = bitmap;
        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Log.d("image__", "orientation - " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        }
        return bitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, float angle) {
        if (bitmap == null) return null;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
