package com.company;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;

public class Main {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }


}


class Demo{
    public void run(){
        Mat image = Highgui.imread(getClass().getResource("/resources/cocacola.jpg").getPath());

        // Convert RGB to Grayscale because SURF is only able to work that way
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

        // Create Surf Extractor
        DescriptorExtractor surf = DescriptorExtractor.create(2);

        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        Mat descriptors = new Mat();

        surf.compute(image, keypoints, descriptors);

        for(int i = 0; i < descriptors.depth(); i++){
            System.out.println(descriptors.get(i, 0).toString());
        }

    }
}