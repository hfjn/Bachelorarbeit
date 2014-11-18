package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;

/**
 * Created by jhoffjann on 13.11.14.
 */
public class Serializer {


    final static String root = System.getProperty("user.dir");

    /**
     *
     * @param name
     * @param sMat
     * @return
     */

    public static boolean serializeMat(String name, opencv_core.Mat sMat) {

        opencv_core.FileStorage storage = new opencv_core.FileStorage(root + File.separator + "object" + File.separator +
                name + ".xml", opencv_core.FileStorage.WRITE);

        opencv_core.CvMat cvMat = sMat.asCvMat();

        System.out.println(cvMat.rows());

        storage.writeObj(name, cvMat);

        storage.release();

        return true;
    }

    /**
     *
     * @param name
     * @return
     */

    public static opencv_core.Mat deserializeMat(String name) {

        opencv_core.FileStorage storage = new opencv_core.FileStorage(root + File.separator + "object" +
                File.separator + name + ".xml", opencv_core.FileStorage.READ);

        opencv_core.CvMat cvMat = new opencv_core.CvMat(storage.get(name).readObj());

        System.out.println(cvMat.rows());

        opencv_core.Mat mat = new opencv_core.Mat(cvMat);

        System.out.println(mat.rows());

        return mat;
    }
}
