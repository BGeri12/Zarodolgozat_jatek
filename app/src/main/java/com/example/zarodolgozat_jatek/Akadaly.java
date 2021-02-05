package com.example.zarodolgozat_jatek;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

import javax.xml.transform.sax.TemplatesHandler;

public class Akadaly extends AlapObject {

    public static int speed;

    public Akadaly(float x, float y, int width, int height) {
        super(x, y, width, height);
        speed = 10*Allandok.SCREEN_WIDTH/1080;
    }

    public void draw(Canvas canvas){

        this.x -= speed;
        canvas.drawBitmap(this.bm, this.x, this.y, null);
    }

    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt((0+this.height/4)+1)-this.height/4;
    }

    public void randomX(){
        Random r = new Random();
        this.x = r.nextInt((0+this.width/4)+1)-this.width/4;
    }

    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm,width,height,true);
    }
}
