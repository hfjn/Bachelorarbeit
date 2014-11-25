package uos.jhoffjann.ObjFinder;
/**
 * Created by jhoffjann on 13.11.14.
 * inspired by OpenGlass Voice Example
 * Github - https://github.com/jaredsburrows/OpenQuartz
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.glass.content.Intents;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import uos.jhoffjann.ObjFinder.Logic.Upload;
import uos.jhoffjann.ObjFinder.View.CameraView;

import java.io.File;


public class MainActivity extends Activity {
    private static final int TAKE_PICTURE_REQUEST = 1;
    private GestureDetector mGestureDetector = null;
    private CameraView cameraView = null;

    private File image = null;

    private String thumbnailPath;

    private final String URL = "http://128.199.32.173:8080/opencvserver-server/analyze";

    private boolean result = false;

    private boolean cameraActive = true;

    MainActivity act = this;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate CameraView
        cameraView = new CameraView(this);

        // Turn on Gestures
        mGestureDetector = createGestureDetector(this);

        // Set the view
        this.setContentView(cameraView);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Do not hold the camera during onResume
        if (cameraView != null) {
            cameraView.releaseCamera();
        }

        // Set the view
        //this.setContentView(cameraView);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Do not hold the camera during onPause
        if (cameraView != null) {
            cameraView.releaseCamera();
        }
    }

    /**
     * Gesture detection for fingers on the Glass
     *
     * @param context
     * @return
     */
    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        //Create a base listener for generic gestures
        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                // Make sure view is initiated
                if(result){
                    cameraView = new CameraView(act);
                    act.setContentView(cameraView);
                    result = false;
                    cameraActive = true;
                }

                else if (cameraView != null && cameraActive) {
                    // Tap with a single finger for photo
                    if (gesture == Gesture.TAP) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent != null) {
                            startActivityForResult(intent, TAKE_PICTURE_REQUEST);
                        }

                        return true;
                    }
                }

                return false;
            }
        });

        return gestureDetector;
    }

    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle photos
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            String picturePath = data.getStringExtra(Intents.EXTRA_PICTURE_FILE_PATH);
            thumbnailPath = data.getStringExtra(Intents.EXTRA_THUMBNAIL_FILE_PATH);
            Log.d("Picture Path: ", picturePath);
            processPictureWhenReady(picturePath);
            updateMainUi("Processing");
            cameraView.releaseCamera();
            cameraActive = false;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Process picture - from example GDK
     *
     * @param picturePath
     */
    private void processPictureWhenReady(final String picturePath) {
        final File pictureFile = new File(picturePath);
        Log.d("Start picture processing", "1");
        if (pictureFile.exists()) {
            Log.d("Analyze.", "Start analyzing");
            image = pictureFile;
            new asyncUploading().execute();
            this.result = true;
        } else {
            Log.d("Start picture Processing", "2");
            final File parentDirectory = pictureFile.getParentFile();
            FileObserver observer = new FileObserver(parentDirectory.getPath(), FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO) {
                // Protect against additional pending events after CLOSE_WRITE is
                // handled.
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path) {
                    if (!isFileWritten) {
                        // For safety, make sure that the file that was created in
                        // the directory is actually the one that we're expecting.
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (affectedFile.equals(pictureFile));

                        if (isFileWritten) {
                            stopWatching();
                            Log.d("Info", "File" + pictureFile.getName() +  " is written");

                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                        else{
                            Log.d("Info", "File is not yet written");
                        }
                    }
                }
            };
            observer.startWatching();
        }
    }

    public void updateMainUi(String result) {
        CardBuilder cardBuilder = new CardBuilder(this, CardBuilder.Layout.CAPTION);
        //cardBuilder.addImage(BitmapFactory.decodeFile(thumbnailPath));
        cardBuilder.setText(result);
        View resultView = cardBuilder.getView();
        cameraView.releaseCamera();
        this.setContentView(resultView);
    }


    private class asyncUploading extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String strResult = null;

        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            strResult = Upload.upload(URL, image);
            return null;
        }

        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            updateMainUi(strResult);
        }
    }

    /**
     * Added but irrelevant
     */
    /*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Stop the preview and release the camera.
            // Execute your logic as quickly as possible
            // so the capture happens quickly.
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
