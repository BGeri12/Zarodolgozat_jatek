package com.example.zarodolgozat_jatek;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class JatekView extends View {

    private KarakterObject karakter;
    private Handler handler;
    private Runnable r;
    private ArrayList<Akadaly> arrAkadaly;
    private int jegyhegySzikla,athaladas;
    private int score,bestscore=0;
    private boolean start;
    private Context contex;

    public JatekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.contex = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if (sp!=null){
            bestscore = sp.getInt("bestscore",0);
        }

        score = 0;
        start = false;
        initKarakter();
        initAkadaly();
       // initAkadaly2();

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

    }

    private void initAkadaly2() {

        jegyhegySzikla = 4;
        athaladas = 400*Allandok.SCREEN_HEIGHT/1920;
        arrAkadaly = new ArrayList<>();
        for (int i = 0; i <jegyhegySzikla ; i++) {
            if (i<jegyhegySzikla/2){
                this.arrAkadaly.add(new Akadaly(0,0,200*Allandok.SCREEN_WIDTH/1080,Allandok.SCREEN_HEIGHT/2));
                this.arrAkadaly.get(this.arrAkadaly.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.jeghegy));
                this.arrAkadaly.get(this.arrAkadaly.size()-1).randomY();
                this.arrAkadaly.get(this.arrAkadaly.size()-1).randomX();
            }else {
                this.arrAkadaly.add(new Akadaly(this.arrAkadaly.get(i-jegyhegySzikla/2).getX(),this.arrAkadaly.get(i-jegyhegySzikla/2).getY()
                        + this.arrAkadaly.get(i-jegyhegySzikla/2).getHeight() + this.athaladas,200*Allandok.SCREEN_WIDTH/1080, Allandok.SCREEN_HEIGHT/2));

                this.arrAkadaly.get(this.arrAkadaly.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.szikla));
            }

        }

    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    private void initAkadaly() {

        jegyhegySzikla = 4;
        athaladas = 400*Allandok.SCREEN_HEIGHT/1920;
        arrAkadaly = new ArrayList<>();
        for (int i = 0; i < jegyhegySzikla; i++) {
            if (i<jegyhegySzikla/2){
                this.arrAkadaly.add(new Akadaly(Allandok.SCREEN_WIDTH+i*((Allandok.SCREEN_WIDTH+200*Allandok.SCREEN_WIDTH/1080)/(jegyhegySzikla/2)),
                        0,200*Allandok.SCREEN_WIDTH/1080,Allandok.SCREEN_HEIGHT/2 ));
                this.arrAkadaly.get(this.arrAkadaly.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.sz2));
                this.arrAkadaly.get(this.arrAkadaly.size()-1).randomY();
            }else {
                this.arrAkadaly.add(new Akadaly(this.arrAkadaly.get(i-jegyhegySzikla/2).getX(),this.arrAkadaly.get(i-jegyhegySzikla/2).getY()
                        + this.arrAkadaly.get(i-jegyhegySzikla/2).getHeight() + this.athaladas,200*Allandok.SCREEN_WIDTH/1080, Allandok.SCREEN_HEIGHT/2));

                this.arrAkadaly.get(this.arrAkadaly.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.sz1));
            }
        }

    }

    private void initKarakter() {

        karakter = new KarakterObject();
        karakter.setWidth(100*Allandok.SCREEN_WIDTH/1920);
        karakter.setHeight(100*Allandok.SCREEN_HEIGHT/1080);
        karakter.setX(100*Allandok.SCREEN_WIDTH/1920);
        karakter.setY(Allandok.SCREEN_HEIGHT/2-karakter.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.hal1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.hal2));
        karakter.setArrBms(arrBms);

    }

    public void draw(Canvas canvas){
        super.draw(canvas);

        if (start){

            karakter.draw(canvas);
            for (int i = 0; i <jegyhegySzikla ; i++) {
                if (karakter.getRect().intersect(arrAkadaly.get(i).getRect()) ||karakter.getY()-karakter.getHeight()<0 ||karakter.getY()>Allandok.SCREEN_HEIGHT){

                    Akadaly.speed = 0;
                    MainActivity.txt_scoreOver.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_bestScore.setText("Best:" +bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_gameOver.setVisibility(VISIBLE);
                }
                if (this.karakter.getX()+this.karakter.getWidth()>arrAkadaly.get(i).getX()+arrAkadaly.get(i).getWidth()/2
                        &&this.karakter.getX()+this.karakter.getWidth()<=arrAkadaly.get(i).getX()+arrAkadaly.get(i).getWidth()/2+Akadaly.speed
                        &&i<jegyhegySzikla/2){
                    score++;
                    if (score>bestscore){
                        bestscore = score;
                        SharedPreferences sp = contex.getSharedPreferences("gamessatting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore",bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                if (this.arrAkadaly.get(i).getX() < -arrAkadaly.get(i).getWidth()){
                    this.arrAkadaly.get(i).setX(Allandok.SCREEN_WIDTH);
                    if (i<jegyhegySzikla/2){
                        arrAkadaly.get(i).randomY();
                    }else{
                        arrAkadaly.get(i).setY(this.arrAkadaly.get(i-jegyhegySzikla/2).getY()
                                + this.arrAkadaly.get(i-jegyhegySzikla/2).getHeight() + this.athaladas);
                    }
                }
                this.arrAkadaly.get(i).draw(canvas);
            }
        }else {
            if (karakter.getY()>Allandok.SCREEN_HEIGHT/2){
                karakter.setDrop(-15*Allandok.SCREEN_HEIGHT/1920);
            }
            karakter.draw(canvas);
        }
        handler.postDelayed(r,10);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            karakter.setDrop(-15);
        }
        return true;
    }

    public void reset() {

        MainActivity.txt_score.setText("0");
        score = 0;
        bestscore += bestscore;
        initAkadaly();
       // initAkadaly2();
        initKarakter();
    }

}
