package com.company;

import org.opencv.*;

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
    FeatureDetector surfFeatureDetector = FeatureDetector.create(FeatureDetector.BRISK);

    // Create Surf Extractor
    DescriptorExtractor surfDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.FREAK);

    // Create Matcher
    DescriptorMatcher flannDescriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

    // Keypointsafes
    ArrayList<MatOfKeyPoint> logoKeypoints = new ArrayList<MatOfKeyPoint>();
    ArrayList<MatOfKeyPoint> imageKeypoints = new ArrayList<MatOfKeyPoint>();

    // Descriptorssafes
    Mat logoDescriptors = new Mat();
    Mat descriptors2 = new Mat();

    public Mat convertToGrayScale(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        return image;
    }

    public void learnAboutLogo(Mat logo) {
        // Detect Keypoints
        surfFeatureDetector.detect(logo, logoKeypoints.get(0));

        // Compute Descriptors
        surfDescriptorExtractor.compute(logo, logoKeypoints.get(0), logoDescriptors);
    }


    public void run() {

        logoKeypoints.add(new MatOfKeyPoint());

        imageKeypoints.add(new MatOfKeyPoint());
        // images
        Mat logo = Imgcodecs.imread(getClass().getResource("/resources/cocacola.jpg").getPath());
        Mat image = Imgcodecs.imread(getClass().getResource("/resources/cocacola2.jpg").getPath());

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
        flannDescriptorMatcher.match(logoDescriptors, descriptors2, matches.get(0));

        // filter for "good matches"

        double max_dist = 0;

        double min_dist = 100;

        List<DMatch> matches1 = matches.get(0).toList();

        for (int i = 0; i < logoDescriptors.rows(); i++) {

            double dist = matches1.get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        List<DMatch> good_matches = new ArrayList<DMatch>();

        System.out.println(min_dist);
        System.out.println(max_dist);

        for (int i = 0; i < logoDescriptors.rows(); i++) {
            if (matches1.get(i).distance < Math.max(2 * min_dist, 0.02)) {
                good_matches.add(matches1.get(i));
                System.out.println(matches1.get(i).distance);
            }

        }

        MatOfDMatch goodmatches = new MatOfDMatch();
        goodmatches.fromList(good_matches);
        matches.add(1, goodmatches);


        Mat outImg = new Mat();
        // write images
        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
        Imgcodecs.imwrite("/Volumes/HDD/Jannik/Desktop/logitech.jpg", outImg);
        Features2d.drawKeypoints(image, imageKeypoints.get(0), outImg);
        Imgcodecs.imwrite("/Volumes/HDD/Jannik/Desktop/logitech_logo.jpg", outImg);
        Features2d.drawMatches(logo, logoKeypoints.get(0), image, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        Imgcodecs.imwrite("/Volumes/HDD/Jannik/Desktop/match_logitech.jpg", outImg);


    }
}