package uos.jhoffjann.server.logic;

import org.apache.commons.io.IOUtils;
import org.bytedeco.javacpp.opencv_core;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jhoffjann on 13.11.14.
 */
public class Serializer {


    final String root = System.getProperty("user.dir" + File.separator + "objects");

    public static boolean serializeMat(String name, opencv_core.Mat sMat) {
        ByteBuffer bMat = sMat.asByteBuffer();
        boolean append = false;
        File file = new File(root + File.separator + name);
        try {
            FileChannel wChannel = new FileOutputStream(file, append).getChannel();
            wChannel.write(bMat);
            wChannel.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static opencv_core.Mat deserializeMat(String name) {
        File file = new File(root + File.separator + name);

        if (!file.exists())
            return null;

        BufferedReader br = null;
        String sCurrentLine = null;
        byte[] bMat = null;
        try {
            bMat = IOUtils.toByteArray(new FileInputStream(file));
        } catch (Exception e) {
            return null;
        }


        opencv_core.Mat nMat = new opencv_core.Mat();

        nMat.data().put(bMat);

        return nMat;
    }
}
