package com.example.droneapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

class JoystickController extends SurfaceView implements SurfaceHolder.Callback {
    private final Joystick joystick1;
    private final Player player;
    private UpdateLoop updateLoop;
    private int width;
    private int height;

    public JoystickController(Context context){
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        updateLoop = new UpdateLoop(this, surfaceHolder);
        joystick1 = new Joystick(width/5,(int)(height/1.2),120,50);
        player = new Player(getContext(), width/2, height/2, 30);

        setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (joystick1.isPressed((double) event.getX(), (double) event.getY())){
                    joystick1.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick1.getIsPressed()){
                    joystick1.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick1.setIsPressed(false);
                joystick1.resetActuator();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        updateLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //drawUPS(canvas);
        //drawFPS(canvas);
        drawVelocity(canvas);
        joystick1.draw(canvas);
        player.draw(canvas);
    }

    /*public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(updateLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }*/

    /*public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(updateLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }*/

    public void drawVelocity(Canvas canvas){
        double velX = player.getVelocityX();
        double velY = player.getVelocityY();
        double v = Math.sqrt(Math.pow(velX,2) + Math.pow(velY, 2));
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.neon_green);
        paint.setColor(color);
        paint.setTextSize(40);
        canvas.drawText(String.format("VelocityX: %.2f",velX), width/2-100, 100, paint);
        canvas.drawText(String.format("VelocityY: %.2f",velY), width/2-100, 200, paint);

    }

    public void update() {
        joystick1.update();
        player.update(joystick1);
    }
}
