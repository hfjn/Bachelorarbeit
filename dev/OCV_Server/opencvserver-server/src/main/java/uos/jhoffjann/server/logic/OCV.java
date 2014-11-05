package uos.jhoffjann.server.logic;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import uos.jhoffjann.server.common.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by jhoffjann on 04.11.14.
 */
public class OCV implements Callable<Result> {


    // Create Feature Detector
    opencv_features2d.FeatureDetector surfFeatureDetector = opencv_features2d.FeatureDetector.create(opencv_features2d.FeatureDetector.SURF);

    // Create Surf Extractor
    opencv_features2d.DescriptorExtractor surfDescriptorExtractor = opencv_features2d.DescriptorExtractor.create(opencv_features2d.DescriptorExtractor.SURF);

    // Create Matcher
    opencv_features2d.DescriptorMatcher matcher = opencv_features2d.DescriptorMatcher.create(opencv_features2d.DescriptorMatcher.FLANNBASED);

    opencv_core.Mat image;
    opencv_core.Mat logo;

    String name;

    // Keypointsafes
    opencv_features2d.KeyPoint logoKeypoints = new ArrayList<MatOfKeyPoint>();
    opencv_features2d.KeyPoint imageKeypoints = new ArrayList<MatOfKeyPoint>();

    // Descriptorssafes
    opencv_core.Mat logoDescriptors = new opencv_core.Mat();
    opencv_core.Mat objectDescriptors = new opencv_core.Mat();


    public OCV(File fLogo, File fImage){
        logo = Highgui.imread(fLogo.getAbsolutePath());
        image = Highgui.imread(fImage.getAbsolutePath());
        name = fLogo.getName();
    }

    /**
     * @param image
     * @return
     */
    public opencv_core.Mat convertToGrayScale(opencv_core.Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        return image;
    }


    /**
     * @param logo
     */
    public void learnAboutLogo(opencv_core.Mat logo) {
        // Detect Keypoints
        surfFeatureDetector.detect(logo, logoKeypoints.get(0));

        // Compute Descriptors
        surfDescriptorExtractor.compute(logo, logoKeypoints.get(0), logoDescriptors);
        System.out.println(logoDescriptors.type());
    }

    /**
     * @param matches
     * @return
     */
    public MatOfDMatch getGoodMatches(ArrayList<MatOfDMatch> matches) {
        List<opencv_features2d.DMatch> good_matches = new ArrayList<opencv_features2d.DMatch>();
        for (int j = 0; j < matches.size(); j++) {
            List<opencv_features2d.DMatch> matchList = matches.get(j).toList();
            System.out.println(matchList.get(0).distance + " + " + matchList.get(1).distance);
            if (matchList.get(0).distance < 0.67 * matchList.get(1).distance) {
                good_matches.add(matchList.get(0));
            }
        }
        MatOfDMatch goodmatches = new MatOfDMatch();
        goodmatches.fromList(good_matches);
        return goodmatches;
    }


    /**
     *
     */
    public Result call() {

        logoKeypoints.add(new MatOfKeyPoint());

        imageKeypoints.add(new MatOfKeyPoint());
        // images


        learnAboutLogo(logo);

        // Find Keypoints of image

        surfFeatureDetector.detect(image, imageKeypoints.get(0));

        // Compute Descriptors of image

        surfDescriptorExtractor.compute(image, imageKeypoints.get(0), objectDescriptors);

        // Matchsafe
        ArrayList<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        matches.add(new MatOfDMatch());

        // Match it
        matcher.knnMatch(logoDescriptors, objectDescriptors, matches, 2);

        // filter for "good matches"

        matches.add(1, getGoodMatches(matches));

        System.out.println(matches.size());

        return new Result(name, matches.size());
    }
}
