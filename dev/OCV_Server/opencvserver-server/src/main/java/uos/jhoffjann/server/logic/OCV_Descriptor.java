package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.*;
import org.slf4j.Logger;

import java.io.File;

/**
 * Created by jhoffjann on 14.11.14.
 */
public class OCV_Descriptor {

    private static final double hessianThreshold = 2500d;
    private static final int nOctaves = 4;
    private static final int nOctaveLayers = 2;
    private static final boolean extended = true;
    private static boolean upright = false;
    private static Logger log = org.slf4j.LoggerFactory.getLogger(OCV_Descriptor.class);


    // Create Surf Keypoint Detector
    private static opencv_nonfree.SURF surfFeatureDetector = new opencv_nonfree.SURF(hessianThreshold, nOctaves, nOctaveLayers,
            extended, upright);

    // Create Surf Extractor
    private static opencv_features2d.DescriptorExtractor surfDescriptorExtractor = opencv_features2d.DescriptorExtractor.create("SURF");

    /**
     * Processes Surf Descriptors for the image
     * @param image The image which is supposed to be analyzed
     * @return A matrix with float descriptors that describe the Features of the image
     */
    public static opencv_core.Mat getDescriptor(File image, boolean debug) {

        // Storage for the relevant data
        opencv_features2d.KeyPoint keypoints = new opencv_features2d.KeyPoint();

        opencv_core.Mat descriptors = new opencv_core.Mat();

        // Read the image
        opencv_core.Mat mImage = opencv_highgui.imread(image.getAbsolutePath());

        opencv_imgproc.cvtColor(mImage, mImage, opencv_imgproc.COLOR_BGR2GRAY);

        // Process it
        surfFeatureDetector.detect(mImage, keypoints);

        surfDescriptorExtractor.compute(mImage, keypoints, descriptors);


        if(debug){
            opencv_core.Mat outImage = new opencv_core.Mat();
            opencv_features2d.drawKeypoints(mImage, keypoints, outImage, new opencv_core.Scalar(255,0,0,0), opencv_features2d.DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
            File dir = new File(System.getProperty("user.dir") + File.separator +"debug");
            if(!dir.exists())
                dir.mkdirs();
            opencv_highgui.imwrite(dir + File.separator + image.getName() + ".jpg", outImage);
        }

        return descriptors;
    }
}
