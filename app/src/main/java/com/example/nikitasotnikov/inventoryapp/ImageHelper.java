package com.example.nikitasotnikov.inventoryapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by NikitaSotnikov on 20.10.2017.
 */

public class ImageHelper {
    /**
     * converts an image bitmap into a sql blob-compatible array
     *
     * @param bitmapImage the input image
     * @return byte[] of the compressed image
     */
    public static final byte[] convertBitmapToBlob(Bitmap bitmapImage) {
        // result
        byte[] byteArray;

        try {
            // http://stackoverflow.com/questions/10618325/how-to-create-a-blob-from-bitmap-in-android-activity
            // from user grattmandu03
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteOutputStream);
            byteArray = byteOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e("EditorActivity", "convertBitmapToBlob exception: " + e);
            return null;
        }

        return byteArray;
    }

    public static final Bitmap convertBlobToBitmap(byte[] imageByteArray) {
        if (imageByteArray == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }
}