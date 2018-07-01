package com.example.sparsh.flash_light;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Parameter;

import static java.lang.Thread.*;

public class MainActivity extends AppCompatActivity {

    boolean first=false;
    boolean resu=false;
    Strobe str=new Strobe();
    static int a=0;
    CameraManager camera;
    ImageButton b1 ;
    Thread t;
    String CamId;
    EditText e1;
    SeekBar s1;
    Parameter parameter;
    boolean isdevicehasflash=false , isflashon=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.bt1);
        e1 = findViewById(R.id.et1);
        s1 = findViewById(R.id.sb1);
        String s = e1.getText().toString();

        isdevicehasflash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                a = i / 10;
                e1.setText(String.valueOf(a));
                if (isflashon&&a!=0)
                {
                    str.check=true;
                    t = null;
                    try{
                        camera.setTorchMode(CamId, false);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        str.check=false;
                        str.fr = a * 50;
                        str.camera = camera;
                        str.CamId = CamId;
                        t = new Thread(str);
                        t.start();

                    }
                    catch (Exception e)
                    {
                    }
                }
                else
                {
                    try
                    {
                        t=null;
                        str.check=true;
                        camera.setTorchMode(CamId,false);
                        if(isflashon){
                        camera.setTorchMode(CamId, true);}
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(!isdevicehasflash)
        {
            Toast.makeText(MainActivity.this,"Flash Not Available",Toast.LENGTH_SHORT).show();
        }
        else
        {
            camera = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            try
            {
                CamId = camera.getCameraIdList()[0];

            }
            catch(CameraAccessException e)
            {
                e.printStackTrace();
            }
        }

        b1.setOnClickListener(new View.OnClickListener()   {

            @Override
            public void onClick(View view)
            {
                if(!isflashon)
                {
                    if (a == 0)
                    {
                        b1.setBackgroundResource(R.drawable.poweron);
                        isflashon = true;
                        try
                        {
                            camera.setTorchMode(CamId, true);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        flashOn();
                        isflashon = true;
                    }
                }
                else
                {
                    flashOff();
                    isflashon = false;
                }
            }
        });

    }

    public void flashOn()
    {
        if(!isflashon)
        {
            b1.setBackgroundResource(R.drawable.poweron);
            try {
                camera.setTorchMode(CamId, true);
                str.check=false;
                str.fr = a * 50;
                str.camera = camera;
                str.CamId = CamId;
                t = new Thread(str);
                t.start();

            }
            catch (Exception e)
            {
            }
        }
    }
    public void flashOff()
    {
        str.check=true;
        t = null;
        b1.setBackgroundResource(R.drawable.poweroff);

        try{
            camera.setTorchMode(CamId, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.camera != null){
            this.camera = null;
        }
    }


    @Override
    protected void onPause() {
        flashOff();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        if(isflashon){
        flashOn();}
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        camera = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
    }

}

