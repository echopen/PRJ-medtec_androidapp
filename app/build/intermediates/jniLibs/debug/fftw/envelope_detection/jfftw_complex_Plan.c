#include "jfftw_complex_Plan.h"
#include <fftw.h>


/*
 * Class:     jfftw_complex_Plan
 * Method:    createPlan
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_complex_Plan_createPlan( JNIEnv *env, jobject obj, jint n, jint dir, jint flags )
{
	jclass clazz;
	jfieldID id;
	jbyteArray arr;
	unsigned char* carr;

	if( sizeof( jdouble ) != sizeof( fftw_real ) )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/RuntimeException" ), "jdouble and fftw_real are incompatible" );
		return;
	}

	clazz = (*env)->GetObjectClass( env, obj );
	id    = (*env)->GetFieldID( env, clazz, "plan", "[B" );
	arr   = (*env)->NewByteArray( env, sizeof( fftw_plan ) );
	carr  = (*env)->GetByteArrayElements( env, arr, 0 );

	(*env)->MonitorEnter( env, (*env)->FindClass( env, "jfftw/Plan" ) );

	*(fftw_plan*)carr = fftw_create_plan( n, dir, flags );

	(*env)->MonitorExit( env, (*env)->FindClass( env, "jfftw/Plan" ) );

	(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
	(*env)->SetObjectField( env, obj, id, arr );
}
/*
 * Class:     jfftw_complex_Plan
 * Method:    createPlanSpecific
 * Signature: (III[DI[DI)V
 */
JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_complex_Plan_createPlanSpecific( JNIEnv *env, jobject obj, jint n, jint dir, jint flags, jdoubleArray in, jint idist, jdoubleArray out, jint odist )
{
	jclass clazz;
	jfieldID id;
	jbyteArray arr;
	unsigned char* carr;
	double *cin, *cout;

	if( sizeof( jdouble ) != sizeof( fftw_real ) )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/RuntimeException" ), "jdouble and fftw_real are incompatible" );
		return;
	}

	clazz = (*env)->GetObjectClass( env, obj );
	id    = (*env)->GetFieldID( env, clazz, "plan", "[B" );
	arr   = (*env)->NewByteArray( env, sizeof( fftw_plan ) );
	carr  = (*env)->GetByteArrayElements( env, arr, 0 );
	cin   = (*env)->GetDoubleArrayElements( env, in, 0 );
	cout  = (*env)->GetDoubleArrayElements( env, out, 0 );

	(*env)->MonitorEnter( env, (*env)->FindClass( env, "jfftw/Plan" ) );

	*(fftw_plan*)carr = fftw_create_plan_specific( n, dir, flags, (fftw_complex*)cin, idist, (fftw_complex*)cout, odist );

	(*env)->MonitorExit( env, (*env)->FindClass( env, "jfftw/Plan" ) );

	(*env)->ReleaseDoubleArrayElements( env, in, cin, 0 );
	(*env)->ReleaseDoubleArrayElements( env, out, cout, 0 );
	(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
	(*env)->SetObjectField( env, obj, id, arr );
}
/*
 * Class:     jfftw_complex_Plan
 * Method:    destroyPlan
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_complex_Plan_destroyPlan( JNIEnv* env, jobject obj )
{
	jclass clazz = (*env)->GetObjectClass( env, obj );
	jfieldID id = (*env)->GetFieldID( env, clazz, "plan", "[B" );
	jbyteArray arr = (jbyteArray)(*env)->GetObjectField( env, obj, id );
	unsigned char* carr = (*env)->GetByteArrayElements( env, arr, 0 );

	fftw_destroy_plan( *(fftw_plan*)carr );

	(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
	(*env)->SetObjectField( env, obj, id, NULL );
}
/*
 * Class:     jfftw_complex_Plan
 * Method:    transform
 * Signature: ([D)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_jfftw_complex_Plan_transform___3D( JNIEnv* env, jobject obj, jdoubleArray in )
{
	jdouble *cin, *cout;
	jdoubleArray out;
	int i;

	jclass clazz = (*env)->GetObjectClass( env, obj );
	jfieldID id = (*env)->GetFieldID( env, clazz, "plan", "[B" );
	jbyteArray arr = (jbyteArray)(*env)->GetObjectField( env, obj, id );
	unsigned char* carr = (*env)->GetByteArrayElements( env, arr, 0 );
	fftw_plan plan = *(fftw_plan*)carr;
	if( plan->n * 2 != (*env)->GetArrayLength( env, in ) )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/IndexOutOfBoundsException" ), "the Plan was created for a different length" );
		(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
		return NULL;
	}

	cin = (*env)->GetDoubleArrayElements( env, in, 0 );

	if( ! plan->flags & FFTW_THREADSAFE )
	{
		// synchronization
		(*env)->MonitorEnter( env, obj );
	}

	if( plan->flags & FFTW_IN_PLACE )
	{
		out = in;

		fftw_one( plan, (fftw_complex*)cin, NULL );
	}
	else
	{
		out = (*env)->NewDoubleArray( env, plan->n * 2 );
		cout = (*env)->GetDoubleArrayElements( env, out, 0 );

		fftw_one( plan, (fftw_complex*)cin, (fftw_complex*)cout );

		(*env)->ReleaseDoubleArrayElements( env, out, cout, 0 );
	}

	if( ! plan->flags & FFTW_THREADSAFE )
	{
		// synchronization
		(*env)->MonitorEnter( env, obj );
	}

	(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
	(*env)->ReleaseDoubleArrayElements( env, in, cin, 0 );
	return out;
}
/*
 * Class:     jfftw_complex_Plan
 * Method:    transform
 * Signature: (I[DII[DII)V
 */
JNIEXPORT void JNICALL Java_jfftw_complex_Plan_transform__I_3DII_3DII( JNIEnv *env, jobject obj, jint howmany, jdoubleArray in, jint istride, jint idist, jdoubleArray out, jint ostride, jint odist )
{
	jdouble *cin, *cout;
	int i;

	jclass clazz = (*env)->GetObjectClass( env, obj );
	jfieldID id = (*env)->GetFieldID( env, clazz, "plan", "[B" );
	jbyteArray arr = (jbyteArray)(*env)->GetObjectField( env, obj, id );
	unsigned char* carr = (*env)->GetByteArrayElements( env, arr, 0 );
	fftw_plan plan = *(fftw_plan*)carr;
	if( (howmany - 1) * idist * 2 + plan->n * istride * 2 != (*env)->GetArrayLength( env, in ) )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/IndexOutOfBoundsException" ), "the Plan was created for a different length (in)" );
		(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
		return;
	}
	if( (howmany - 1) * odist * 2 + plan->n * ostride * 2 != (*env)->GetArrayLength( env, out ) )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/IndexOutOfBoundsException" ), "the Plan was created for a different length (out)" );
		(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
		return;
	}

	cin = (*env)->GetDoubleArrayElements( env, in, 0 );
	cout = (*env)->GetDoubleArrayElements( env, out, 0 );

	if( ! plan->flags & FFTW_THREADSAFE )
	{
		// synchronization
		(*env)->MonitorEnter( env, obj );
	}
	fftw( plan, howmany, (fftw_complex*)cin, istride, idist, (fftw_complex*)cout, ostride, odist );
	if( ! plan->flags & FFTW_THREADSAFE )
	{
		// synchronization
		(*env)->MonitorExit( env, obj );
	}

	(*env)->ReleaseByteArrayElements( env, arr, carr, 0 );
	(*env)->ReleaseDoubleArrayElements( env, in, cin, 0 );
	(*env)->ReleaseDoubleArrayElements( env, out, cout, 0 );
}


