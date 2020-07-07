package com.ljy.memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DrawingView extends View implements View.OnTouchListener {
    private ArrayList<PaintPoint> points = new ArrayList<PaintPoint>();
    private int color = Color.BLACK; // 선 색
    private int lineWith = 3; // 선 두께
    private int number = 1;

    // View 를 Xml 에서 사용하기 위해선 3가지 생성자를 모두 정의 해주어야함
    // 정의 x -> Runtime Error
    public DrawingView(Context context) {
        super(context);
        this.setOnTouchListener(this);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Paint p = new Paint();
                p.setColor(color);
                p.setStrokeWidth(lineWith);
                p.setAntiAlias(true);
                points.add(new PaintPoint(motionEvent.getX(), motionEvent.getY(), true, p));
                // 화면을 갱신함 -> onDraw()를 호출
                invalidate();
                break;
            // 아래 작업을 안하게 되면 선이 어색하게 그려지는 현상 발생
            // ex) 점을 찍고 이동한 뒤에 점을 찍는 경우
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_DOWN:
                points.add(new PaintPoint(motionEvent.getX(), motionEvent.getY(), false, null));
                break;
        }
        // System.out.println("DrawingView.onTouch - " + points);
        // return false 로 하게되면 이벤트가 한번 발생하고 종료 -> 점을 그림
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i < points.size(); i++) {
            if (!points.get(i).isDraw()) continue;
            // 선을 그려줌
            // canvas.drawLine( 이전 좌표, 현재 좌표, 선 속성 );
            canvas.drawLine(points.get(i - 1).getX(), points.get(i - 1).getY(), points.get(i).getX(), points.get(i).getY(), points.get(i).getPaint());
        }
    }

    // Reset Function
    public void reset() {
        points.clear(); // PaintPoint ArrayList Clear
        invalidate(); // 화면을 갱신함 -> onDraw()를 호출
    }

    // Save Function
    public String save(Context context) {
        this.setDrawingCacheEnabled(true);
        Bitmap screenshot = this.getDrawingCache();

        String filename = number + "_drawImage.png";
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        try {
            while (file.exists()) {
                filename = (number++) + "_drawImage.png";
                file = new File(Environment.getExternalStorageDirectory(), filename);
            }
            if (file.createNewFile())
                Log.d("save", "파일 생성 성공 / filename : " + filename);
            OutputStream outStream = new FileOutputStream(file);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            // 갤러리에 변경을 알려줌
            // 안드로이드 버전이 Kitkat 이상 일때
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

            Toast.makeText(context.getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setDrawingCacheEnabled(false);
        return file.getPath();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLineWith() {
        return lineWith;
    }

    public void setLineWith(int lineWith) {
        this.lineWith = lineWith;
    }
}
