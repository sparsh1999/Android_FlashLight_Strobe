package com.example.sparsh.flash_light;

import android.hardware.camera2.CameraManager;

public class Strobe implements Runnable
{
    String CamId;
    CameraManager camera;
    boolean check = false;
    int fr=0;
    @Override
    public void run()
    {
        while (!check)
        {
            try {
                camera.setTorchMode(CamId, true);
                Thread.sleep(500 - fr);
                camera.setTorchMode(CamId, false);
                Thread.sleep(500 - fr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
