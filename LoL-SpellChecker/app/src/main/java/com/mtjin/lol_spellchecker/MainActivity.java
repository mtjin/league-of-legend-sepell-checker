package com.mtjin.lol_spellchecker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.SpeechRecognition;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity implements OnSpeechRecognitionListener, OnSpeechRecognitionPermissionListener, CaulyInterstitialAdListener, CaulyCloseAdListener {

    //카울리광고
   private boolean showInterstitial = false;
    private static final String APP_CODE = "AAAAA"; // 광고 요청을 위한 App Code CAULY
    CaulyCloseAd mCloseAd ;

    // Back Key가 눌러졌을 때, CloseAd 호출
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
            if (mCloseAd.isModuleLoaded())
            {
                mCloseAd.show(this);
            }
            else
            {
                // 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
                showDefaultClosePopup();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDefaultClosePopup()
    {
        new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요",null)
                .show();
    }

    // CaulyCloseAdListener
    @Override
    public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,String errMsg) {
    }
    // CloseAd의 광고를 클릭하여 앱을 벗어났을 경우 호출되는 함수이다.
    @Override
    public void onLeaveCloseAd(CaulyCloseAd ad) {
    }
    // CloseAd의 request()를 호출했을 때, 광고의 여부를 알려주는 함수이다.
    @Override
    public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {

    }
    //왼쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
    @Override
    public void onLeftClicked(CaulyCloseAd ad) {

    }
    //오른쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
    //Default로는 오른쪽 버튼이 종료로 설정되어있다.
    @Override
    public void onRightClicked(CaulyCloseAd ad) {
        finish();
    }
    @Override
    public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {
    }

    // Activity 버튼 처리
    // - 전면 광고 요청 버튼
    public void onRequestInterstitial(View button) {

        // CaulyAdInfo 생성
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).build();

        // 전면 광고 생성
        CaulyInterstitialAd interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(this);

        // 전면광고 노출 후 back 버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
        // 단, requestInterstitialAd 위에서 추가되어야 합니다.
        // interstitialAd.disableBackKey();

        // 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
        interstial.requestInterstitialAd(this);
        // 전면 광고 노출 플래그 활성화
        showInterstitial = true;
    }

    // - 전면 광고 노출 취소 버튼
    public void onCancelInterstitial(View button) {
        // 전면 광고 노출 플래그 비활성화
        showInterstitial = false;
    }

    // CaulyInterstitialAdListener
    //전면 광고의 경우, 광고 수신 후 자동으로 노출되지 않으므로,
    //반드시 onReceiveInterstitialAd 메소드에서 노출 처리해 주어야 한다.
    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        }else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }
        // 노출 활성화 상태이면, 광고 노출
        if (showInterstitial)
            ad.show();
        else
            ad.cancel();
    }
    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errorCode, String errorMsg) {
        // 전면 광고 수신 실패할 경우 호출됨.
        Log.d("CaulyExample", "failed to receive interstitial AD.");
    }
    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {

    }

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
    Button usageButton; //사용법버튼
    Switch aSwitch;
    Switch bSwitch;
    Switch cSwitch;

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


    final static String TAG = "MainTAG";

    //구글음성인식용(SST)
    private Intent mIntent;
    SpeechRecognizer mRecognizer;
    SpeechRecognition speechRecognition;
    //스레드개수
    ExecutorService threadPool = Executors.newFixedThreadPool(12);

    //음성출력(TTS)
    private TextToSpeech tts;

    final int PERMISSION = 1;


    @Override
    protected void onResume() {
        super.onResume();
        if (mCloseAd != null)
            mCloseAd.resume(this); // 필수 호출
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
        if (s.trim().equals("탑") || s.trim().equals("타압") || s.trim().equals("탓") || s.trim().equals("팝") || s.trim().equals("탁") || s.trim().equals("탐")
                || s.trim().equals("답") || s.trim().equals("닭") || s.trim().equals("덫") | s.trim().equals("밥") || s.trim().equals("다")
                || s.trim().equals("타") || s.trim().equals("박")) {
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
        } else if (s.trim().equals("탑스") || s.trim().equals("타압스") || s.trim().equals("탓스") || s.trim().equals("팝스") || s.trim().equals("탁스") || s.trim().equals("탐스") || s.trim().equals("답스")
                || s.trim().equals("탑쓰") || s.trim().equals("타압쓰") || s.trim().equals("탓쓰") || s.trim().equals("팝쓰") || s.trim().equals("탁쓰") || s.trim().equals("탐쓰") || s.trim().equals("답쓰")
                || s.trim().equals("닥쓰") || s.trim().equals("닥스") || s.trim().equals("다스") || s.trim().equals("박세") || s.trim().equals("잡스") || s.trim().equals("다시")
                | s.trim().equals("탑플") | s.trim().equals("닥플") | s.trim().equals("타플") | s.trim().equals("터틀")) {
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
        } else if (s.trim().equals("정글") || s.trim().equals("전글") || s.trim().equals("정클") || s.trim().equals("점글") || s.trim().equals("정그")
                || s.trim().equals("정걸") || s.trim().equals("전걸") || s.trim().equals("전갈") || s.trim().equals("전골")) {
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
        } else if (s.trim().equals("정글스") || s.trim().equals("전글스") || s.trim().equals("정클스") || s.trim().equals("점글스") || s.trim().equals("정그스")
                || s.trim().equals("정걸스") || s.trim().equals("전걸스") || s.trim().equals("정글s") || s.trim().equals("정글 s") || s.trim().equals("전 결승")
                || s.trim().equals("전결승") || s.trim().equals("잠금 어플") || s.trim().equals("정글 풀") || s.trim().equals("정 베풀") || s.trim().equals("정 급해")
                || s.trim().equals("정글 파일") || s.trim().equals("정글 플") || s.trim().equals("정갑철") || s.trim().equals("정글 클") || s.trim().equals("정 베플")
                || s.trim().equals("정글펫")) {
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
        } else if (s.trim().equals("미드") || s.trim().equals("미들") || s.trim().equals("미드을") || s.trim().equals("미덜") || s.trim().equals("미딜")
                || s.trim().equals("비들") || s.trim().equals("미든") || s.trim().equals("리드") || s.trim().equals("매드")|| s.trim().equals("미래")) {
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
        } else if (s.trim().equals("미드스") || s.trim().equals("이글스") || s.trim().equals("위더스") || s.trim().equals("리더스") || s.trim().equals("미디어스")
                || s.trim().equals("미즈") || s.trim().equals("미스") || s.trim().equals("미드쓰") || s.trim().equals("미들스") || s.trim().equals("위디스") || s.trim().equals("미드 스")
                || s.trim().equals("미래에셋")  || s.trim().equals("미드 플")  || s.trim().equals("미드 풀")  || s.trim().equals("리플")  || s.trim().equals("미드 클")
                || s.trim().equals("매드피플") || s.trim().equals("이대팔")
                || s.trim().equals("미드 8") || s.trim().equals("애플") || s.trim().equals("뷰티풀")) {
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
        } else if (s.trim().equals("ad") || s.trim().equals("ag") || s.trim().equals("에이디") || s.trim().equals("에디") || s.trim().equals("애이디")
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
        } else if (s.trim().equals("ads") || s.trim().equals("에이디스") || s.trim().equals("에디슨") || s.trim().equals("레이디스") || s.trim().equals("앨리스")
                || s.trim().equals("azs") || s.trim().equals("에이드스")  || s.trim().equals("adpr")  || s.trim().equals("Adele")  || s.trim().equals("에듀플")
                || s.trim().equals("데드풀")  || s.trim().equals("AD 풀") || s.trim().equals("AD 플") || s.trim().equals("애기풀")  || s.trim().equals("에이지플")
                || s.trim().equals("엘디플")  || s.trim().equals("LG 풀")  || s.trim().equals("에듀플")  || s.trim().equals("AV 풀")  || s.trim().equals("원 데이트")
                || s.trim().equals("원더풀")  || s.trim().equals("언제 풀")  || s.trim().equals("원딜 풀")  || s.trim().equals("원 대패")  || s.trim().equals("원재필")
                || s.trim().equals("언제 풀")  || s.trim().equals("원 들풀")  || s.trim().equals("원주 애플")  || s.trim().equals("원들 풀") || s.trim().equals("애니 클")
                || s.trim().equals("애니플") || s.trim().equals("돼지풀")  || s.trim().equals("원 제클") ) {

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
        } else if (s.trim().equals("서포터") || s.trim().equals("섯포터") || s.trim().equals("서포털") || s.trim().equals("스퍼터") || s.trim().equals("써포터")
                || s.trim().equals("support") || s.trim().equals("supporter") || s.trim().equals("서폿") || s.trim().equals("Super") || s.trim().equals("super") || s.trim().equals("수퍼")) {
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
        } else if (s.trim().equals("서포터스") || s.trim().equals("섯포터스") || s.trim().equals("서포털스") || s.trim().equals("스퍼터스") || s.trim().equals("써포터스")
                || s.trim().equals("supports") || s.trim().equals("supporters") || s.trim().equals("서퍼스") || s.trim().equals("서폿스") || s.trim().equals("소프트하우스")
                || s.trim().equals("서커스") || s.trim().equals("써플")  || s.trim().equals("서플")  || s.trim().equals("서커스")  || s.trim().equals("서커스")
                || s.trim().equals("서포터 풀")  || s.trim().equals("버터플")  || s.trim().equals("서포터 펜")  || s.trim().equals("스포탑 펫")  || s.trim().equals("서포터 어플")
                || s.trim().equals("서포터 호텔")) {
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

        //음성인식 퍼미션
        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        //CloseAd 초기화
        CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
        mCloseAd = new CaulyCloseAd();

 			/*  Optional
 			//원하는 버튼의 문구를 설정 할 수 있다.
 			mCloseAd.setButtonText("취소", "종료");
 			//원하는 텍스트의 문구를 설정 할 수 있다.
 			mCloseAd.setDescriptionText("종료하시겠습니까?");
 			*/
        mCloseAd.setAdInfo(closeAdInfo);
        mCloseAd.setCloseAdListener(this); // CaulyCloseAdListener 등록
        // 종료광고 노출 후 back버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
         mCloseAd.disableBackKey();

        //음성인식
        mIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        speechRecognition = new SpeechRecognition(this);
        speechRecognition.setSpeechRecognitionPermissionListener(this);
        speechRecognition.setSpeechRecognitionListener(this);

        //음성출력
        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

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
        usageButton = findViewById(R.id.usuage_btn);
        aSwitch = findViewById(R.id.switch1);
        bSwitch = findViewById(R.id.switch2);
        cSwitch = findViewById(R.id.switch3);
        //진동
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //보이스스위치
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Speak spell to the ringtone timing", Toast.LENGTH_LONG).show();
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

        //사용법버튼
        usageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UsageDialogActivity.class);
                startActivity(intent);
               /* final CharSequence[] usageModels = {"한국말 사용법", "ENGLISH USAGE"};
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getApplicationContext());
                //alt_bld.setIcon(R.drawable.icon);
                alt_bld.setTitle("사용법 USAGE");
                alt_bld.setSingleChoiceItems(usageModels, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(MainActivity.this, usageModels[item] + "가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        if (item == 0) {
                            Intent intent = new Intent(MainActivity.this, UsageActivity.class);
                            startActivity(intent);
                        } else if (item == 1) {
                            Intent intent = new Intent(MainActivity.this, Usage2Activity.class);
                            startActivity(intent);
                        }
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();*/
            }
        });

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
            while ((isCancelled() == false) && bSwitch.isChecked()) { //종료되거나 stop안누른경우
                publishProgress();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //  Toast.makeText(getApplicationContext(), "Speak spell to the ringtone timing", Toast.LENGTH_SHORT).show();
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("Top first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("Top second Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("Jungle first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }

                    if (cSwitch.isChecked()) {
                        tts.speak("Jungle second Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("mid first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("mid second Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("AD carry first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("AD carry first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("Supporter first Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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
                if (values[0].intValue() == 5) {
                    if (aSwitch.isChecked()) {
                        vibrator.vibrate(1000); // 1초간 진동
                    }
                    if (cSwitch.isChecked()) {
                        tts.speak("Supporter second Spell left 5 seconds", TextToSpeech.QUEUE_FLUSH, null);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
