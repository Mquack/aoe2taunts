package com.example.aoe2_taunts;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int colm = width/250;
        int btnAmount = 42;
        int rowCount = btnAmount /colm;
        if (btnAmount %colm != 0){
            rowCount += 1;
        }
        Toast.makeText(this, colm + " and " + rowCount, Toast.LENGTH_SHORT).show();
        TableLayout mainTableLayout = findViewById(R.id.mainlayout);

        TableRow tableRow = new TableRow(context);
//        TableLayout.LayoutParams tparams = (TableLayout.LayoutParams) tableRow.getLayoutParams();
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        mainTableLayout.addView(tableRow);

        Field[] fields=R.raw.class.getFields();
        int currentColumn = 0;
        for (Field field : fields) {
            if(currentColumn == colm) {
                tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                mainTableLayout.addView(tableRow);
                currentColumn = 0;
            }
            currentColumn++;

            ImageButton newBtn = new ImageButton(context);
            newBtn.setTag(field.getName());
            newBtn.setImageResource(this.getResources().getIdentifier(field.getName(), "drawable", context.getPackageName()));
            newBtn.setBackgroundResource(0);
            newBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);

            //added to screen
            tableRow.addView(newBtn);
            setClickableAnimation(newBtn);

            newBtn.setOnClickListener(this::playSounds);

            //get factor to multiply with pixel value to get dp's
            float dpFactor = getResources().getDisplayMetrics().density;
            //determine params for buttons
            TableRow.LayoutParams params = (TableRow.LayoutParams) newBtn.getLayoutParams();
            params.height = (int)(75 * dpFactor);
            params.weight = 1f;
            params.width = 0;

            newBtn.setLayoutParams(params);
        }
    }
    public void playSounds(View view) {
        Resources res = context.getResources();
        int soundId = res.getIdentifier(view.getTag().toString(), "raw", context.getPackageName());
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        }
        catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setClickableAnimation(ImageButton imgBtn)
    {
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        imgBtn.setBackground(ContextCompat.getDrawable(context, outValue.resourceId));
    }
}