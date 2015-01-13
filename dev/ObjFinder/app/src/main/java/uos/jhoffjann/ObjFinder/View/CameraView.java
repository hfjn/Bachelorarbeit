package uos.jhoffjann.ObjFinder.View;

/**
 * Created by jhoffjann on 13.11.14.
 * inspired by OpenGlass Voice Example
 * Github - https://github.com/jaredsburrows/OpenQuartz
 */

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Manages the Camera Surface
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder = null;
    private Camera camera = null;

    /**
     * Camera View Holder
     * @param context the App Context
     */
    public CameraView(Context context) {
        super(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */

    /**
     * initializes the Surface
     * @param holder holds the surface
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();

        //Set the Hotfix for Google Glass
        this.setCameraParameters(camera);

        //Show the Camera display
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            this.releaseCamera();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */

    /**
     * reinitializes the surface
     * @param holder new Holder
     * @param format new Format
     * @param width new Width
     * @param height new Height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Start the preview for surfaceChanged
        if (camera != null) {
            camera.startPreview();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */

    /**
     * Releases the Surface
     * @param holder the holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Do not hold the camera during surfaceDestroyed - view should be gone
        this.releaseCamera();
    }

    /**
     * Important HotFix for Google Glass (post-XE11) update
     *
     * @param camera Object
     */
    public void setCameraParameters(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewFpsRange(30000, 30000);
            camera.setParameters(parameters);
        }
    }

    /**
     * Release the camera from use
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
