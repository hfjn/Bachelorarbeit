package uos.jhoffjann.server.logic;


import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
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
    FeatureDetector surfFeatureDetector = FeatureDetector.create(FeatureDetector.SURF);

    // Create Surf Extractor
    DescriptorExtractor surfDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);

    // Create Matcher
    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

    Mat image;
    Mat logo;

    String name;

    // Keypointsafes
    ArrayList<MatOfKeyPoint> logoKeypoints = new ArrayList<MatOfKeyPoint>();
    ArrayList<MatOfKeyPoint> imageKeypoints = new ArrayList<MatOfKeyPoint>();

    // Descriptorssafes
    Mat logoDescriptors = new Mat();
    Mat objectDescriptors = new Mat();


    public OCV(File fLogo, File fImage){
        logo = Highgui.imread(fLogo.getAbsolutePath());
        image = Highgui.imread(fImage.getAbsolutePath());
        name = fLogo.getName();
    }

    /**
     * @param image
     * @return
     */
    public Mat convertToGrayScale(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        return image;
    }


    /**
     * @param logo
     */
    public void learnAboutLogo(Mat logo) {
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
        List<DMatch> good_matches = new ArrayList<DMatch>();
        for (int j = 0; j < matches.size(); j++) {
            List<DMatch> matchList = matches.get(j).toList();
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

        return new Result(name, matches.size());
    }
}
