package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.util.UUID;

/**
 * Created by jhoffjann on 13.11.14.
 */
public class Serializer {


    final static String root = System.getProperty("user.dir");

    /**
     * Serializes a Mat to an xml-File and saves it to the hard drive
     * @param name the object's name
     * @param sMat the Mat
     * @return the absolute file path
     */

    public static String serializeMat(String name, opencv_core.Mat sMat) {

        File dir = new File(root + File.separator + "object_xml");

        if(!dir.exists())
            dir.mkdirs();

        String filePath = dir.getAbsolutePath() + File.separator + UUID.randomUUID() + ".xml";

        opencv_core.FileStorage storage = new opencv_core.FileStorage(filePath, opencv_core.FileStorage.WRITE);

        opencv_core.CvMat cvMat = sMat.asCvMat();

        storage.writeObj(name, cvMat);

        storage.release();

        return filePath;
    }

    /**
     * reads a Mat from an xml-file
     * @param name the objects name
     * @return the Mat
     */

    public static opencv_core.Mat deserializeMat(String filePath, String name) {

        opencv_core.FileStorage storage = new opencv_core.FileStorage(filePath, opencv_core.FileStorage.READ);

        opencv_core.CvMat cvMat = new opencv_core.CvMat(storage.get(name).readObj());

        opencv_core.Mat mat = new opencv_core.Mat(cvMat);

        return mat;
    }
}
