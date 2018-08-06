package hmf.com.project.onboarding.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import hmf.com.project.onboarding.Bitmaps;
import hmf.com.project.onboarding.R;
import hmf.com.project.onboarding.logger.L;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnClickListener {
    private ImageView imgPicture1, imgPicture2, imgPicture3, imgCapture, imgDone;
    private SurfaceView mPreview;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private boolean inPreview;
    private CameraManager cameraManager;
    private int count;
    private boolean isCameraConfigured;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        imgCapture = (ImageView) findViewById(R.id.img_capture);
        imgPicture1 = (ImageView) findViewById(R.id.img_1);
        imgPicture2 = (ImageView) findViewById(R.id.img_2);
        imgPicture3 = (ImageView) findViewById(R.id.img_3);
        imgDone = (ImageView) findViewById(R.id.img_done);
        mPreview = (SurfaceView) findViewById(R.id.camera_preview_surface);
        surfaceHolder = mPreview.getHolder();
        imgCapture.setOnClickListener(this);
        imgPicture1.setOnClickListener(this);
        imgPicture2.setOnClickListener(this);
        imgPicture3.setOnClickListener(this);
        imgDone.setOnClickListener(this);

        surfaceHolder.addCallback(this);
        L.d("CameraActivity::onCreate():: surface created");


    }//

    @Override
    protected void onResume() {
        super.onResume();
        L.d("CameraActivity:: starting the camera preview");
        init();
    }

    private void init() {
        try {
            if (camera == null) {
                L.d("CameraActivity::init():: Opening camera");
                camera = Camera.open();
            }//
            if (camera != null)
                inPreview = true;

            L.d("CameraActivity::init():: Camera done");
        } catch (Exception e) {
            e.printStackTrace();
            L.e("CameraActivity::init():: Exception in opening camera " + e.getLocalizedMessage());
        }//catch()...

    }//

    private void initCameraParameters() throws IOException {
        if (camera != null) {
            L.d("CameraActivity::initCameraParameter():: initailizing the camera");
            if (!isCameraConfigured) {
                cameraManager = new CameraManager(camera);
                isCameraConfigured = true;
            }//
            camera.startPreview();
            camera.setPreviewDisplay(surfaceHolder);
            inPreview = true;
            L.d("CameraActivity::initCameraParameter():: pre-viewing");
        }//
    }//

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        L.d("CameraActivity::surfaceCreated()::");
    }//surfaceCreated()...

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            L.d("CameraActivity::surfaceChanged():: initailizing the camera");
            initCameraParameters();
        } catch (IOException e) {
            e.printStackTrace();
            L.e("CameraActivity::surfaceCreated():: IOException " + e.getMessage());
        }//catch()...
    }//surfaceChanged()

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            L.d("CameraActivity::surfaceDestroyed():: stopping preview");
            camera.stopPreview();
            inPreview = false;
        }//if()...
    }//surfaceDestroyed()...

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            L.d("CameraActivity::onDestroy():: stopping preview");
            camera.release();
            camera = null;
        }//if()
    }//onDestroy()...

    @Override
    public void onClick(View v) {
        if (v == imgCapture) {
            if (inPreview) {
                onClickCapture();

            }
        }//
        if (v == imgDone) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }//
        if (v == imgPicture1) {
            try {
                if (!inPreview) {
                    initCameraParameters();

                }
                count = 0;
            }//
            catch (Exception e) {
                e.printStackTrace();
                L.e("onClick():: while starting preiew");
            }
        }//
        if (v == imgPicture2) {
            try {
                if (!inPreview) {
                    initCameraParameters();

                }
                count = 1;
            }//
            catch (Exception e) {
                e.printStackTrace();
                L.e("onClick():: while starting preiew");
            }
        }//
        if (v == imgPicture3) {
            try {
                if (!inPreview) {
                    initCameraParameters();

                }
                count = 2;
            }//
            catch (Exception e) {
                e.printStackTrace();
                L.e("onClick():: while starting preiew");
            }
        }//
    }//

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmaps.bitmap = Bitmaps.decodeSampledBitmapFromBytes(data, 150, 150);
            imgPicture1.setImageBitmap(Bitmaps.bitmap);
        }//
    };

    private void onClickCapture() {
        L.d("onClickCapture()::Clicking camera");

        if (camera != null) {
            L.d("onClickCapture()::Calling picture Callback");
            Camera.Parameters parameters = camera.getParameters();
            //parameters.setPictureSize(picturewidth, picturewidth);
            camera.setParameters(parameters);
            L.d("Capturing Image of size: h:" +
                    parameters.getPictureSize().height + " w:" +
                    parameters.getPictureSize().width);
            camera.takePicture(null, null, null, pictureCB);
            inPreview = false;
        }//if()...
    } //if()..

    Camera.PictureCallback pictureCB = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera cam) {
            new SavePhotoTask().execute(data);
            inPreview = false;
        }//onPictureTaken()...
    };//PictureCallback()

    class SavePhotoTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... jpeg) {

            try {
                Bitmaps.bitmap =
                        (Bitmaps.decodeSampledBitmapFromBytes(jpeg[0], 480, 640));


                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmaps.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                String strImage = Base64.encodeToString(bos.toByteArray(), Base64.NO_PADDING);
                bos.flush();
                bos.close();
                return strImage;
            }//try
            catch (Exception e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }//catch()...


            return (null);
        }//doInBackground()...

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (count == 0) {
                imgPicture1.setImageBitmap(Bitmaps.bitmap);
                imgPicture2.setVisibility(View.VISIBLE);
                if (null != result && !result.equals("")) {
                    ImageString.photoStringList.add(0, result);
                }//if(null)
            }//
            if (count == 1) {
                imgPicture2.setImageBitmap(Bitmaps.bitmap);
                imgPicture3.setVisibility(View.VISIBLE);
                if (null != result && !result.equals("")) {
                    ImageString.photoStringList.add(1, result);
                }//if(null)
            }//
            if (count == 2) {
                imgPicture3.setImageBitmap(Bitmaps.bitmap);
                if (null != result && !result.equals("")) {
                    ImageString.photoStringList.add(2, result);
                }//if(null)
            }//


            super.onPostExecute(result);
        }
    }//AsyncTask


}
