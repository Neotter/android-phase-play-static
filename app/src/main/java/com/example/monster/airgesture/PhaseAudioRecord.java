package com.example.monster.airgesture;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/6.
 */

public class PhaseAudioRecord {
    public class WavAudioRecord{
        public int audioSource = MediaRecorder.AudioSource.MIC;
        public int sampleRateInHz = GlobalConfig.AUDIO_SAMPLE_RATE;
        public int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        public int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    }
    private int wavCopyChannelNum = 1;//1;//2;//1;
    private short wavCopyBitsPerSample = 16;
    public static long lRecordNum = 0;
    AudioRecord recorder;
    recordThread stRecordThread = new  recordThread();                                               //录制的Pcm文件名称
    // 录制的PCM文件句柄
    WavAudioRecord stWavAudioRecord = new WavAudioRecord();
    public static int  offsetInBytes = 0;
    public static boolean   bFirstSimulatePlay = true;
    public static boolean   bSimulate = true;
    public void initRecord(){
        int bufferSize = AudioRecord.getMinBufferSize(stWavAudioRecord.sampleRateInHz,
                stWavAudioRecord.channelConfig, stWavAudioRecord.audioFormat);
        recorder = new AudioRecord(stWavAudioRecord.audioSource,
                stWavAudioRecord.sampleRateInHz, stWavAudioRecord.channelConfig,
                stWavAudioRecord.audioFormat, bufferSize*10);
        if(GlobalConfig.bRecordThreadFlag) {
            stRecordThread = new recordThread();
            stRecordThread.start();
        }
    }

    public void stopRecording() throws IOException {
        Log.i("timer","audio Stopped");
        //record release
        GlobalConfig.isRecording = false;
        if(GlobalConfig.bSaveWavFile) {
            String sFile = GlobalConfig.fPcmRecordFile.getAbsolutePath();
            String sWavPath = WaveFileUtil.getWaveFile(sFile);
            int bufferSize = AudioRecord.getMinBufferSize(stWavAudioRecord.sampleRateInHz,
                    stWavAudioRecord.channelConfig, stWavAudioRecord.audioFormat);
            WaveFileUtil.copyWaveFile(sFile, sWavPath, stWavAudioRecord.sampleRateInHz, wavCopyChannelNum, bufferSize, wavCopyBitsPerSample);//给裸数据加上头文件

            String sFile2 = GlobalConfig.fPcmRecordFile2.getAbsolutePath();
            String sWavPath2 = WaveFileUtil.getWaveFile(sFile2);
            WaveFileUtil.copyWaveFile(sFile2, sWavPath2, stWavAudioRecord.sampleRateInHz, wavCopyChannelNum, bufferSize, wavCopyBitsPerSample);//给裸数据加上头文件
        }
        recorder.stop();
        recorder.release();
    }



    private class recordThread  extends Thread {

        @Override
        public void run() {
            //Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            int bufferSize = AudioRecord.getMinBufferSize(stWavAudioRecord.sampleRateInHz,
                    stWavAudioRecord.channelConfig, stWavAudioRecord.audioFormat);
            while (GlobalConfig.isRecording!=true){}
            try {
                if(GlobalConfig.fPcmRecordFile != null){
                    GlobalConfig.recDos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(GlobalConfig.fPcmRecordFile)));
                }
                //Log.i("audio","buffersize:"+bufferSize);
                recordByte(GlobalConfig.RECORD_FRAME_SIZE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void recordByte(int bufferSize) throws IOException, InterruptedException {
            recorder.startRecording();
            GlobalConfig.isRecording = true;
            while (GlobalConfig.isRecording ) {
                if (GlobalConfig.bPlayDataReady) {
                    //Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                    byte[] rec = new byte[bufferSize];
                    int iReadSize = 0;                 
                    iReadSize = recorder.read(rec, 0, rec.length);
                    long lNow = System.currentTimeMillis();
                    lRecordNum = lRecordNum+iReadSize;
                    //Log.i("WaveFileUtil ", "|before writetoFile iReadSize:" + iReadSize);
                    if (GlobalConfig.bSupportLLAP) {
                        int iMaxData = MatrixProcess.max(rec, rec.length);
                        //Log.i("speed","record:"+lRecordNum + "|lNow:"+lNow + "|max:"+iMaxData +"|iFrame:" + iFrame);
                        if (iMaxData != 0) {                     //
                            //Log.i("mFreqPower","==================iframe:"+iFrame+"======================");
                            //Log.i("read ", "readdata:" + iReadSize);
                            GlobalConfig.getInstance().pushRecData(rec);
                            /*for(int i=0; i<rec.length;i++){
                                Log.i("recdata","record["+i+"],"+rec[i]);
                            }*/
                            //writeByte(rec);
                        }
                    }
                }
            }
        }



        public void writeByte(byte[] recData){
            if (GlobalConfig.bByte && GlobalConfig.bSaveWavFile) {
                int iReadSize = recData.length;
                //Log.i("WaveFileUtil ", "|before writetoFile iReadSize:" + iReadSize);

                if (GlobalConfig.recDos == null) {
                    //Log.i("record", "resdos is null");
                    if (GlobalConfig.fPcmRecordFile != null) {
                        try {
                            GlobalConfig.recDos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(GlobalConfig.fPcmRecordFile)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //循环将buffer中的音频数据写入到OutputStream中
                if (GlobalConfig.recDos != null) {
                    for (int i = 0; i < iReadSize; i++) {
                        try {
                            GlobalConfig.recDos.writeByte(recData[i]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
