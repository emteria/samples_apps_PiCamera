/*
 * Copyright (C) 2017 emteria
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

#include <jni.h>
#include <string>
#include <stdlib.h>

#include <android/log.h>
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "SerialJNI", __VA_ARGS__)

extern "C" JNIEXPORT void JNICALL Java_com_emteria_samples_picamera_RaspistillUtility_sendSignal(JNIEnv *env, jobject /* this */)
{
    FILE* command = popen("pidof raspistill", "r");

    int length = 128;
    char line[length];
    fgets(line, length, command);
    LOGI("Detected raspistill PID: %s", line);

    jint pid = (jint) strtol(line, NULL, 10);
    pclose(command);

    int ret = kill(pid, SIGUSR1);
    LOGI("Signal result: %d", ret);
}
