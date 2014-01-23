package edu.uaic.fii.wad.edec.service.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * User: irina
 * Date: 1/22/14
 * Time: 4:53 PM
 */
public class ImageBase64 {

    public static Bitmap decodeImage(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
