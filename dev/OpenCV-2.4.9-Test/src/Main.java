import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    /**
     *
     * @param args
     */
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
        System.out.println(logoDescriptors.type());
//
       // String json = matToJson(logoDescriptors);
//
        //logoDescriptors = matFromJson(json);
    }

/*
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
                if (matchList.get(0).distance < 100000 * matchList.get(1).distance) {
                    good_matches.add(matchList.get(0));
                }



            }
            MatOfDMatch goodmatches = new MatOfDMatch();
            goodmatches.fromList(good_matches);
            if (good_matches.size() != 0x0) {
                if(!((matches.size() / good_matches.size()) >= 0.2)){
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
    public void run() {

        logoKeypoints.add(new MatOfKeyPoint());

        imageKeypoints.add(new MatOfKeyPoint());
        // images
        Mat logo = Highgui.imread(getClass().getResource("/resources/dhl-logo_bg.jpg").getPath(), Imgproc.COLOR_BGR2GRAY);
        Mat image = Highgui.imread(getClass().getResource("/resources/dhl-car_bg.jpg").getPath(), Imgproc.COLOR_BGR2GRAY);


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



        Mat outImg = new Mat();
        // write images
        System.out.println(logoDescriptors.type());
        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic1.jpg", outImg);
        Features2d.drawKeypoints(image, imageKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic2.jpg", outImg);
        Features2d.drawMatches(logo, logoKeypoints.get(0), image, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/match.jpg", outImg);


    }
}