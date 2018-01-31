package com.example.monster.airgesture;


import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import android.os.Process;

public class MainActivity extends ActionBarActivity {
     //////////////////UI///////////////////////////////
    public static TextView tv;
    private String sRecordStatus = "Init Record";
    public static TextView tvTime100MilliSecond;
    private static String INIT_100_MILL_SECOND = "00:00:0";
    private String s100MillSecond = INIT_100_MILL_SECOND;

    //////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.textView2);
        //time init
        tvTime100MilliSecond=(TextView)findViewById(R.id.time_100millisecond);
        tvTime100MilliSecond.setText(s100MillSecond);
        tv.setText(sRecordStatus);
        GlobalConfig.fAbsolutepath.mkdirs();//创建文件夹

        GlobalConfig.stWaveFileUtil.initIQFile();
        startRecordAction();
        GlobalConfig.stPhaseProxy.init();
        //initIos();
        if(GlobalConfig.bPlayThreadFlag) {
            ThreadInstantPlay threadInstantPlay = new ThreadInstantPlay();
            //Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
            threadInstantPlay.start();
        }
        else{
            GlobalConfig.isRecording=true;
        }
    }

    class ThreadInstantPlay extends Thread
    {
        @Override
        public void run()
        {
            //Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            AudioTrackPlay Player= new AudioTrackPlay();
            GlobalConfig.isRecording=true;
            Player.play();

            while (GlobalConfig.isRecording==true){}
            Player.stop();
        }
    }

    private void initIos()
    {
        //GlobalConfig.stWaveFileUtil.readTxtDataToShort(GlobalConfig.stWaveFileUtil.getIosRecordFileName(),GlobalConfig.vIosData);
        for( int i=0; i<GlobalConfig.FRAME_NUM; i++)
        {
            String sFileName = GlobalConfig.stWaveFileUtil.getIosRecordFileNameByFrame(i+1);
            GlobalConfig.stWaveFileUtil.readTxtDataToShort(sFileName,GlobalConfig.vvIosData[i]);
        }
    }
    public void startRecordAction(){
        try {
            //创建临时文件,注意这里的格式为.pcm
            GlobalConfig.fPcmRecordFile = File.createTempFile(GlobalConfig.sRecordPcmFileName, ".pcm", GlobalConfig.fAbsolutepath);
            GlobalConfig.fPcmRecordFile2 = File.createTempFile(GlobalConfig.sRecordPcmFileName2, ".pcm", GlobalConfig.fAbsolutepath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GlobalConfig.stPhaseAudioRecord.initRecord();
        sRecordStatus="-------------start Record-----------------";
        tv.setText(sRecordStatus);
    }

    //runs after pressing the record button
    public void startRecording(View view) throws IOException {
        /*short[] recordData1 = new short[512];
        short[] recordData2 = new short[512];
        short iTmp = 0;
        java.util.Random random=new java.util.Random();// 定义随机类
        for(int i = 0;i < 512;i++)
        {
            iTmp = (short)(random.nextInt(65536) - 32767);
            recordData1[i] = iTmp;
            recordData2[i] = iTmp;
        }

        Log.e("Jni", "aaa");
        long lTime = 0;
        PhaseProcessI ppi = new PhaseProcessI(GlobalConfig.MAX_FRAME_SIZE , GlobalConfig.NUM_FREQ, GlobalConfig.START_FREQ, GlobalConfig.FREQ_INTERVAL);
        Log.e("Jni", ppi.getJniString());
        lTime = System.currentTimeMillis();
        float       f               = ppi.getDistanceChange(ppi.nativePerson, recordData1, recordData1.length);

        float[] iqDatas = ppi.getBaseBand(ppi.nativePerson, GlobalConfig.NUM_FREQ);
        for(int i = 0;i < iqDatas.length;i++) {
            if(i % 64 == 0)
            {
                Log.i("bobo", "Line" + String.valueOf(i / 64) + " ");
            }
            Log.i("bobo", "IQ" + String.valueOf(i % 64) + "=" + String.valueOf(iqDatas[i]));
        }


        long lDic1 = System.currentTimeMillis() - lTime;
        lTime = System.currentTimeMillis();
        float       distancechange  = PhaseProxy.stPhaseProcess.GetDistanceChange(recordData2);
        long lDic2 = System.currentTimeMillis() - lTime;
        Log.i("Jni", String.valueOf(f) + "," + String.valueOf(distancechange) + "," + String.valueOf(lDic1) + "," + String.valueOf(lDic2));*/


        startRecordAction();
        //UI
        view.setClickable(false);
        Button btn=(Button)findViewById(R.id.button);
        btn.setClickable(true);
        //judgeEddian();
    }


    public void stopRecordingAction() throws IOException {
        Log.i("timer","audio Stopped");
        sRecordStatus="!!!!!!!!!!!!!stop Record!!!!!!!!!!!!";
        tv.setText(sRecordStatus);
        //play stop
        //record release
        GlobalConfig.isRecording = false;
        GlobalConfig.stPhaseAudioRecord.stopRecording();
        GlobalConfig.stPhaseProxy.destroy();
        GlobalConfig.stWaveFileUtil.destroy();
    }

    //runs when the stop button is pressed
    public void stopRecording(View view) throws IOException {
        Log.i("audio","Stopped");
        stopRecordingAction();
        //UI
        view.setClickable(false);
        Button btn=(Button)findViewById(R.id.button2);
        btn.setClickable(true);
    }

    //any code below this comment can be neglected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
