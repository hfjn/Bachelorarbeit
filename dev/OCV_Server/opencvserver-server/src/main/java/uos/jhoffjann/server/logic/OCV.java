package uos.jhoffjann.server.logic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhoffjann on 04.11.14.
 */
public class OCV implements Runnable {

    private FeatureDetector surfFeatureDetector;
    private DescriptorExtractor surfDescriptorExtractor;
    private DescriptorMatcher matcher;


    /**
     *
     */
    public OCV(File image) {
        // Create Feature Detector
        FeatureDetector surfFeatureDetector = FeatureDetector.create(FeatureDetector.SURF);

        // Create Surf Extractor
        DescriptorExtractor surfDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);

        // Create Matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
    }


    /**
     * @param image
     * @return
     */
    public Mat imageToMat(File image) {
        return Highgui.imread(image.getAbsolutePath());
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
    public String learnAboutLogos(File file) {
        Mat logo = imageToMat(file);


        FileStorage
        // Detect Keypoints
        ArrayList<MatOfKeyPoint> logoKeypoints = new ArrayList<MatOfKeyPoint>();
        logoKeypoints.add(new MatOfKeyPoint());
        surfFeatureDetector.detect(logo, logoKeypoints.get(0));

        // Compute Descriptors
        Mat logoDescriptors = new Mat();
        surfDescriptorExtractor.compute(logo, logoKeypoints.get(0), logoDescriptors);
        System.out.println(logoDescriptors.type());

        return matToJson(logoDescriptors);
    }

    /**
     * @param matches
     * @return
     */
    public MatOfDMatch getGoodMatches(ArrayList<MatOfDMatch> matches) {
        // double max_dist = 0;

        // double min_dist = 1000;

        List<DMatch> good_matches = new ArrayList<DMatch>();
        for (int j = 0; j < matches.size(); j++) {
            List<DMatch> matchList = matches.get(j).toList();
            System.out.println(matchList.get(0).distance + " + " + matchList.get(1).distance);
            if (matchList.get(0).distance < 0.59 * matchList.get(1).distance && ((int) matches.get(j).size().) >= 2) {
                good_matches.add(matchList.get(0));
            }


        }
        MatOfDMatch goodmatches = new MatOfDMatch();
        goodmatches.fromList(good_matches);
        if (good_matches.size() != 0x0) {
            if (!((matches.size() / good_matches.size()) >= 0.2)) {
                System.out.println("No object found!");
                return new MatOfDMatch();
            }
        }
        System.out.println(goodmatches.size());
        return goodmatches;


    }


    /**
     *
     */
    @Override
    public void run() {

        // Keypointsafes
        ArrayList<MatOfKeyPoint> imageKeypoints = new ArrayList<MatOfKeyPoint>();
        imageKeypoints.add(new MatOfKeyPoint());

        // Descriptorssafes
        Mat imageDescriptors = new Mat();

        // image
        Mat mImage = imageToMat(image);

        // Find Keypoints of image

        surfFeatureDetector.detect(mImage, imageKeypoints.get(0));

        // Compute Descriptors of image

        surfDescriptorExtractor.compute(mImage, imageKeypoints.get(0), imageDescriptors);

        // Matchsafe
        ArrayList<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        matches.add(new MatOfDMatch());

        // Match it
        matcher.knnMatch(logoDescriptors, imageDescriptors, matches, 2);

        // filter for "good matches"


        matches.add(1, getGoodMatches(matches));


//        Mat outImg = new Mat();
//        // write images
//        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
//        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic1.jpg", outImg);
//        Features2d.drawKeypoints(mImage, imageKeypoints.get(0), outImg);
//        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic2.jpg", outImg);
//        Features2d.drawMatches(logo, logoKeypoints.get(0), mImage, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
//        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/match.jpg", outImg);


    }
}
