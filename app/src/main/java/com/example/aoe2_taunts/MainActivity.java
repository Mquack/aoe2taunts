package com.example.aoe2_taunts;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    MediaPlayer mediaPlayer;
    int btnAmount = 42;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int colm = width/250;
        int row = btnAmount/colm;
        if (btnAmount%colm != 0){
            row += 1;
        }
        Toast.makeText(this, colm + " and " + row, Toast.LENGTH_SHORT).show();
        TableLayout mainTableLayout = findViewById(R.id.mainlayout);
        //TableRow tRow = findViewById(R.id.firstrow);

        Field[] fields=R.raw.class.getFields();
        for (Field field : fields) {
            TableRow rowwy = new TableRow(this);
            TableLayout.LayoutParams tparams = (TableLayout.LayoutParams) rowwy.getLayoutParams();
            rowwy.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            mainTableLayout.addView(rowwy);

            //tparams.topMargin = 10;

            ImageButton newBtn = new ImageButton(this);
            newBtn.setTag(field.getName());
            newBtn.setImageResource(this.getResources().getIdentifier(field.getName(), "drawable", context.getPackageName()));
            newBtn.setBackgroundResource(0);
            newBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);

            //added to screen
            rowwy.addView(newBtn);
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

        mediaPlayer = MediaPlayer.create(context, soundId);
        try{
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(context, soundId);
            }
            mediaPlayer.start();
        }
        catch (Exception e){e.printStackTrace();}
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setClickableAnimation(ImageButton imgBtn)
    {
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(
                android.R.attr.actionBarItemBackground, outValue, true);
        imgBtn.setForeground(getDrawable(outValue.resourceId));
    }
}