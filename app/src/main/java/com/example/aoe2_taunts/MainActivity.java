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


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int colm = width/250;

        TableLayout mainTableLayout = findViewById(R.id.mainlayout);

        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        mainTableLayout.addView(tableRow);

        List<String> fields= Arrays.stream(R.raw.class.getFields())
                .map(Field::getName).sorted((m1, m2) -> {
                    String sub1 = m1.substring(m1.length() - 2);
                    String sub2 = m2.substring(m2.length() - 2);
                    if (sub1.charAt(0) == 0) {
                        sub1 = "" + sub1.charAt(1);
                    }
                    if (sub2.charAt(0) == 0) {
                        sub2 = "" + sub2.charAt(1);
                    }
                    int first = Integer.parseInt(sub1);
                    int second = Integer.parseInt(sub2);
                    if (first > second) {
                        return 1;
                    } else if (second > first) {
                        return -1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        int currentColumn = 0;
        for (String field : fields) {
            if(currentColumn == colm) {
                tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                mainTableLayout.addView(tableRow);
                currentColumn = 0;
            }
            currentColumn++;

            ImageButton newBtn = new ImageButton(context);
            newBtn.setTag(field);
            newBtn.setImageResource(this.getResources().getIdentifier(field, "drawable", context.getPackageName()));
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
            params.height = (int)(90 * dpFactor);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setClickableAnimation(ImageButton imgBtn)
    {
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        imgBtn.setBackground(ContextCompat.getDrawable(context, outValue.resourceId));
    }
}