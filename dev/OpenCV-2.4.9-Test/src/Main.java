import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Base64;
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

    public static String matToJson(Mat mat){
        JsonObject obj = new JsonObject();

        if(mat.isContinuous()){
            int cols = mat.cols();
            int rows = mat.rows();
            int elemSize = (int) mat.elemSize();

            byte[] data = new byte[cols * rows * elemSize];

            mat.get(0, 0, data);

            obj.addProperty("rows", mat.rows());
            obj.addProperty("cols", mat.cols());
            obj.addProperty("type", mat.type());

            // We cannot set binary data to a json object, so:
            // Encoding data byte array to Base64.
            Base64.Encoder encoder = Base64.getEncoder();
            String dataString = new String(encoder.encode(data));

            obj.addProperty("data", dataString);

            Gson gson = new Gson();
            String json = gson.toJson(obj);

            return json;
        } else {
            System.out.println("Mat not continuous.");
        }
        return "{}";
    }

    public static Mat matFromJson(String json){
        JsonParser parser = new JsonParser();
        JsonObject JsonObject = parser.parse(json).getAsJsonObject();

        int rows = JsonObject.get("rows").getAsInt();
        int cols = JsonObject.get("cols").getAsInt();
        int type = JsonObject.get("type").getAsInt();

        String dataString = JsonObject.get("data").getAsString();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] data = decoder.decode(dataString.getBytes());

        Mat mat = new Mat(rows, cols, type);
        mat.put(0, 0, data);

        return mat;
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
                if (matchList.get(0).distance < 0.59 * matchList.get(1).distance && ((int) matches.get(j).size().) >= 2) {
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
        Mat logo = Highgui.imread(getClass().getResource("/resources/dhl-logo.jpg").getPath());
        Mat image = Highgui.imread(getClass().getResource("/resources/dhl-car.jpg").getPath());


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
        Features2d.drawKeypoints(logo, logoKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic1.jpg", outImg);
        Features2d.drawKeypoints(image, imageKeypoints.get(0), outImg);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/pic2.jpg", outImg);
        Features2d.drawMatches(logo, logoKeypoints.get(0), image, imageKeypoints.get(0), matches.get(1), outImg, new Scalar(0, 255, 0), new Scalar(0, 0, 255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        Highgui.imwrite("/Volumes/HDD/Jannik/Desktop/match.jpg", outImg);


    }
}