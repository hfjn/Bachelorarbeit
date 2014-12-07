package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import uos.jhoffjann.server.model.Result;

import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 *
 * Created by jhoffjann on 04.11.14.
 */
public class OCV_Matcher implements Callable<Result> {

    // Create Nearest-Neighbour-Matcher
    private opencv_features2d.FlannBasedMatcher matcher = new opencv_features2d.FlannBasedMatcher();

    private final double RATIO = 0.65;

    private String name;

    private String path;

    opencv_core.Mat descriptors[] = {new opencv_core.Mat(), new opencv_core.Mat()};

    /**
     * The constructor
     * @param path Path to the origin object
     * @param name Name of the object
     * @param desc1 Descriptors of first image
     * @param desc2 Descriptors of second image
     */
    public OCV_Matcher(String path, String name, opencv_core.Mat desc1, opencv_core.Mat desc2) {
        descriptors[0] = desc1;
        descriptors[1] = desc2;
        this.path = path;
        this.name = name;
    }

    /**
     * Filters the Matches for good matches
     * @param matches the matches which are supposed to be filtered
     * @return ArrayList with Best Matches
     */
    private ArrayList<Double> getGoodMatches(opencv_features2d.DMatchVectorVector matches) {
        ArrayList<Double> goodMatches = new ArrayList<Double>();
        for (int j = 0; j < matches.size(); j++) {
            double mRatio = matches.get(j, 0).distance() / matches.get(j, 1).distance();
            if (mRatio <= RATIO)
                goodMatches.add(mRatio);
        }
        return goodMatches;
    }

    /**
     * Method that starts the Thread and returns the quantity of good Matches
     * @return Future Result
     */
    @Override
    public Result call() {
        // Match it
        opencv_features2d.DMatchVectorVector matches = new opencv_features2d.DMatchVectorVector();
        matcher.knnMatch(descriptors[0], descriptors[1], matches, 2);
        // filter for "good matches"
        ArrayList<Double> goodMatches = getGoodMatches(matches);

        return new Result(name, goodMatches, path);
    }
}
