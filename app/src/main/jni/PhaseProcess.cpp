//
// Created by bostinshi on 2017/7/27.
//
#include "com_example_monster_airgesture_PhaseProcessI.h"
#include <RangeFinder.h>

JNIEXPORT jstring JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getJniString
        (JNIEnv *env, jobject obj)
{
   // new 一个字符串，返回Hello World
   return env -> NewStringUTF("Hello World");
}

JNIEXPORT jlong JNICALL Java_com_example_monster_airgesture_PhaseProcessI_createNativeRangeFinder
        (JNIEnv *env, jobject obj, jint inMaxFramesPerSlice, jint inNumFreqs, jfloat inStartFreq, jfloat inFreqInterv)
{
    return (jlong) (new RangeFinder(inMaxFramesPerSlice, inNumFreqs, inStartFreq, inFreqInterv));
}


JNIEXPORT jfloat JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getDistanceChange
        (JNIEnv *env, jobject obj, jlong thizptr, jshortArray recordData, jint size)
{
    jfloat fDistance = 0.0;
    jshort *carr;
    carr = env->GetShortArrayElements(recordData, 0);
    if(carr == NULL) {
        return 0;
    }
    fDistance = ((RangeFinder*)thizptr)->GetDistanceChange(carr, size);
    //fDistance = RangeFinder::getCosin();//addAll(carr, size);
    env->ReleaseShortArrayElements(recordData, carr, 0);
    return fDistance;
}



JNIEXPORT jfloatArray JNICALL Java_com_example_monster_airgesture_PhaseProcessI_getBaseBand
        (JNIEnv *env, jobject obj, jlong thizptr, jint size)
{
    //创建一个指定大小的数组
    int len = size * 32 * 2;
    jfloatArray jint_arr = env->NewFloatArray(len);
    jfloat *elems = env->GetFloatArrayElements(jint_arr, 0);
    ((RangeFinder*)thizptr)->getBaseBand(elems);
    //同步
    env->ReleaseFloatArrayElements(jint_arr, elems, 0);
    return jint_arr;
}
