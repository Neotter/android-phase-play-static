package com.example.monster.airgesture;

/**
 * Created by bostinshi on 2017/7/27.
 */

public class PhaseProcessI {
    //保存c++类的地址
    public long nativePerson;
    //构造函数
    public PhaseProcessI(int inMaxFramesPerSlice, int inNumFreq, float inStartFreq, float inFreqInterv) {
        nativePerson = createNativeRangeFinder(inMaxFramesPerSlice, inNumFreq, inStartFreq, inFreqInterv);
    }




    static {
        System.loadLibrary("PhaseProcess");
    }

    public native String getJniString();

    public native long createNativeRangeFinder(int inMaxFramesPerSlice, int inNumFreq, float inStartFreq, float inFreqInterv);

    public native float getDistanceChange(long thizptr, short[] recordData, int size);

    public native float[] getBaseBand(long thizptr, int inNumFreq);
}
