package com.mtjin.lol_spellchecker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
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

    String name; //스펠이름
    int time; //스펠시간초

    final static String TAG = "MainTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        startButton = findViewById(R.id.startbtn);


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

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        spell11AsyncTask.execute(Integer.valueOf(spell11TextView.getText().toString().trim()));
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
                        spell12AsyncTask.execute(Integer.valueOf(spell12TextView.getText().toString().trim()));
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
                        spell21AsyncTask.execute(Integer.valueOf(spell21TextView.getText().toString().trim()));
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
                        spell22AsyncTask.execute(Integer.valueOf(spell22TextView.getText().toString().trim()));
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
                        spell31AsyncTask.execute(Integer.valueOf(spell31TextView.getText().toString().trim()));
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
                        spell32AsyncTask.execute(Integer.valueOf(spell32TextView.getText().toString().trim()));
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
                        spell41AsyncTask.execute(Integer.valueOf(spell41TextView.getText().toString().trim()));
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
                        spell42AsyncTask.execute(Integer.valueOf(spell42TextView.getText().toString().trim()));
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
                        spell51AsyncTask.execute(Integer.valueOf(spell51TextView.getText().toString().trim()));
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
                        spell52AsyncTask.execute(Integer.valueOf(spell52TextView.getText().toString().trim()));
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
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell11TextView.setText(time + "");
            setSpellImage(request11, name);
        } else if (requestCode == request12 && resultCode == RESULT_OK) {
            Log.d(TAG, "스펠12 결과통과");
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell12TextView.setText(time + "");
            setSpellImage(request12, name);
        } else if (requestCode == request21 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell21TextView.setText(time + "");
            setSpellImage(request21, name);
        } else if (requestCode == request22 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell22TextView.setText(time + "");
            setSpellImage(request22, name);
        } else if (requestCode == request31 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell31TextView.setText(time + "");
            setSpellImage(request31, name);
        } else if (requestCode == request32 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell32TextView.setText(time + "");
            setSpellImage(request32, name);
        } else if (requestCode == request41 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell41TextView.setText(time + "");
            setSpellImage(request41, name);
        } else if (requestCode == request42 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell42TextView.setText(time + "");
            setSpellImage(request42, name);
        } else if (requestCode == request51 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell51TextView.setText(time + "");
            setSpellImage(request51, name);
        } else if (requestCode == request52 && resultCode == RESULT_OK) {
            name = data.getExtras().getString("name");
            time = data.getExtras().getInt("time");
            spell52TextView.setText(time + "");
            setSpellImage(request52, name);
        }
    }

    public void setSpellImage(int position, String name) {
        if (position == request11) {
            if (name.equals("exhausted")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell11TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request12) {
            if (name.equals("exhausted")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell12TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request21) {
            if (name.equals("exhausted")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell21TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request22) {
            if (name.equals("exhausted")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell22TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request31) {
            if (name.equals("exhausted")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell31TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request32) {
            if (name.equals("exhausted")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell32TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request41) {
            if (name.equals("exhausted")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell41TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request42) {
            if (name.equals("exhausted")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell42TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request51) {
            if (name.equals("exhausted")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell51TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
        } else if (position == request52) {
            if (name.equals("exhausted")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exhastued, 0, 0, 0);
            } else if (name.equals("flash")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flash, 0, 0, 0);
            } else if (name.equals("gangta")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gangta, 0, 0, 0);
            } else if (name.equals("heal")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heal, 0, 0, 0);
            } else if (name.equals("jumhwa")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jumhwa, 0, 0, 0);
            } else if (name.equals("junghwa")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.junghwa, 0, 0, 0);
            } else if (name.equals("sheild")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sheild, 0, 0, 0);
            } else if (name.equals("teleport")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teleport, 0, 0, 0);
            } else if (name.equals("youchehwa")) {
                spell52TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.youchehwa, 0, 0, 0);
            }
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
            if (values[0].intValue() <= 30) {
                spell11TextView.setTextColor(Color.parseColor("#FF1000"));
                spell11TextView.setText(values[0].toString());
            } else {
                spell11TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell11TextView.setTextColor(Color.parseColor("#000000"));
            spell11TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell11TextView.setTextColor(Color.parseColor("#000000"));
            spell11TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell12TextView.setTextColor(Color.parseColor("#FF1000"));
                spell12TextView.setText(values[0].toString());
            } else {
                spell12TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell12TextView.setTextColor(Color.parseColor("#000000"));
            spell12TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell12TextView.setTextColor(Color.parseColor("#000000"));
            spell12TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell21TextView.setTextColor(Color.parseColor("#FF1000"));
                spell21TextView.setText(values[0].toString());
            } else {
                spell21TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell21TextView.setTextColor(Color.parseColor("#000000"));
            spell21TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell21TextView.setTextColor(Color.parseColor("#000000"));
            spell21TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell22TextView.setTextColor(Color.parseColor("#FF1000"));
                spell22TextView.setText(values[0].toString());
            } else {
                spell22TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell22TextView.setTextColor(Color.parseColor("#000000"));
            spell22TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell22TextView.setTextColor(Color.parseColor("#000000"));
            spell22TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell31TextView.setTextColor(Color.parseColor("#FF1000"));
                spell31TextView.setText(values[0].toString());
            } else {
                spell31TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell31TextView.setTextColor(Color.parseColor("#000000"));
            spell31TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell31TextView.setTextColor(Color.parseColor("#000000"));
            spell31TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell32TextView.setTextColor(Color.parseColor("#FF1000"));
                spell32TextView.setText(values[0].toString());
            } else {
                spell32TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell32TextView.setTextColor(Color.parseColor("#000000"));
            spell32TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell32TextView.setTextColor(Color.parseColor("#000000"));
            spell32TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell41TextView.setTextColor(Color.parseColor("#FF1000"));
                spell41TextView.setText(values[0].toString());
            } else {
                spell41TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell41TextView.setTextColor(Color.parseColor("#000000"));
            spell41TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell41TextView.setTextColor(Color.parseColor("#000000"));
            spell41TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell42TextView.setTextColor(Color.parseColor("#FF1000"));
                spell42TextView.setText(values[0].toString());
            } else {
                spell42TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell42TextView.setTextColor(Color.parseColor("#000000"));
            spell42TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell42TextView.setTextColor(Color.parseColor("#000000"));
            spell42TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell51TextView.setTextColor(Color.parseColor("#FF1000"));
                spell51TextView.setText(values[0].toString());
            } else {
                spell51TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell51TextView.setTextColor(Color.parseColor("#000000"));
            spell51TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell51TextView.setTextColor(Color.parseColor("#000000"));
            spell51TextView.setText(originalTIme + "");
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
            if (values[0].intValue() <= 30) {
                spell52TextView.setTextColor(Color.parseColor("#FF1000"));
                spell52TextView.setText(values[0].toString());
            } else {
                spell52TextView.setText(values[0].toString());
            }
        }

        //작업종료 후 원래시간으로 세팅
        @Override
        protected void onPostExecute(Integer integer) {
            spell52TextView.setTextColor(Color.parseColor("#000000"));
            spell52TextView.setText(originalTIme + "");
        }

        @Override
        protected void onCancelled() {
            spell52TextView.setTextColor(Color.parseColor("#000000"));
            spell52TextView.setText(originalTIme + "");
        }

    }

}
