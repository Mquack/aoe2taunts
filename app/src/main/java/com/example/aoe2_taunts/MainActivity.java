package com.example.aoe2_taunts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;
//import com.example.aoe2_taunts.R;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    MediaPlayer mediaPlayer;
    int btnAmount = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int colm = width/250;
        int row = btnAmount/colm;
        if (btnAmount%colm != 0){
            row += 1;
        }
        Toast.makeText(this,  String.valueOf(colm) + " and " + String.valueOf(row), Toast.LENGTH_SHORT).show();
        //TableRow tRow = new TableRow(this);
        TableRow tRow = (TableRow)findViewById(R.id.lastrow);
        //tRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        ImageButton newBtn = new ImageButton(this);
        //newBtn.setImageResource(R.drawable.my_granny);

        newBtn.setTag("my_granny");
        TypedValue outVal = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outVal, true);
        newBtn.setBackgroundResource(R.drawable.my_granny);
        newBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //added to screen
        tRow.addView(newBtn);
        //get factor to multiply with pixel value to get dp's
        float dpFactor = getResources().getDisplayMetrics().density;
        //determin params for buttons
        TableRow.LayoutParams params = (TableRow.LayoutParams) newBtn.getLayoutParams();
        params.height = (int)(75.0 * dpFactor);
        params.width = params.height;
        //params.weight = 1f;
        //params.width = 0;
        params.bottomMargin = (int)(10.0 * dpFactor);
        params.leftMargin = params.bottomMargin;
        params.rightMargin = params.bottomMargin;
        params.topMargin = params.bottomMargin;

        newBtn.setLayoutParams(params);
    }
    public void playSounds(View view) {
        //Toast.makeText(this,  "pressed", Toast.LENGTH_SHORT).show();
        //Log.d("GETID", view.getTag().toString());

        //get tag from button.
        String audiofile = view.getTag().toString();
        //get audio id with the tag.
        Resources res = context.getResources();
        int soundId = res.getIdentifier(audiofile, "raw", context.getPackageName());

        //try to play audio file
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
}