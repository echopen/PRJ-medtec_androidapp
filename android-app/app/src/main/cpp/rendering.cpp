//
// Created by clecoued on 10/01/19.
//

#include <jni.h>
#include "scan_conversion_float.hpp"
#include "ThreadPool.h"
#include "Convolution3x3Filter.hpp"
#include "LinearQuantificationFilter.hpp"
#include <memory>
#include <android/log.h>

bool gIsContextReady = false;
std::shared_ptr<scan_conv> gScanConv;
std::shared_ptr<ThreadPool> gPool;

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_echopen_asso_echopen_echography_1image_1streaming_EchographyImageStreamingService_render(JNIEnv *env, jobject instance,
                                                                 jintArray iImageInput_,
                                                                 jdouble slope, jdouble offset) {
    jint *iImageInput = env->GetIntArrayElements(iImageInput_, NULL);

    float ** lImageInput = NULL;
    lImageInput=(float **)malloc(gScanConv->Nline*sizeof(float *));
    for (int i=0 ; i<gScanConv->Nline ; i++) {lImageInput[i]=(float *)malloc(gScanConv->Nr*sizeof(float));}

    for(int i = 0; i < gScanConv->Nline; i++){
        for(int j = 0; j < gScanConv->Nr; j++){
            lImageInput[i][j] = iImageInput[j + i *  gScanConv->Nr];
        }
    }

    // apply scan conversion
    image_scan_conversion(gScanConv.get(), lImageInput);

    // clean image input
    free(lImageInput);
    for (int i=0 ; i<gScanConv->Nline ; i++) {free(lImageInput[i]);}
    env->ReleaseIntArrayElements(iImageInput_, iImageInput, 0);

    Image2D<float> lScanConverted(gScanConv->Nx, gScanConv->Ny);
    for(int i = 0; i < gScanConv->Nx; i++)
    {
        for(int j = 0; j < gScanConv->Ny; j++)
        {
            lScanConverted.mData[i][j] = gScanConv.get()->image[i][j];
        }
    }

    Image2D<int> lImageRGBOutput(gScanConv->Nx, gScanConv->Ny);
    LinearQuantificationFilter lLinearQuantificationFilter = LinearQuantificationFilter();
    lLinearQuantificationFilter.setUp(gPool);
    lLinearQuantificationFilter.applyFilter(lScanConverted, lImageRGBOutput, 0, 1000);

    // return generated image
    jintArray oImage_ = env->NewIntArray(gScanConv->Nx * gScanConv->Ny);
    jint oImage[gScanConv->Nx * gScanConv->Ny];
    for(int i = 0; i < gScanConv->Ny; i++){
        for(int j = 0; j < gScanConv->Nx; j++){
            oImage[j + i * gScanConv->Nx] = lImageRGBOutput.mData[i][j];
        }
    }

    env->SetIntArrayRegion(oImage_, 0, gScanConv->Nx * gScanConv->Ny, oImage);
    return oImage_;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_echopen_asso_echopen_echography_1image_1streaming_EchographyImageStreamingService_prepareRenderingContext(JNIEnv *env,
                                                                                  jobject instance,
                                                                                  jint Nr_probe,
                                                                                  jint Nline_probe,
                                                                                  jfloat sector_probe,
                                                                                  jfloat R0_probe,
                                                                                  jfloat Rf_probe,
                                                                                  jint Nx_im,
                                                                                  jint Ny_im,
                                                                                  jfloat nh0,
                                                                                  jfloat nhp,
                                                                                  jfloat nsr,
                                                                                  jfloat ndx,
                                                                                  jfloat Rb,
                                                                                  jfloat l1,
                                                                                  jfloat tr1,
                                                                                  jfloat nspeed,
                                                                                  jfloat ndec,
                                                                                  jfloat ndelay) {

    if(gIsContextReady)
        return;

    gIsContextReady = true;

    int lRecommendedThreads = std::thread::hardware_concurrency() * 2;
    gPool = std::make_shared<ThreadPool>(lRecommendedThreads);
    gScanConv = std::make_shared<scan_conv>();

    __android_log_print(ANDROID_LOG_DEBUG, "Config Scan", "%d %d %f %f %f %d %d %f %f %f %d %f %f %f %d %f %f %f %d", (int) Nr_probe, (int) Nline_probe, (float) sector_probe, (float) R0_probe * 1000, (float) Rf_probe * 1000, (int) Nx_im, (int) Ny_im,  nh0, nhp, nsr, ndec, ndelay, ndx, nspeed, 0, Rb, l1, tr1, 0);
    create_scan_conv_struct_lounger(gScanConv.get(), (int)Nline_probe , (int) Nr_probe, (float) sector_probe, (float) R0_probe * 1000, (float) Rf_probe * 1000, (int) Nx_im, (int) Ny_im,  nh0, nhp, nsr, ndec, ndelay, ndx, nspeed, 0, Rb, l1, tr1, 0);
}
