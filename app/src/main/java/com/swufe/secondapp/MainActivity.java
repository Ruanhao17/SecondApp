package com.swufe.secondapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView t;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = findViewById(R.id.textView2);

    }
    public void onClick1(View btn){
         showScore(3);
    }
    public void onClick2(View btn){
        showScore(2);
    }
    public void onClick3(View btn){
        showScore(1);
    }
    public void onClick4(View btn){
        t.setText("0");
    }
    private void showScore(int i){
        String oldScore =(String) t.getText();
        int newScore = Integer.parseInt(oldScore) + i;
        t.setText("" + newScore);
    }
}
