


import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.*;

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
    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);


    // Keypointsafes
    ArrayList<MatOfKeyPoint> logoKeypoints = new ArrayList<MatOfKeyPoint>();
    ArrayList<MatOfKeyPoint> imageKeypoints = new ArrayList<MatOfKeyPoint>();

    // Descriptorssafes
    Mat logoDescriptors = new Mat();
    Mat descriptors2 = new Mat();


    /**
     *
     * @param image
     * @return
     */
    public Mat convertToGrayScale(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        return image;
    }


    /**
     *
     * @param logo
     */
    public void learnAboutLogo(Mat logo) {
        // Detect Keypoints
        surfFeatureDetector.detect(logo, logoKeypoints.get(0));

        // Compute Descriptors
        surfDescriptorExtractor.compute(logo, logoKeypoints.get(0), logoDescriptors);
    }


    /**
     *
     * @param matches
     * @return
     */
    public MatOfDMatch getGoodMatches(ArrayList<MatOfDMatch> matches){
        double max_dist = 0;

        double min_dist = 1000;

        List<DMatch> matchList = matches.get(0).toList();


        // filter for best match
        for (int i = 0; i < logoDescriptors.rows(); i++) {

            double dist = matchList.get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        System.out.println(min_dist);
        System.out.println(max_dist);

        // filter matches for usable ones

        List<DMatch> good_matches = new ArrayList<DMatch>();

        for(int i = 0; i < matchList.size(); i ++){
            if(matchList.get(i).distance < 1.2 * min_dist){
                good_matches.add(matchList.get(i));
            }
        }


        MatOfDMatch goodmatches = new MatOfDMatch();
        goodmatches.fromList(good_matches);

        return goodmatches;
    }

    


    /**
     *
     */
    public void run() {

        logoKeypoints.add(new MatOfKeyPoint());

        imageKeypoints.add(new MatOfKeyPoint());
        // images
        Mat logo = Highgui.imread(getClass().getResource("/resources/fuenf-euro.jpg").getPath());
        Mat image = Highgui.imread(getClass().getResource("/resources/cocacola2.jpg").getPath());

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
        matcher.match(logoDescriptors, descriptors2, matches.get(0));

        // filter for "good matches"


        matches.add(1, getGoodMatches(matches));


        Mat outImg = new Mat();
        // write images
        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/fuenfer.jpg", outImg);
        Features2d.drawKeypoints(image, imageKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/fuenfer2.jpg", outImg);
        Features2d.drawMatches(logo, logoKeypoints.get(0), image, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/match_fuenfer.jpg", outImg);


    }
}