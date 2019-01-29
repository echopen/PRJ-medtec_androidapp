//
// Created by clecoued on 10/01/19.
//

#include <jni.h>
#include "scan_conversion_float.hpp"
#include "ThreadPool.h"
#include "Convolution3x3Filter.hpp"
#include "LinearQuantificationFilter.hpp"
#include <memory>

bool gIsContextReady = false;
std::shared_ptr<scan_conv> gScanConv;
std::shared_ptr<ThreadPool> gPool;

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_echopen_asso_echopen_model_Data_AbstractDataTask_render(JNIEnv *env, jobject instance,
                                                                 jintArray iImageInput_,
                                                                 jdouble slope, jdouble offset) {
    jint *iImageInput = env->GetIntArrayElements(iImageInput_, NULL);

    float ** lImageInput = NULL;
    float **data_float=NULL;
    lImageInput=(float **)malloc(gScanConv->Nline*sizeof(float *));
    for (int i=0 ; i<gScanConv->Nline ; i++) {lImageInput[i]=(float *)malloc(gScanConv->Nr*sizeof(float));}

    for(int i = 0; i < gScanConv->Nline; i++){
        for(int j = 0; j < gScanConv->Nr; j++){
            lImageInput[i][j] = iImageInput[j + i *  gScanConv->Nr];
        }
    }

    // apply scan conversion
    image_scan_conversion(gScanConv.get(), lImageInput);
    free(lImageInput);
    env->ReleaseIntArrayElements(iImageInput_, iImageInput, 0);
    /*Image2D<float> lScanConvertedImage = Image2D<float>(gScanConv->Nx, gScanConv->Ny);
    lScanConvertedImage.mData = gScanConv->image;*/

    // apply Gaussian Filter
   /* Image2D<float> lGaussianFilteredImage = Image2D<float>(gScanConv->Nx, gScanConv->Ny);
    Convolution3x3Filter lConvolution3x3Filter = Convolution3x3Filter();
    float lGaussianKernel[3][3] = {{1.f, 2.f, 1.f},{2.f, 4.f, 2.f},{1.f, 2.f, 1.f}};
    lConvolution3x3Filter.setUp(gPool, lGaussianKernel);
    lConvolution3x3Filter.applyFilter(lScanConvertedImage, lGaussianFilteredImage);*/

    // apply Quantification filter
   /* Image2D<int> lRGBImage =  Image2D<int>(gScanConv->Nx, gScanConv->Ny);
    LinearQuantificationFilter lLinearQuantificationFilter = LinearQuantificationFilter();
    lLinearQuantificationFilter.setUp(gPool);
    lLinearQuantificationFilter.applyFilter(lScanConvertedImage, lRGBImage, 0, 500);*/


    Image2D<float> lScanConverted(gScanConv->Nx, gScanConv->Ny);
    for(int i = 0; i < gScanConv->Nx; i++)
    {
        for(int j = 0; j < gScanConv->Ny; j++)
        {
            lScanConverted.mData[i][j] = gScanConv.get()->image[i][j];
        }
    }
    Image2D<float> lImageOutput(gScanConv->Nx, gScanConv->Ny);

    Convolution3x3Filter lConvolution3x3Filter = Convolution3x3Filter();
    float lGaussianKernel[3][3] = {{1.f, 2.f, 1.f},{2.f, 4.f, 2.f},{1.f, 2.f, 1.f}};

    lConvolution3x3Filter.setUp(gPool, lGaussianKernel);
    lConvolution3x3Filter.applyFilter(lScanConverted, lImageOutput);

    Image2D<int> lImageRGBOutput(gScanConv->Nx, gScanConv->Ny);
    LinearQuantificationFilter lLinearQuantificationFilter = LinearQuantificationFilter();
    lLinearQuantificationFilter.setUp(gPool);
    lLinearQuantificationFilter.applyFilter(lImageOutput, lImageRGBOutput, 0.f, 100.f);

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
Java_com_echopen_asso_echopen_model_Data_AbstractDataTask_prepareRenderingContext(JNIEnv *env,
                                                                                  jobject instance,
                                                                                  jint Nr_probe,
                                                                                  jint Nline_probe,
                                                                                  jfloat sector_probe,
                                                                                  jfloat R0_probe,
                                                                                  jfloat Rf_probe,
                                                                                  jint Nx_im,
                                                                                  jint Ny_im) {

    if(gIsContextReady)
        return;

    gIsContextReady = true;
    float nh0 = 10.f, nhp = 0.f, nsr = 80.f, ndelay = 0.f, ndx = 16.f, nspeed = 1480.f;
    int ndec=100, nosc=2, npt=0;
    float Rb=12.5, l1=6.2658, tr1=10.f;

    int lRecommendedThreads = std::thread::hardware_concurrency() * 2;
    gPool = std::make_shared<ThreadPool>(lRecommendedThreads);
    gScanConv = std::make_shared<scan_conv>();
    create_scan_conv_struct_lounger(gScanConv.get(), (int) Nr_probe, (int) Nline_probe, (float) sector_probe, (float) R0_probe, (float) Rf_probe, (int) Nx_im, (int) Ny_im,  nh0, nhp, nsr, ndec, ndelay, ndx, nspeed, 0, Rb, l1, tr1, npt);
}