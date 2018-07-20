package com.abhatem.balancingplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sp = this.getSharedPreferences("appSettings", Context.MODE_PRIVATE);

        final Button button = (Button)findViewById(R.id.ok_btn);
        final SeekBar thresh_val_seek = (SeekBar)findViewById(R.id.thresh_val);
        final TextView seekBarValue = (TextView)findViewById(R.id.seekBarVal);
        final CheckBox ifchk = (CheckBox)findViewById(R.id.showInputCHK);

        thresh_val_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        ifchk.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


        thresh_val_seek.setProgress((sp.getInt("Threshold", 50)));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int thresh = thresh_val_seek.getProgress();
                boolean showInput = ifchk.isChecked();
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("Threshold", thresh);
                editor.putBoolean("showInputFrame", showInput);
                editor.commit();
                returnToMain();
            }
        });
    }

    void returnToMain() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}
