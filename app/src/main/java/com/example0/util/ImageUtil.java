package com.example0.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;


public class ImageUtil {

        public static String imageToBase64(Bitmap bitmap){
            ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] buffer = byteArrayOutputStream.toByteArray();
            String basestr= Base64.encodeToString(buffer, Base64.DEFAULT);
            return basestr;
    }
        public static Bitmap base64ToImage(String base64Str) {
            byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
    }

}
