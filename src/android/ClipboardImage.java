package com.hollax.clipboardimage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClipboardImage extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getClipboardImage")) {
            getClipboardImage(callbackContext);
            return true;
        }
        return false;
    }

    private void getClipboardImage(CallbackContext callbackContext) {
        ClipboardManager clipboard = (ClipboardManager) cordova.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClip().getItemAt(0).getUri() != null) {
            Uri uri = clipboard.getPrimaryClip().getItemAt(0).getUri();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cordova.getActivity().getContentResolver(), uri);

                // Option 1: Return Base64 String
                String base64Image = convertBitmapToBase64(bitmap);
                callbackContext.success(base64Image);

                // Option 2: Save Image as File and Return Path (Uncomment if needed)
                // String imagePath = saveBitmapToFile(bitmap);
                // callbackContext.success(imagePath);

            } catch (IOException e) {
                callbackContext.error("Failed to retrieve image from clipboard.");
            }
        } else {
            callbackContext.error("No image found in clipboard.");
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String saveBitmapToFile(Bitmap bitmap) throws IOException {
        File file = new File(cordova.getActivity().getCacheDir(), "clipboard_image.png");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        return file.getAbsolutePath();
    }
}
