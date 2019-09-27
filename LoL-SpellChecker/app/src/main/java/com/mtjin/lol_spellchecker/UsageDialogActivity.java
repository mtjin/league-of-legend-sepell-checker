package com.mtjin.lol_spellchecker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UsageDialogActivity extends AppCompatActivity {
    Button koreaButton;
    Button englishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_dialog);
        koreaButton = findViewById(R.id.usagedialog_btn_korea);
        englishButton = findViewById(R.id.usagedialog_btn_english);
        koreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UsageActivity.class);
                startActivity(intent);
            }
        });

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Usage2Activity.class);
                startActivity(intent);
            }
        });
    }
}
