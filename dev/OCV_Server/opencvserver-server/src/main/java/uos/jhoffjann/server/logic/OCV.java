package uos.jhoffjann.server.logic;


import org.bytedeco.javacpp.*;
import uos.jhoffjann.server.common.Result;

import java.io.File;
import java.util.concurrent.Callable;


/**
 * Created by jhoffjann on 04.11.14.
 */
public class OCV implements Callable<Result> {

    // Create Feature Detector
    double hessianThreshold = 2500d;
    int nOctaves = 4;
    int nOctaveLayers = 2;
    boolean extended = true;
    boolean upright = false;
    opencv_nonfree.SURF surfFeatureDetector = new opencv_nonfree.SURF(hessianThreshold, nOctaves, nOctaveLayers,
            extended, upright);

    // Create Surf Extractor
    opencv_features2d.DescriptorExtractor surfDescriptorExtractor = opencv_features2d.DescriptorExtractor.create("SURF");

    // Create Matcher
    opencv_features2d.DescriptorMatcher matcher = new opencv_features2d.FlannBasedMatcher();

    opencv_core.Mat image;
    opencv_core.Mat logo;

    // images
    opencv_core.Mat images[] = {new opencv_core.Mat(), new opencv_core.Mat()};

    String name;

    // Keypointsafes
    opencv_features2d.KeyPoint keypoints[] = new opencv_features2d.KeyPoint[2];

    // Descriptorssafes
    opencv_core.Mat descriptors[] = {new opencv_core.Mat(), new opencv_core.Mat()};


    public OCV(File fLogo, File fImage) {
        images[0] = opencv_highgui.imread(fLogo.getAbsolutePath());
        images[1] = opencv_highgui.imread(fImage.getAbsolutePath());
        name = fLogo.getName();
    }

    /**
     * @param image
     * @return
     */
    public opencv_core.Mat convertToGrayScale(opencv_core.Mat image) {
        opencv_imgproc.cvtColor(image, image, opencv_imgproc.COLOR_BGR2GRAY);
        return image;
    }


    /**
     * @param images
     */
    public void learnAboutImages(opencv_core.Mat[] images) {
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
    public opencv_features2d.DMatchVectorVector getGoodMatches(opencv_features2d.DMatchVectorVector matches) {
        opencv_features2d.DMatchVectorVector goodMatches = new opencv_features2d.DMatchVectorVector();
        int i = 0;
        for (int j = 0; j < matches.size(); j++) {
            System.out.println(matches.get(j, 0).distance() + " + " + matches.get(j , 1).distance());
            if (matches.get(j, 0).distance() < 0.67 * matches.get(j, 1).distance()) {
                i++;
                goodMatches.put(i, 0, matches.get(j, 0));
                goodMatches.put(i, 1, matches.get(j, 1));
            }
        }
        return goodMatches;
    }


    /**
     *
     */
    public Result call() {
        learnAboutImages(images);

        // Matchsafe
        opencv_features2d.DMatchVectorVector matches = new opencv_features2d.DMatchVectorVector();

        // Match it

        matcher.knnMatch(descriptors[0], descriptors[1], matches, 2);

        // filter for "good matches"

        matches = getGoodMatches(matches);

        System.out.println(matches.size());

        return new Result(name, (int) matches.size());
    }
}
