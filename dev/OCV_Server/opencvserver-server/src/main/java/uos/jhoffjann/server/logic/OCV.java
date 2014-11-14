package uos.jhoffjann.server.logic;


import org.bytedeco.javacpp.*;
import uos.jhoffjann.server.common.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 * Created by jhoffjann on 04.11.14.
 */
public class OCV implements Callable<Result> {

    // Create Feature Detector
    private final double hessianThreshold = 2500d;
    private final int nOctaves = 4;
    private final int nOctaveLayers = 2;
    private final boolean extended = true;
    private final boolean upright = false;
    private opencv_nonfree.SURF surfFeatureDetector = new opencv_nonfree.SURF(hessianThreshold, nOctaves, nOctaveLayers,
            extended, upright);

    // Create Surf Extractor
    private opencv_features2d.DescriptorExtractor surfDescriptorExtractor = opencv_features2d.DescriptorExtractor.create("SURF");

    // Create Matcher
    private opencv_features2d.FlannBasedMatcher matcher = new opencv_features2d.FlannBasedMatcher();

    private final double RATIO = 0.65;
    // images
    private opencv_core.Mat images[] = {new opencv_core.Mat(), new opencv_core.Mat()};

    private String name;

    // Keypointsafes
    private opencv_features2d.KeyPoint keypoints[] = {new opencv_features2d.KeyPoint(), new opencv_features2d.KeyPoint()};

    // Descriptorssafes
    private opencv_core.Mat descriptors[] = {new opencv_core.Mat(), new opencv_core.Mat()};


    public OCV(File fLogo, File fImage) {
        images[0] = opencv_highgui.imread(fLogo.getAbsolutePath());
        images[1] = opencv_highgui.imread(fImage.getAbsolutePath());
        name = fLogo.getName();
    }

    /**
     * @param image
     * @return
     */
    private opencv_core.Mat convertToGrayScale(opencv_core.Mat image) {
        opencv_imgproc.cvtColor(image, image, opencv_imgproc.COLOR_BGR2GRAY);
        return image;
    }


    /**
     * @param images
     */
    private void learnAboutImages(opencv_core.Mat[] images) {
        for (int i = 0; i < images.length; i++) {
            // Detect Keypoints
            surfFeatureDetector.detect(images[i], keypoints[i]);

            // Compute Descriptors
            surfDescriptorExtractor.compute(images[i], keypoints[i], descriptors[i]);

            
        }

    }

    /**
     * @param matches
     * @return
     */
    private ArrayList<opencv_features2d.DMatch> getGoodMatches(opencv_features2d.DMatchVectorVector matches) {
        ArrayList<opencv_features2d.DMatch> goodMatches = new ArrayList<opencv_features2d.DMatch>();
        for (int j = 0; j < matches.size(); j++) {
            double mRatio = matches.get(j, 0).distance() / matches.get(j, 1).distance();
            // System.out.println(matches.get(j, 0).distance() + " / " + matches.get(j, 1).distance() + " = " + mRatio);

            if (mRatio <= RATIO) {
                goodMatches.add(matches.get(j, 0));
            }
        }
        return goodMatches;

    }

    /**
     *
     */
    public Result call() {
        images[0] = convertToGrayScale(images[0]);
        images[1] = convertToGrayScale(images[1]);

        learnAboutImages(images);
        // Match it
        opencv_features2d.DMatchVectorVector matches = new opencv_features2d.DMatchVectorVector();
        matcher.knnMatch(descriptors[0], descriptors[1], matches, 2);
        // filter for "good matches"

        ArrayList<opencv_features2d.DMatch> goodMatches = getGoodMatches(matches);

        return new Result(name, goodMatches.size());
    }
}
