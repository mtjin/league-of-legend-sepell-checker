package com.mtjin.lol_spellchecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.SpeechRecognition;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements OnSpeechRecognitionListener, OnSpeechRecognitionPermissionListener {
    TextView spell11TextView;
    TextView spell12TextView;
    TextView spell21TextView;
    TextView spell22TextView;
    TextView spell31TextView;
    TextView spell32TextView;
    TextView spell41TextView;
    TextView spell42TextView;
    TextView spell51TextView;
    TextView spell52TextView;
    Button startButton;
    Button searchButton;
    Switch aSwitch;
    Switch bSwitch;

    Boolean isStart;

    final static int request11 = 11;
    final static int request12 = 12;
    final static int request21 = 21;
    final static int request22 = 22;
    final static int request31 = 31;
    final static int request32 = 32;
    final static int request41 = 41;
    final static int request42 = 42;
    final static int request51 = 51;
    final static int request52 = 52;

    //Asynctask
    Spell11AsyncTask spell11AsyncTask;
    Spell12AsyncTask spell12AsyncTask;
    Spell21AsyncTask spell21AsyncTask;
    Spell22AsyncTask spell22AsyncTask;
    Spell31AsyncTask spell31AsyncTask;
    Spell32AsyncTask spell32AsyncTask;
    Spell41AsyncTask spell41AsyncTask;
    Spell42AsyncTask spell42AsyncTask;
    Spell51AsyncTask spell51AsyncTask;
    Spell52AsyncTask spell52AsyncTask;
    VoiceAsyncTask voiceAsyncTask;

    //초기스펠이름
    String name11;
    String name12;
    String name21;
    String name22;
    String name31;
    String name32;
    String name41;
    String name42;
    String name51;
    String name52;

    //진동
    private Vibrator vibrator;

    String name; //스펠이름
    int time; //스펠시간초

    //애드몹
    private InterstitialAd mInterstitialAd;

    final static String TAG = "MainTAG";

    //구글음성인식용(SST)
    private Intent mIntent;
    SpeechRecognizer mRecognizer;
     SpeechRecognition speechRecognition;
    //스레드개수
    ExecutorService threadPool = Executors.newFixedThreadPool(12);

    final int PERMISSION = 1;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void OnSpeechRecognitionStarted() {
        Log.d(TAG, "음성라이브러리 OnSpeechRecognitionStarted");
    }

    @Override
    public void OnSpeechRecognitionStopped() {
        Log.d(TAG, "음성라이브러리 OnSpeechRecognitionStopped");
    }

    @Override
    public void OnSpeechRecognitionFinalResult(String s) {
        Log.d(TAG, "OnSpeechRecognitionFinalResult:        " + s);
        if (s.trim().equals("탑") || s.trim().equals("타압") || s.trim().equals("탓") || s.trim().equals("팝") || s.trim().equals("탁") || s.trim().equals("탐") || s.trim().equals("답")) {
            if (isStart) {
                if (spell11AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell11AsyncTask.cancel(true);
                    spell11AsyncTask = new Spell11AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell11AsyncTask = new Spell11AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell11AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell11TextView.getText().toString().trim()));
                    } else {
                        spell11AsyncTask.execute(Integer.valueOf(spell11TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request11);
            }
        }
        else if(s.trim().equals("탑스") || s.trim().equals("타압스") || s.trim().equals("탓스") || s.trim().equals("팝스") || s.trim().equals("탁스") || s.trim().equals("탐스") || s.trim().equals("답스")
                || s.trim().equals("탑쓰") || s.trim().equals("타압쓰") || s.trim().equals("탓쓰") || s.trim().equals("팝쓰") || s.trim().equals("탁쓰") || s.trim().equals("탐쓰") || s.trim().equals("답쓰")
                || s.trim().equals("닥쓰") || s.trim().equals("닥스")){
            if (isStart) {
                if (spell12AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell12AsyncTask.cancel(true);
                    spell12AsyncTask = new Spell12AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell12AsyncTask = new Spell12AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell12AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell12TextView.getText().toString().trim()));
                    } else {
                        spell12AsyncTask.execute(Integer.valueOf(spell12TextView.getText().toString().trim()));
                    }
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request12);
            }
        }
        else  if (s.trim().equals("정글") || s.trim().equals("전글") || s.trim().equals("정클") || s.trim().equals("점글") || s.trim().equals("정그")
                || s.trim().equals("정걸") || s.trim().equals("전걸")) {
            if (isStart) {
                if (spell21AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell21AsyncTask.cancel(true);
                    spell21AsyncTask = new Spell21AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell21AsyncTask = new Spell21AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell21AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell21TextView.getText().toString().trim()));
                    } else {
                        spell21AsyncTask.execute(Integer.valueOf(spell21TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request21);
            }
        }
        else  if (s.trim().equals("정글스") || s.trim().equals("전글스") || s.trim().equals("정클스") || s.trim().equals("점글스") || s.trim().equals("정그스")
                || s.trim().equals("정걸스") || s.trim().equals("전걸스")|| s.trim().equals("정글s") || s.trim().equals("정글 s")) {
            if (isStart) {
                if (spell22AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell22AsyncTask.cancel(true);
                    spell22AsyncTask = new Spell22AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell22AsyncTask = new Spell22AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell22AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell22TextView.getText().toString().trim()));
                    } else {
                        spell22AsyncTask.execute(Integer.valueOf(spell22TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request22);
            }
        }
        else  if (s.trim().equals("미드") || s.trim().equals("미들") || s.trim().equals("미드을") || s.trim().equals("미덜") || s.trim().equals("미딜")
                || s.trim().equals("비들") || s.trim().equals("미든")) {
            if (isStart) {
                if (spell31AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell31AsyncTask.cancel(true);
                    spell31AsyncTask = new Spell31AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell31AsyncTask = new Spell31AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell31AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell31TextView.getText().toString().trim()));
                    } else {
                        spell31AsyncTask.execute(Integer.valueOf(spell31TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request31);
            }
        }
        else  if (s.trim().equals("미드스") || s.trim().equals("이글스") || s.trim().equals("위더스") || s.trim().equals("리더스") || s.trim().equals("미디어스")
                || s.trim().equals("미즈") || s.trim().equals("미스")|| s.trim().equals("미드쓰") || s.trim().equals("미들스")) {
            if (isStart) {
                if (spell32AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell32AsyncTask.cancel(true);
                    spell32AsyncTask = new Spell32AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell32AsyncTask = new Spell32AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell32AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell32TextView.getText().toString().trim()));
                    } else {
                        spell32AsyncTask.execute(Integer.valueOf(spell32TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request32);
            }
        }
        else  if (s.trim().equals("ad") || s.trim().equals("ag") || s.trim().equals("에이디") || s.trim().equals("에디") || s.trim().equals("애이디")
                || s.trim().equals("az") || s.trim().equals("에이드")) {
            if (isStart) {
                if (spell41AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell41AsyncTask.cancel(true);
                    spell41AsyncTask = new Spell41AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell41AsyncTask = new Spell41AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell41AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell41TextView.getText().toString().trim()));
                    } else {
                        spell41AsyncTask.execute(Integer.valueOf(spell41TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request41);
            }
        }
        else  if (s.trim().equals("ads") || s.trim().equals("에이디스") || s.trim().equals("에디슨") || s.trim().equals("레이디스")|| s.trim().equals("앨리스")
                || s.trim().equals("azs") || s.trim().equals("에이드스")) {
            if (isStart) {
                if (spell42AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell42AsyncTask.cancel(true);
                    spell42AsyncTask = new Spell42AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell42AsyncTask = new Spell42AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell42AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell42TextView.getText().toString().trim()));
                    } else {
                        spell42AsyncTask.execute(Integer.valueOf(spell42TextView.getText().toString().trim()));
                    }
                }
            } else {

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request42);
            }
        }
        else  if (s.trim().equals("서포터") || s.trim().equals("섯포터") || s.trim().equals("서포털") || s.trim().equals("스퍼터")|| s.trim().equals("써포터")
                || s.trim().equals("support") || s.trim().equals("supporter")|| s.trim().equals("서폿")||s.trim().equals("Super")||s.trim().equals("super")||s.trim().equals("수퍼")) {
            if (isStart) {
                if (spell51AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell51AsyncTask.cancel(true);
                    spell51AsyncTask = new Spell51AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell51AsyncTask = new Spell51AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell51AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell51TextView.getText().toString().trim()));
                    } else {
                        spell51AsyncTask.execute(Integer.valueOf(spell51TextView.getText().toString().trim()));
                    }
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request51);
            }
        }
        else  if (s.trim().equals("서포터스") || s.trim().equals("섯포터스") || s.trim().equals("서포털스") || s.trim().equals("스퍼터스")|| s.trim().equals("써포터스")
                || s.trim().equals("supports") || s.trim().equals("supporters")|| s.trim().equals("서퍼스")|| s.trim().equals("서폿스")) {
            if (isStart) {
                if (spell52AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                    spell52AsyncTask.cancel(true);
                    spell52AsyncTask = new Spell52AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                } else {
                    spell52AsyncTask = new Spell52AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        spell52AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell52TextView.getText().toString().trim()));
                    } else {
                        spell52AsyncTask.execute(Integer.valueOf(spell52TextView.getText().toString().trim()));
                    }
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                startActivityForResult(intent, request52);
            }
        }
    }

    @Override
    public void OnSpeechRecognitionCurrentResult(String s) {
        Log.d(TAG, "음성라이브러리 OnSpeechRecognitionCurrentResult");

    }

    @Override
    public void OnSpeechRecognitionError(int i, String s) {
        Log.d(TAG, "음성라이브러리 OnSpeechRecognitionError");
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 안꺼지게하기
        //구글애드몹
        MobileAds.initialize(this, "ca-app-pub-8924705805317182/3164737399");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8924705805317182/3164737399");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //음성인식 퍼미션
        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        //음성인식
        mIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
          speechRecognition = new SpeechRecognition(this);
        speechRecognition.setSpeechRecognitionPermissionListener(this);
        speechRecognition.setSpeechRecognitionListener(this);

        isStart = false;
        spell11TextView = findViewById(R.id.spell11);
        spell12TextView = findViewById(R.id.spell12);
        spell21TextView = findViewById(R.id.spell21);
        spell22TextView = findViewById(R.id.spell22);
        spell31TextView = findViewById(R.id.spell31);
        spell32TextView = findViewById(R.id.spell32);
        spell41TextView = findViewById(R.id.spell41);
        spell42TextView = findViewById(R.id.spell42);
        spell51TextView = findViewById(R.id.spell51);
        spell52TextView = findViewById(R.id.spell52);
        startButton = findViewById(R.id.startBtn);
        searchButton = findViewById(R.id.searchBtn);
        aSwitch = findViewById(R.id.switch1);
        bSwitch = findViewById(R.id.switch2);
        //진동
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //보이스스위치
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Say Spell in 2.5 seconds(Speak to the ringtone timing)", Toast.LENGTH_LONG).show();
                        voiceAsyncTask.cancel(true);
                        voiceAsyncTask = new VoiceAsyncTask();
                        voiceAsyncTask.executeOnExecutor(threadPool);
                }
            }
        });


        spell11AsyncTask = new Spell11AsyncTask();
        spell12AsyncTask = new Spell12AsyncTask();
        spell21AsyncTask = new Spell21AsyncTask();
        spell22AsyncTask = new Spell22AsyncTask();
        spell31AsyncTask = new Spell31AsyncTask();
        spell32AsyncTask = new Spell32AsyncTask();
        spell41AsyncTask = new Spell41AsyncTask();
        spell42AsyncTask = new Spell42AsyncTask();
        spell51AsyncTask = new Spell51AsyncTask();
        spell52AsyncTask = new Spell52AsyncTask();
        voiceAsyncTask = new VoiceAsyncTask();

        //초기스펠값
        name11 = "teleport";
        name12 = "flash";
        name21 = "gangta";
        name22 = "flash";
        name31 = "teleport";
        name32 = "flash";
        name41 = "heal";
        name42 = "flash";
        name51 = "jumhwa";
        name52 = "flash";

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.op.gg/l=ko_KR"));
                startActivity(intent);
            }
        });

        //시작버튼
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구글애드몹
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
            /*    if(bSwitch.isChecked()) {
                    voiceAsyncTask.cancel(true);
                    voiceAsyncTask = new VoiceAsyncTask();
                    voiceAsyncTask.executeOnExecutor(threadPool);
                }else{
                    voiceAsyncTask.cancel(true);
                }*/

                if (!isStart) { //시작
                    isStart = true;
                    startButton.setText("TIMER STOP");
                } else { //중지
                    isStart = false;
                    //실행중인 스레드 다 종료
                    if (spell11AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell11AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell12AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell12AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell21AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell21AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell22AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell22AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell31AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell31AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell32AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell32AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell41AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell41AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell42AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell42AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell51AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell51AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    if (spell52AsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        spell52AsyncTask.cancel(true);
                        Log.d(TAG, "스레드실행중이던 거 종료");
                    }
                    startButton.setText("TIMER START");
                }
            }
        });

        spell11TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell11AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell11AsyncTask.cancel(true);
                        spell11AsyncTask = new Spell11AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell11AsyncTask = new Spell11AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell11AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell11TextView.getText().toString().trim()));
                        } else {
                            spell11AsyncTask.execute(Integer.valueOf(spell11TextView.getText().toString().trim()));
                        }
                    }
                } else {

                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request11);
                }
            }
        });
        spell12TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell12AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell12AsyncTask.cancel(true);
                        spell12AsyncTask = new Spell12AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell12AsyncTask = new Spell12AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell12AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell12TextView.getText().toString().trim()));
                        } else {
                            spell12AsyncTask.execute(Integer.valueOf(spell12TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request12);
                }
            }
        });
        spell21TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell21AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell21AsyncTask.cancel(true);
                        spell21AsyncTask = new Spell21AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell21AsyncTask = new Spell21AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell21AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell21TextView.getText().toString().trim()));
                        } else {
                            spell21AsyncTask.execute(Integer.valueOf(spell21TextView.getText().toString().trim()));
                        }

                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request21);
                }
            }
        });
        spell22TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell22AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell22AsyncTask.cancel(true);
                        spell22AsyncTask = new Spell22AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell22AsyncTask = new Spell22AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell22AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell22TextView.getText().toString().trim()));
                        } else {
                            spell22AsyncTask.execute(Integer.valueOf(spell22TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request22);
                }
            }
        });
        spell31TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell31AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell31AsyncTask.cancel(true);
                        spell31AsyncTask = new Spell31AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell31AsyncTask = new Spell31AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell31AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell31TextView.getText().toString().trim()));
                        } else {
                            spell31AsyncTask.execute(Integer.valueOf(spell31TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request31);
                }
            }
        });
        spell32TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell32AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell32AsyncTask.cancel(true);
                        spell32AsyncTask = new Spell32AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell32AsyncTask = new Spell32AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell32AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell32TextView.getText().toString().trim()));
                        } else {
                            spell32AsyncTask.execute(Integer.valueOf(spell32TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request32);
                }
            }
        });
        spell41TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell41AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell41AsyncTask.cancel(true);
                        spell41AsyncTask = new Spell41AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell41AsyncTask = new Spell41AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell41AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell41TextView.getText().toString().trim()));
                        } else {
                            spell41AsyncTask.execute(Integer.valueOf(spell41TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request41);
                }
            }
        });
        spell42TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell42AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell42AsyncTask.cancel(true);
                        spell42AsyncTask = new Spell42AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell42AsyncTask = new Spell42AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell42AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell42TextView.getText().toString().trim()));
                        } else {
                            spell42AsyncTask.execute(Integer.valueOf(spell42TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request42);
                }
            }
        });
        spell51TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell51AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell51AsyncTask.cancel(true);
                        spell51AsyncTask = new Spell51AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell51AsyncTask = new Spell51AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell51AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell51TextView.getText().toString().trim()));
                        } else {
                            spell51AsyncTask.execute(Integer.valueOf(spell51TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request51);
                }
            }
        });
        spell52TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (spell52AsyncTask.getStatus() == AsyncTask.Status.RUNNING) { //이미 실행중인게있으면 종료 후 새스레드 생성
                        spell52AsyncTask.cancel(true);
                        spell52AsyncTask = new Spell52AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                    } else {
                        spell52AsyncTask = new Spell52AsyncTask(); //스레드 재생성 (한번 사용한 Asynctask는 재활용이 불가능하나봄)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            spell52AsyncTask.executeOnExecutor(threadPool, Integer.valueOf(spell52TextView.getText().toString().trim()));
                        } else {
                            spell52AsyncTask.execute(Integer.valueOf(spell52TextView.getText().toString().trim()));
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivityForResult(intent, request52);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request11 && resultCode == RESULT_OK) {
            name11 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell11TextView.setText(time + "");
            setSpellImage(request11, name11, true);
        } else if (requestCode == request12 && resultCode == RESULT_OK) {
            Log.d(TAG, "스펠12 결과통과");
            name12 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell12TextView.setText(time + "");
            setSpellImage(request12, name12, true);
        } else if (requestCode == request21 && resultCode == RESULT_OK) {
            name21 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell21TextView.setText(time + "");
            setSpellImage(request21, name21, true);
        } else if (requestCode == request22 && resultCode == RESULT_OK) {
            name22 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell22TextView.setText(time + "");
            setSpellImage(request22, name22, true);
        } else if (requestCode == request31 && resultCode == RESULT_OK) {
            name31 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell31TextView.setText(time + "");
            setSpellImage(request31, name31, true);
        } else if (requestCode == request32 && resultCode == RESULT_OK) {
            name32 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell32TextView.setText(time + "");
            setSpellImage(request32, name32, true);
        } else if (requestCode == request41 && resultCode == RESULT_OK) {
            name41 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell41TextView.setText(time + "");
            setSpellImage(request41, name41, true);
        } else if (requestCode == request42 && resultCode == RESULT_OK) {
            name42 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell42TextView.setText(time + "");
            setSpellImage(request42, name42, true);
        } else if (requestCode == request51 && resultCode == RESULT_OK) {
            name51 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell51TextView.setText(time + "");
            setSpellImage(request51, name51, true);
        } else if (requestCode == request52 && resultCode == RESULT_OK) {
            name52 = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell52TextView.setText(time + "");
            setSpellImage(request52, name52, true);
        }
    }

    public void setSpellImage(int position, String name, Boolean isLight) {
        if (position == request11) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request12) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request21) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request22) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request31) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request32) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request41) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request42) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request51) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        } else if (position == request52) {
            if (name.equals("exhausted")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_exhastued, 0, 0, 0);
                }
            } else if (name.equals("flash")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_flash, 0, 0, 0);
                }
            } else if (name.equals("gangta")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_gangta, 0, 0, 0);
                }
            } else if (name.equals("heal")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_heal, 0, 0, 0);
                }
            } else if (name.equals("jumhwa")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_jumhwa, 0, 0, 0);
                }
            } else if (name.equals("junghwa")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_junghwa, 0, 0, 0);
                }
            } else if (name.equals("sheild")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_sheild, 0, 0, 0);
                }
            } else if (name.equals("teleport")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_teleport, 0, 0, 0);
                }
            } else if (name.equals("youchehwa")) {
                if (isLight) {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
                } else {
                    spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_youchehwa, 0, 0, 0);
                }
            }
        }
    }

    public class VoiceAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            while ((isCancelled() == false) && bSwitch.isChecked() ) { //종료되거나 stop안누른경우
                publishProgress();
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            speechRecognition.startSpeechRecognition();
        }
    }


    public class Spell11AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간

        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request11, name11, false);
            if (values[0].intValue() <= 30) {
                spell11TextView.setTextColor(Color.parseColor("#FF1000"));
                spell11TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell11TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell11TextView.setTextColor(Color.parseColor("#000000"));
            spell11TextView.setText(originalTIme + "");
            setSpellImage(request11, name11, true);
        }

        @Override
        protected void onCancelled() {
            spell11TextView.setTextColor(Color.parseColor("#000000"));
            spell11TextView.setText(originalTIme + "");
            setSpellImage(request11, name11, true);
        }

    }

    public class Spell12AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request12, name12, false);
            if (values[0].intValue() <= 30) {
                spell12TextView.setTextColor(Color.parseColor("#FF1000"));
                spell12TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell12TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell12TextView.setTextColor(Color.parseColor("#000000"));
            spell12TextView.setText(originalTIme + "");
            setSpellImage(request12, name12, true);
        }

        @Override
        protected void onCancelled() {
            spell12TextView.setTextColor(Color.parseColor("#000000"));
            spell12TextView.setText(originalTIme + "");
            setSpellImage(request12, name12, true);
        }

    }

    public class Spell21AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간

        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request21, name21, false);
            if (values[0].intValue() <= 30) {
                spell21TextView.setTextColor(Color.parseColor("#FF1000"));
                spell21TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell21TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell21TextView.setTextColor(Color.parseColor("#000000"));
            spell21TextView.setText(originalTIme + "");
            setSpellImage(request21, name21, true);
        }

        @Override
        protected void onCancelled() {
            spell21TextView.setTextColor(Color.parseColor("#000000"));
            spell21TextView.setText(originalTIme + "");
            setSpellImage(request21, name21, true);
        }

    }

    public class Spell22AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request22, name22, false);
            if (values[0].intValue() <= 30) {
                spell22TextView.setTextColor(Color.parseColor("#FF1000"));
                spell22TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell22TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell22TextView.setTextColor(Color.parseColor("#000000"));
            spell22TextView.setText(originalTIme + "");
            setSpellImage(request22, name22, true);
        }

        @Override
        protected void onCancelled() {
            spell22TextView.setTextColor(Color.parseColor("#000000"));
            spell22TextView.setText(originalTIme + "");
            setSpellImage(request22, name22, true);
        }

    }

    public class Spell31AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request31, name31, false);
            if (values[0].intValue() <= 30) {
                spell31TextView.setTextColor(Color.parseColor("#FF1000"));
                spell31TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell31TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell31TextView.setTextColor(Color.parseColor("#000000"));
            spell31TextView.setText(originalTIme + "");
            setSpellImage(request31, name31, true);
        }

        @Override
        protected void onCancelled() {
            spell31TextView.setTextColor(Color.parseColor("#000000"));
            spell31TextView.setText(originalTIme + "");
            setSpellImage(request31, name31, true);
        }

    }

    public class Spell32AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request32, name32, false);
            if (values[0].intValue() <= 30) {
                spell32TextView.setTextColor(Color.parseColor("#FF1000"));
                spell32TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell32TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell32TextView.setTextColor(Color.parseColor("#000000"));
            spell32TextView.setText(originalTIme + "");
            setSpellImage(request32, name32, true);
        }

        @Override
        protected void onCancelled() {
            spell32TextView.setTextColor(Color.parseColor("#000000"));
            spell32TextView.setText(originalTIme + "");
            setSpellImage(request32, name32, true);
        }

    }

    public class Spell41AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request41, name41, false);
            if (values[0].intValue() <= 30) {
                spell41TextView.setTextColor(Color.parseColor("#FF1000"));
                spell41TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell41TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell41TextView.setTextColor(Color.parseColor("#000000"));
            spell41TextView.setText(originalTIme + "");
            setSpellImage(request41, name41, true);
        }

        @Override
        protected void onCancelled() {
            spell41TextView.setTextColor(Color.parseColor("#000000"));
            spell41TextView.setText(originalTIme + "");
            setSpellImage(request41, name41, true);
        }

    }

    public class Spell42AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request42, name42, false);
            if (values[0].intValue() <= 30) {
                spell42TextView.setTextColor(Color.parseColor("#FF1000"));
                spell42TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell42TextView.setText(values[0].toString());
            }

        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell42TextView.setTextColor(Color.parseColor("#000000"));
            spell42TextView.setText(originalTIme + "");
            setSpellImage(request42, name42, true);
        }

        @Override
        protected void onCancelled() {
            spell42TextView.setTextColor(Color.parseColor("#000000"));
            spell42TextView.setText(originalTIme + "");
            setSpellImage(request42, name42, true);
        }

    }

    public class Spell51AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request51, name51, false);
            if (values[0].intValue() <= 30) {
                spell51TextView.setTextColor(Color.parseColor("#FF1000"));
                spell51TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell51TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell51TextView.setTextColor(Color.parseColor("#000000"));
            spell51TextView.setText(originalTIme + "");
            setSpellImage(request51, name51, true);
        }

        @Override
        protected void onCancelled() {
            spell51TextView.setTextColor(Color.parseColor("#000000"));
            spell51TextView.setText(originalTIme + "");
            setSpellImage(request51, name51, true);
        }

    }

    public class Spell52AsyncTask extends AsyncTask<Integer, Integer, Integer> {
        int leftTime;   //스펠 남은시간
        int originalTIme; //스펠 초기시간


        @Override
        protected Integer doInBackground(Integer... value) {
            originalTIme = value[0].intValue();
            leftTime = value[0].intValue();
            while (isCancelled() == false) { //종료되거나 stop안누른경우
                if (leftTime <= 0) {
                    break;
                } else {
                    leftTime--;
                    publishProgress(leftTime); //onProgressUpdate호출
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            return leftTime;
        }

        //중간중간 UI업데이트
        @Override
        protected void onProgressUpdate(Integer... values) {
            setSpellImage(request52, name52, false);
            if (values[0].intValue() <= 30) {
                spell52TextView.setTextColor(Color.parseColor("#FF1000"));
                spell52TextView.setText(values[0].toString());
                if (aSwitch.isChecked() && values[0].intValue() == 5) {
                    vibrator.vibrate(1000); // 1초간 진동
                }
            } else {
                spell52TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell52TextView.setTextColor(Color.parseColor("#000000"));
            spell52TextView.setText(originalTIme + "");
            setSpellImage(request52, name52, true);
        }

        @Override
        protected void onCancelled() {
            spell52TextView.setTextColor(Color.parseColor("#000000"));
            spell52TextView.setText(originalTIme + "");
            setSpellImage(request52, name52, true);
        }

    }
}
