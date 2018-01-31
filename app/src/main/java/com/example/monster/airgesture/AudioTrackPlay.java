package com.example.monster.airgesture;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Administrator on 2017/6/21.
 */

public class AudioTrackPlay {
    private AudioTrack audioTrack;
    private byte generatedSound[] ;

    AudioTrackPlay() {
        init();
    }
    public void init(){
        generatedSound = new byte[2 * GlobalConfig.stPhaseProxy.GetPlayBuffer().length];

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, GlobalConfig.AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSound.length, AudioTrack.MODE_STATIC);
        int bufferSize = AudioTrack.getMinBufferSize(GlobalConfig.AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        setFrequency();
    }

    //sets to new frequency and continues playing
    public void changeFrequency() {
        setFrequency();
        play();
    }

    //sets frequency and stops sound
    public void setFrequency() {
        genTone();
        audioTrack.write(generatedSound, 0, generatedSound.length);
        audioTrack.setLoopPoints(0, generatedSound.length/2, -1);
        //audioTrack.setLoopPoints(0, 512, -1);
    }

    public void play() {
        //16 bit because it's supported by all phones
        audioTrack.play();
    }
    public void pause() {
        audioTrack.pause();
    }

    public void stop() {
        audioTrack.stop();
        audioTrack.release();
    }

    void genTone(){
        // fill out the array

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final short val : GlobalConfig.stPhaseProxy.GetPlayBuffer()) {
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSound[idx++] = (byte) (val & 0x00ff);
            //Log.i("playdata","["+idx+"],"+generatedSound[idx-1] );
            generatedSound[idx++] = (byte) ((val & 0xff00) >>> 8);
            //Log.i("playdata","["+idx+"],"+generatedSound[idx-1] );
        }
        //GlobalConfig.stWaveFileUtil.saveDataToWav(generatedSound,GlobalConfig.stWaveFileUtil.getAndroidPlayFileName(),(long)(GlobalConfig.AUDIO_SAMPLE_RATE),1,generatedSound.length,(short)16);
    }
}
