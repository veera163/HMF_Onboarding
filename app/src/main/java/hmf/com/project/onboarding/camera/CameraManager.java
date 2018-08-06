package hmf.com.project.onboarding.camera;

import android.hardware.Camera;

import hmf.com.project.onboarding.logger.L;

import java.util.List;

/**
 * Created by saurabh.singh on 6/3/2016.
 */
public class CameraManager {


    private Camera camera;

    public CameraManager(Camera camera) {
        this.camera = camera;
        if(this.camera!= null){
            setBestPictureSize();
            setBestPreviewSizes();
            setSceneMode();
            setWhiteBalanceMode();
        }//
    }//

    private void setBestPreviewSizes() {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        if (sizes != null && sizes.size() > 0) {
            Camera.Size size = sizes.get(0);
            parameters.setPreviewSize(size.width, size.height);

        }//
        camera.setParameters(parameters);

    }//
    private void setBestPictureSize(){
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        if (sizes != null && sizes.size() > 0) {
            Camera.Size size = sizes.get(0);
            parameters.setPictureSize(size.width, size.height);

        }//
        camera.setParameters(parameters);
    }//setBestPictureSize()..
    private void setSceneMode(){
        Camera.Parameters parameters =  camera.getParameters();
        List<String> sceneModes = parameters.getSupportedSceneModes();
        if(sceneModes.contains(Camera.Parameters.SCENE_MODE_AUTO)){
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            L.d("CameraManager()::setSceneMode():: SceneMode is " + Camera.Parameters.SCENE_MODE_AUTO);

        }//
        camera.setParameters(parameters);
    }//
    private void setWhiteBalanceMode(){
        Camera.Parameters parameters =  camera.getParameters();
        List<String> sceneModes = parameters.getSupportedWhiteBalance();
        if(sceneModes.contains(Camera.Parameters.WHITE_BALANCE_AUTO)){
            parameters.setSceneMode(Camera.Parameters.WHITE_BALANCE_AUTO);
            L.d("CameraManager()::setSceneMode():: White balance Mode is " + Camera.Parameters.WHITE_BALANCE_AUTO);

        }//
        camera.setParameters(parameters);
    }//
}
