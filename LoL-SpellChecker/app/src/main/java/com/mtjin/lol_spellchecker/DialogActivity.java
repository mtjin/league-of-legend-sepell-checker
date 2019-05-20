package com.mtjin.lol_spellchecker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class DialogActivity extends AppCompatActivity {
    ImageView exhaustImageView;
    ImageView flashImageView;
    ImageView gangtaImageView;
    ImageView healImageView;
    ImageView jumhwaImageView;
    ImageView junghwaImageView;
    ImageView sheildImageView;
    ImageView teleportImageView;
    ImageView youchehwaImageView;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        flashImageView = findViewById(R.id.dialog_flash);
        exhaustImageView = findViewById(R.id.dialog_exhausted);
        gangtaImageView = findViewById(R.id.dialog_gangta);
        healImageView = findViewById(R.id.dialog_heal);
        jumhwaImageView = findViewById(R.id.dialog_jumhwa);
        junghwaImageView = findViewById(R.id.dialog_junghwa);
        sheildImageView = findViewById(R.id.dialog_shield);
        teleportImageView = findViewById(R.id.dialog_teleport);
        youchehwaImageView = findViewById(R.id.dialog_youchehwa);
        exhaustImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainTAG", "탈진통과");
                intent = new Intent();
                intent.putExtra("name","exhausted");
                intent.putExtra("time", 210);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        flashImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainTAG", "탈진통과");
                intent = new Intent();
                intent.putExtra("name","flash");
                intent.putExtra("time", 300);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        gangtaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainTAG", "탈진통과");
                intent = new Intent();
                intent.putExtra("name","gangta");
                intent.putExtra("time", 15);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        healImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","heal");
                intent.putExtra("time", 240);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        jumhwaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","jumhwa");
                intent.putExtra("time", 180);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        junghwaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","junghwa");
                intent.putExtra("time", 210);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        sheildImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","sheild");
                intent.putExtra("time", 180);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        teleportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","teleport");
                intent.putExtra("time", 360);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
        youchehwaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("name","youchehwa");
                intent.putExtra("time", 180);
                setResult(Activity.RESULT_OK , intent);
                finish();
            }
        });
    }
}
