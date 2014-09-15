/**
 * Created by Jannik on 15.09.14.
 */

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class SimpleSample {

    public static void main(String[] args) {
        IplImage image = cvLoadImage("CocaCola.jpg");
        if (image != null){
            cvSmooth(image, image);
            cvSaveImage("smoothCola.jpg", image);
            cvReleaseImage(image);
        }

    }
}
