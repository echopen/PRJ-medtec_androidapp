#include "jfftw_Wisdom.h"
#include <fftw.h>

/*
 * Class:     jfftw_Wisdom
 * Method:    get
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_Wisdom_get( JNIEnv * env, jclass clazz )
{
	char* cwisdom = fftw_export_wisdom_to_string();

	jstring wisdom = (*env)->NewStringUTF( env, cwisdom );
	fftw_free( cwisdom );

	return wisdom;
}
/*
 * Class:     jfftw_Wisdom
 * Method:    add
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_Wisdom_add( JNIEnv * env, jclass clazz, jstring wisdom )
{
	const char* cwisdom = (*env)->GetStringUTFChars( env, wisdom, NULL );

	fftw_status s = fftw_import_wisdom_from_string( cwisdom );

	(*env)->ReleaseStringUTFChars( env, wisdom, cwisdom );

	if( s == FFTW_FAILURE )
	{
		(*env)->ThrowNew( env, (*env)->FindClass( env, "java/lang/IllegalArgumentException" ), "unable to parse wisdom" );
	}
}
/*
 * Class:     jfftw_Wisdom
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_echopen_asso_echopen_envelope1_detection_jfftw_Wisdom_clear( JNIEnv * env, jclass clazz )
{
	fftw_forget_wisdom();
}
