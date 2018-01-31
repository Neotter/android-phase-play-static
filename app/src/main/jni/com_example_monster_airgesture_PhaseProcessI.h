/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_monster_airgesture_PhaseProcessI */

#ifndef _Included_com_example_monster_airgesture_PhaseProcessI
#define _Included_com_example_monster_airgesture_PhaseProcessI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_monster_airgesture_PhaseProcessI
 * Method:    getJniString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getJniString
        (JNIEnv *, jobject);

/*
 * Class:     com_example_monster_airgesture_PhaseProcessI
 * Method:    createNativeRangeFinder
 * Signature: (IIFF)J
 */
JNIEXPORT jlong JNICALL Java_com_example_monster_airgesture_PhaseProcessI_createNativeRangeFinder
        (JNIEnv *, jobject, jint, jint, jfloat, jfloat);

/*
 * Class:     com_example_monster_airgesture_PhaseProcessI
 * Method:    getDistanceChange
 * Signature: (J[SI)F
 */
JNIEXPORT jfloat JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getDistanceChange
        (JNIEnv *, jobject, jlong, jshortArray, jint);

/*
 * Class:     com_example_monster_airgesture_PhaseProcessI
 * Method:    getBaseBand
 * Signature: ()[F
 */
JNIEXPORT jfloatArray JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getBaseBand
        (JNIEnv *, jobject, jlong , jint);

#ifdef __cplusplus
}
#endif
#endif
