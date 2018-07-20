package com.abhatem.balancingplate;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import org.opencv.core.Mat;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Scalar;
import org.opencv.core.Core;


import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;




import me.aflak.bluetooth.Bluetooth;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener{
    private int threshold = 0;
    private boolean showInput = false;
    BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    public static int ballx = 0, bally =0;
    public static boolean ballDetected = false;




    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {

                }
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setMaxFrameSize(400, 400);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        final Bluetooth bluetooth = new Bluetooth(this);
        bluetooth.enableBluetooth();

        bluetooth.setDiscoveryCallback(new Bluetooth.DiscoveryCallback() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onDevice(BluetoothDevice device) {

            }

            @Override
            public void onPair(BluetoothDevice device) {

            }

            @Override
            public void onUnpair(BluetoothDevice device) {

            }

            @Override
            public void onError(String message) {

            }
        });

        bluetooth.scanDevices();
        bluetooth.connectToName("HC-06");

        bluetooth.setCommunicationCallback(new Bluetooth.CommunicationCallback() {
            @Override
            public void onConnect(BluetoothDevice device) {


                if(bluetooth.isConnected()) {

                } else {

                }
                bluetooth.send("30 50\n");
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {
            }

            @Override
            public void onMessage(String message) {

                if(message.equals("g")) {
                    bluetooth.send(String.valueOf(MainActivity.ballx) + " ");
                    bluetooth.send(String.valueOf(MainActivity.bally) + "\n");
                }
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {

            }
        });

    }

    public void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void showHelp() {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause()
    {
        super.onPause();
        disableCamera();
    }

    public void disableCamera() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        SharedPreferences sp = this.getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        this.threshold = sp.getInt("Threshold", 50);
        this.showInput = sp.getBoolean("showInputFrame", false);

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        List<Mat> lRgb = new ArrayList<Mat>(3);
        Core.split(inputFrame, lRgb);
        Mat mR = lRgb.get(0);
        Mat mG = lRgb.get(1);
        Mat mB = lRgb.get(2);
        Core.divide(mR, new Scalar(1.588), mR);
        Core.divide(mB, new Scalar(2.7), mB);

        Core.subtract(mG, mR, mG);
        Core.subtract(mG, mB, mG);
        Mat input = mG;
        mR.release();

        mB.release();

        Imgproc.blur(input, input, new Size(21, 21));

        Imgproc.threshold(input, input, this.threshold, 250, Imgproc.THRESH_BINARY);

        Imgproc.Canny(input, input, 100, 255);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(input, contours , new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int cx = 0, cy = 0;
        if(contours.size() > 0) {
            this.ballDetected = true;
            Rect box = Imgproc.boundingRect(contours.get(0));
            cx = box.x + (box.width/2);
            cy = box.y + (box.height/2);
            this.ballx = cx;
            this.bally = cy;
        } else {
            this.ballDetected = false;
        }
        if(this.showInput)
            Imgproc.circle(inputFrame, new Point(cx, cy), 10, new Scalar(255, 255, 255), 3);
        else
            Imgproc.circle(input, new Point(cx, cy), 10, new Scalar(255), 3);

        System.gc();
        if(this.showInput)
            return inputFrame;

        return input;
    }



}
