package com.swufe.secondapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText input;
    TextView t;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.editText);
        t = findViewById(R.id.textView);

    }
    public void onClick(View btn){
        String str = input.getText().toString();
        double tem = Double.parseDouble(str)*1.8+32;
        t.setText("" + tem);

    }


}
