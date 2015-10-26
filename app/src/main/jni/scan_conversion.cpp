#define __CL_ENABLE_EXCEPTIONS
#define CL_USE_DEPRECATED_OPENCL_1_1_APIS

#include "scan_conversion.h"

#include <string>
#include <cmath>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <CL/cl.hpp>

static cl::Context      gContext;
static cl::CommandQueue gQueue;
static cl::Kernel       gNV21Kernel;
static cl::Kernel       gLaplacianK;
static cl::Kernel       scanConverterK;

struct openCLMeta{
    cl::Context      echoContext;
    cl::CommandQueue echoQueue;
    cl::Platform echoPlatform;
    cl::Device echoDevice;
    cl::Program echoProgram;
    cl::Kernel echoKernel;
}

char *file_contents(const char *filename, int *length)
{
    FILE *f = fopen(filename, "r");
    void *buffer;

    if (!f) {
        LOGE("Unabled to open %s for reading\n", filename);
        return NULL;
    }

    fseek(f, 0, SEEK_END);
    *length = ftell(f);
    fseek(f, 0, SEEK_SET);

    buffer = malloc(*length+1);
    *length = fread(buffer, 1, *length, f);
    fclose(f);
    ((char*)buffer)[*length] = '\0';

    return (char*)buffer;
}

bool throwJavaException(JNIEnv *env,std::string method_name,std::string exception_msg, int errorCode=0)
{
    char buf[8];
    sprintf(buf,"%d",errorCode);
    std::string code(buf);

    std::string msg = "@" + method_name + ": " + exception_msg + " ";
    if(errorCode!=0) msg += code;

    jclass generalExp = env->FindClass("java/lang/Exception");
    if (generalExp != 0) {
        env->ThrowNew(generalExp, msg.c_str());
        return true;
    }
    return false;
}

void cb(cl_program p,void* data)
{
    clRetainProgram(p);
    cl_device_id devid[1];
    clGetProgramInfo(p,CL_PROGRAM_DEVICES,sizeof(cl_device_id),(void*)devid,NULL);
    char bug[65536];
    clGetProgramBuildInfo(p,devid[0],CL_PROGRAM_BUILD_LOG,65536*sizeof(char),bug,NULL);
    clReleaseProgram(p);
    LOGE("Build log \n %s\n",bug);
}

JNIEXPORT jboolean JNICALL Java_com_echopen_asso_echopen_LiveFeatureActivity_compileKernels(JNIEnv *env, jclass clazz)
{
    // Find OCL devices and compile kernels
    cl_int err = CL_SUCCESS;
    try {
        std::vector<cl::Platform> platforms;
        cl::Platform::get(&platforms);
        if (platforms.size() == 0) {
            return false;
        }

        cl_context_properties properties[] =
        { CL_CONTEXT_PLATFORM, (cl_context_properties)(platforms[0])(), 0};
        gContext = cl::Context(CL_DEVICE_TYPE_GPU, properties);
        std::vector<cl::Device> devices = gContext.getInfo<CL_CONTEXT_DEVICES>();
        gQueue = cl::CommandQueue(gContext, devices[0], 0, &err);
        int src_length = 0;
		//const char* src  = file_contents("/data/data/com.echopen.asso.echopen/app_execdir/kernels.cl",&src_length);
		const char* src  = file_contents("/data/data/com.echopen.asso.echopen/app_execdir/scan_conversion_kernel.cl",&src_length);

        cl::Program::Sources sources(1,std::make_pair(src, src_length) );

        cl::Program program(gContext, sources);

        program.build(devices,NULL,cb);

        while(program.getBuildInfo<CL_PROGRAM_BUILD_STATUS>(devices[0]) != CL_BUILD_SUCCESS);

        //gNV21Kernel = cl::Kernel(program, "nv21torgba", &err);
        //gLaplacianK = cl::Kernel(program, "laplacian", &err);
        scanConverterK = cl::Kernel(program, "scanConverter", &err);

        return true;
    }
    catch (cl::Error e) {
        if( !throwJavaException(env,"decode",e.what(),e.err()) )
            LOGI("@decode: %s %d \n",e.what(), e.err());
        return false;
    }
}

void helper(uint32_t* out, int osize, uint8_t* in, int isize, int w, int h, int choice)
{
    try {
        cl::Buffer bufferIn = cl::Buffer(gContext, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                isize*sizeof(cl_uchar), in, NULL);
        cl::Buffer bufferOut = cl::Buffer(gContext, CL_MEM_READ_WRITE, osize*sizeof(cl_uchar4));
        cl::Buffer bufferOut2= cl::Buffer(gContext, CL_MEM_READ_WRITE, osize*sizeof(cl_uchar4));

        gNV21Kernel.setArg(3,h);
        gNV21Kernel.setArg(2,w);
        gNV21Kernel.setArg(1,bufferIn);
        gNV21Kernel.setArg(0,bufferOut);
        gQueue.enqueueNDRangeKernel(gNV21Kernel,
                cl::NullRange,
                cl::NDRange( (int)ceil((float)w/16.0f)*16,(int)ceil((float)h/16.0f)*16),
                cl::NDRange(16,16),
                NULL,
                NULL);
        if (choice>0) {
            gLaplacianK.setArg(2,w);
            gLaplacianK.setArg(3,h);
            gLaplacianK.setArg(1,bufferOut);
            gLaplacianK.setArg(0,bufferOut2);
            gQueue.enqueueNDRangeKernel(gLaplacianK,
                    cl::NullRange,
                    cl::NDRange( (int)ceil((float)w/16.0f)*16,(int)ceil((float)h/16.0f)*16),
                    cl::NDRange(16,16),
                    NULL,
                    NULL);
        }
        gQueue.enqueueReadBuffer(bufferOut2, CL_TRUE, 0, osize*sizeof(cl_uchar4), out);
    }
    catch (cl::Error e) {
        LOGI("@oclDecoder: %s %d \n",e.what(),e.err());
    }
}

JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_example_CameraPreview_runfilter(
        JNIEnv *env,
        jclass clazz,
        jobject outBmp,
        jbyteArray inData,
        jint width,
        jint height,
        jint choice)
{
    int outsz = width*height;
    int insz = outsz + outsz/2;
    LOGI("this is the size %d", insz);
    AndroidBitmapInfo bmpInfo;
    if (AndroidBitmap_getInfo(env, outBmp, &bmpInfo) < 0) {
        throwJavaException(env,"gaussianBlur","Error retrieving bitmap meta data");
        return;
    }
    if (bmpInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        throwJavaException(env,"gaussianBlur","Expecting RGBA_8888 format");
        return;
    }
    uint32_t* bmpContent;
    if (AndroidBitmap_lockPixels(env, outBmp,(void**)&bmpContent) < 0) {
        throwJavaException(env,"gaussianBlur","Unable to lock bitmap pixels");
        return;
    }
    jbyte* inPtr = (jbyte*)env->GetPrimitiveArrayCritical(inData, 0);
    if (inPtr == NULL) {
        throwJavaException(env,"gaussianBlur","NV21 byte stream getPointer returned NULL");
        return;
    }
    // call helper for processing frame
    helper(bmpContent,outsz,(uint8_t*)inPtr,insz,width,height,choice);
    // This is absolutely neccessary before calling any other JNI function
    env->ReleasePrimitiveArrayCritical(inData,inPtr,0);
    AndroidBitmap_unlockPixels(env, outBmp);
}

void runScanConverter(uint32_t* out, int size, uint8_t* in,
int length, int weight, int height, int n_samples,
jint* index_data_array, jint* img_data_array, jdouble* weight_array, int num_pixels)
{
    try {
        cl::Buffer bufferIn = cl::Buffer(gContext, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                isize*sizeof(cl_uchar), in, NULL);
        cl::Buffer bufferOut = cl::Buffer(gContext, CL_MEM_READ_WRITE, size*sizeof(cl_uchar4));

        scanConverterK.setArg(0,bufferOut);
        scanConverterK.setArg(1,size);
        scanConverterK.setArg(2,envelope_data);
        scanConverterK.setArg(3,length);
        scanConverterK.setArg(4,im_width);
        scanConverterK.setArg(5,im_height);
        scanConverterK.setArg(6,N_samples);
        scanConverterK.setArg(7,index_samp_line);
        scanConverterK.setArg(8,weight_coef);
        scanConverterK.setArg(9,n_values);

        gQueue.enqueueNDRangeKernel(scanConverterK,
                cl::NullRange,
                cl::NDRange( (int)ceil((float)weight/16.0float)*16,(int)ceil((float)h/16.0f)*16),
                cl::NDRange(8,8),
                NULL,
                NULL);

        gQueue.enqueueReadBuffer(bufferOut, CL_TRUE, 0, osize*sizeof(cl_uchar4), out);
    }
    catch (cl::Error e) {
        LOGI("@scanConverter: %s %d \n",e.what(),e.err());
    }
}

JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_example_CameraPreview_scanConverter(
        JNIEnv *env,
        jclass clazz,
        jobject bitmapOut,
        jbyteArray byteArrayData,
        jint width,
        jint height,
        jint n_samples,
        jintArray index_data,
        jintArray index_img,
        jdoubleArray weight,
        jint num_pixels)
{
    int size = width*height;
    int length = size + size/2;
    LOGI("this is the size %d", length);
    jint *index_data_array;
    jint *img_data_array;
    jdouble *weight_array;

    AndroidBitmapInfo bitmapInfo;
    if (AndroidBitmap_getInfo(env, outBitmap, &bitmapInfo) < 0) {
        throwJavaException(env,"scanConverter","Error retrieving bitmap meta data");
        return;
    }
    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        throwJavaException(env,"scanConverter","Expecting RGBA_8888 format");
        return;
    }
    uint32_t* bitmapContent;
    if (AndroidBitmap_lockPixels(env, bitmapOut,(void**)&bitmapContent) < 0) {
        throwJavaException(env,"scanConverter","Unable to lock bitmap pixels");
        return;
    }
    jbyte* arrayPointer = (jbyte*)env->GetPrimitiveArrayCritical(byteArrayData, 0);
    if (arrayPointer == NULL) {
        throwJavaException(env,"scanConverter","Get byte data failed to NULL");
        return;
    }

    index_data_array = (*env)->GetIntArrayElements(env, index_data, &iscopy);
    img_data_array= (*env)->GetIntArrayElements(env, img_data, &iscopy);
    weight_array = (*env)->GetDoubleArrayElements(env, *weight, NULL);


    runScanConverter(bitmapContent, size, (uint8_t*)arrayPointer, length, width, height, n_samples, index_data_array, img_data_array, weight_array, num_pixels);
    env->ReleasePrimitiveArrayCritical(byteArrayData,arrayPointer,0);
    AndroidBitmap_unlockPixels(env, bitmapOut);
}
