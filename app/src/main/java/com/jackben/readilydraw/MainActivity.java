package com.jackben.readilydraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, OnClickListener {
    private ImageView main_iv;
    private Button main_save, main_clear;
    private int downPointX;
    private int downPointY;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayoutView();
    }

    private void initLayoutView() {
        main_iv = (ImageView) findViewById(R.id.main_imageview);
        main_save = (Button) findViewById(R.id.main_save);
        main_clear = (Button) findViewById(R.id.main_clear);
        main_iv.setOnTouchListener(this);
        main_save.setOnClickListener(this);
        main_clear.setOnClickListener(this);
    }

    /**
     * imageview的触摸事件的监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        event.getAction();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                initView();
                initPaint();

                downPointX = (int) event.getX();
                downPointY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                int movePointX = (int) event.getX();
                int movePointY = (int) event.getY();
                canvas.drawLine(downPointX, downPointY, movePointX, movePointY, paint);//在画布上画线到位图
                main_iv.setImageBitmap(bitmap);//把位图设置为iv
                downPointX = movePointX;
                downPointY = movePointY;
                break;
            case MotionEvent.ACTION_UP://抬起
                break;

        }
        return true;//返回true的话，表示监听器消耗了改事件，即可以对事件进行处理。反之不能进行处理。
    }

    /**
     * button的点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_save:
                String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReadileDrawDir/";//保存的地址（绝对地址）
                File ReadileDrawDir = new File(cachePath);//创建保存的文件夹
                if (!ReadileDrawDir.exists()) {//如果文件夹不存在，则新建文件夹
                    ReadileDrawDir.mkdir();
                }
                File file = new File(cachePath, "ReadlyDraw" + System.currentTimeMillis() + ".jpg");
                try {
                    FileOutputStream ooStream = new FileOutputStream(file);
                    boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ooStream);//将bitmap以特定格式的输出流输出
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "保存出错:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_clear:
                canvas.drawColor(Color.YELLOW);//重新将bitmap画为黄色
                main_iv.setImageBitmap(bitmap);
                break;

        }

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();//初始化画笔
        paint.setColor(Color.BLUE);//设置画笔的颜色为蓝色
        paint.setStrokeWidth(6);//设置画笔的宽度为6
        paint.setAntiAlias(true);
    }

    /**
     * 初始化一个画布（canvas）和一个同密度的可变的位图（bitmap）
     */
    private void initView() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(main_iv.getWidth(), main_iv.getHeight(), Bitmap.Config.ARGB_4444);//位图的长和宽是main_iv的长宽
            canvas = new Canvas(bitmap);
            canvas.drawColor(Color.YELLOW);//填满整个画布的位图用指定的颜色
            main_iv.setImageBitmap(bitmap);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
