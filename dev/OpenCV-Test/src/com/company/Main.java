package com.company;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class Main {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }


}


class Demo{
    public void run(){
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/resources/lbpcascade_frontalface.xml").getPath());

        Mat image = Highgui.imread(getClass().getResource("/resources/cocacola.jpg").getPath());

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

    }
}