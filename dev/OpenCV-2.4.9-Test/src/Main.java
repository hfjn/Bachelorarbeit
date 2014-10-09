


import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.*;
import org.opencv.calib3d.*;

import java.util.*;


public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }


}


class Demo {

    // Create Feature Detector
    FeatureDetector surfFeatureDetector = FeatureDetector.create(FeatureDetector.SURF);

    // Create Surf Extractor
    DescriptorExtractor surfDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);

    // Create Matcher
    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);


    // Keypointsafes
    ArrayList<MatOfKeyPoint> logoKeypoints = new ArrayList<MatOfKeyPoint>();
    ArrayList<MatOfKeyPoint> imageKeypoints = new ArrayList<MatOfKeyPoint>();

    // Descriptorssafes
    Mat logoDescriptors = new Mat();
    Mat descriptors2 = new Mat();


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


            // filter for best match
        /*for (int i = 0; i < matchList.size(); i++) {

            double dist = matchList.get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        System.out.println(min_dist);
        System.out.println(max_dist);*/

            // filter matches for usable ones

            if (matchList.get(0).distance < 0.7 * matchList.get(1).distance) {
                good_matches.add(matchList.get(0));

            }


        }
        MatOfDMatch goodmatches = new MatOfDMatch();
        goodmatches.fromList(good_matches);
        if (good_matches.size() < 10) {
            System.out.println("No object found!");
            return new MatOfDMatch();
        }
        return goodmatches;


    }

    public Mat createHomography(MatOfDMatch goodmatches){
        List<Point> srcPoints = new ArrayList<Point>();
        List<Point> dstPoints = new ArrayList<Point>();

        List<KeyPoint> logoKeypointList = logoKeypoints.get(0).toList();
        List<KeyPoint> imgKeypointList = imageKeypoints.get(0).toList();

        List<DMatch> matches = goodmatches.toList();
        for (int i = 0; i < matches.size(); i++){
            srcPoints.add(logoKeypointList.get(matches.get(i).queryIdx).pt);
            dstPoints.add(imgKeypointList.get(matches.get(i).trainIdx).pt);
        }

        MatOfPoint2f matSrcPoints = new MatOfPoint2f();
        MatOfPoint2f matDstPoints = new MatOfPoint2f();

        matSrcPoints.fromList(srcPoints);
        matDstPoints.fromList(dstPoints);

        Mat mask = new Mat();

        Calib3d.findHomography(matSrcPoints, matDstPoints, Calib3d.RANSAC, 5.0, mask);

        return mask;

    }

    /**
     *
     */
    public void run() {

        logoKeypoints.add(new MatOfKeyPoint());

        imageKeypoints.add(new MatOfKeyPoint());
        // images
        Mat logo = Highgui.imread(getClass().getResource("/resources/dhl-logo.jpg").getPath());
        Mat image = Highgui.imread(getClass().getResource("/resources/dhl-car.jpg").getPath());

        // Convert RGB to Grayscale because SURF is only able to work that way
        logo = convertToGrayScale(logo);
        image = convertToGrayScale(image);

        learnAboutLogo(logo);

        // Find Keypoints of image

        surfFeatureDetector.detect(image, imageKeypoints.get(0));

        // Compute Descriptors of image

        surfDescriptorExtractor.compute(image, imageKeypoints.get(0), descriptors2);

        // Matchsafe
        ArrayList<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        matches.add(new MatOfDMatch());

        // Match it
        matcher.knnMatch(logoDescriptors, descriptors2, matches, 2);

        // filter for "good matches"


        matches.add(1, getGoodMatches(matches));

        Mat mask = createHomography(matches.get(1));

        

        Mat outImg = new Mat();
        // write images
        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic1.jpg", outImg);
        Features2d.drawKeypoints(image, imageKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic2.jpg", outImg);
        Features2d.drawMatches(logo, logoKeypoints.get(0), image, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/match.jpg", outImg);


    }
}