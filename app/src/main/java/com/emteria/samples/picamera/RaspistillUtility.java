/*
 * Copyright (C) 2017 emteria
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package com.emteria.samples.picamera;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RaspistillUtility
{
    private static final String TAG = RaspistillUtility.class.getSimpleName();
    private static final boolean USE_SIGNAL_MODE = true;

    private static void enableAccess()
    {
        String device = "/dev/vchiq";
        File f = new File(device);
        if (f.canRead() && f.canWrite()) { return; }

        try
        {
            String[] cmd = new String[] {"su", "-c", "chmod 0666 " + device};
            Runtime.getRuntime().exec(cmd).waitFor();
            Log.i(TAG, "Enabled access to vchiq");
        }
        catch (Exception ex)
        {
            Log.i(TAG, "Failed enabling access to vchiq", ex);
        }
    }

    public static boolean snapshot(String path) throws IOException, InterruptedException
    {
        enableAccess();
        Log.i(TAG, "Starting raspistill");

        String signal = USE_SIGNAL_MODE ? " --signal" : "";
        Process p = Runtime.getRuntime().exec("raspistill --width 1920 --height 1080 --verbose --timeout 1 --output " + path + signal);

        InputStreamReader isr = new InputStreamReader(p.getErrorStream());
        BufferedReader br = new BufferedReader(isr);

        String line;
        while (br.ready() && (line = br.readLine()) != null) { Log.i(TAG, line); }

        br.close();
        isr.close();

        if (USE_SIGNAL_MODE)
        {
            System.loadLibrary("native-lib");
            sendSignal();

            Log.i(TAG, "Waiting for the file content...");
            Thread.sleep(1000);

            p.destroy();
            Log.i(TAG, "Process was killed, as it was running in signal mode");

            return true;
        }
        else
        {
            int res = p.waitFor();
            Log.i(TAG, "Process exited with code " + res);

            return res == 0;
        }
    }

    public static native void sendSignal();
}
