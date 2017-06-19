package com.emteria.samples.picamera;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RaspistillUtility {
    private static boolean mAccessEnabled = false;

    public static void enableAccess()
    {
        if (mAccessEnabled)
            return;

        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 0777 /dev/vchiq"}).waitFor();
            Log.i("RaspistillUtility", "Enabled access to vchiq");
        }
        catch (Exception ex)
        {
            Log.i("RaspistillUtility", "Failed enabling access to vchiq", ex);
        }

        mAccessEnabled = true;
    }

    public static boolean snapshot(String path) throws IOException, InterruptedException {
        enableAccess();

        Log.i("RaspistillUtility", "Starting raspistill");
        Process p = Runtime.getRuntime().exec("raspistill --width 1920 --height 1080 --verbose --timeout 1 --output " + path);
        BufferedReader outReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        while (outReader.ready() && (line = outReader.readLine()) != null)
            Log.i("Raspistill", line);
        int res = p.waitFor();
        Log.i("RaspistillUtility", "Process exited with code " + res);
        return res == 0;
    }
}
