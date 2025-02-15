package com.hollax.clipboardimage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
                
                // Convert Bitmap to BitmapDrawable if needed
                BitmapDrawable bitmapDrawable = new BitmapDrawable(cordova.getActivity().getResources(), bitmap);

                // Convert bitmap to Base64
                String base64Image = convertBitmapToBase64(bitmap);

                // Get MIME type
                String mimeType = getMimeType(uri);

                // Prepare JSON response
                JSONObject response = new JSONObject();
                response.put("base64", base64Image);
                response.put("mimeType", mimeType);

                callbackContext.success(response);

            } catch (IOException | JSONException e) {
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

    private String getMimeType(Uri uri) {
        String type = cordova.getActivity().getContentResolver().getType(uri);
        if (type == null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type != null ? type : "image/png"; // Default to PNG if unknown
    }
}
