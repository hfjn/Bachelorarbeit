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
     *
     * @param name
     * @param sMat
     * @return
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
     *
     * @param name
     * @return
     */

    public static opencv_core.Mat deserializeMat(String filePath) {

        opencv_core.FileStorage storage = new opencv_core.FileStorage(filePath, opencv_core.FileStorage.READ);

        opencv_core.CvMat cvMat = new opencv_core.CvMat(storage.get(name).readObj());

        opencv_core.Mat mat = new opencv_core.Mat(cvMat);

        return mat;
    }
}
