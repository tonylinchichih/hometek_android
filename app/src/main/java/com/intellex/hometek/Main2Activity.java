package com.intellex.hometek;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.intellex.hometek.sip.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // setTitle(R.string.security_title);
    }

    public void setActionBarTitle(int str_id) {
        ((TextView) findViewById(R.id.page_title2)).setText(str_id);
    }

    public void clearActionBarTitle() {
        ((TextView) findViewById(R.id.page_title2)).setText("");
    }
}
