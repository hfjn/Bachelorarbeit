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


    final static String root = System.getProperty("user.dir");

    


    public static boolean serializeMat(String name, opencv_core.Mat sMat) {
        ByteBuffer bMat = sMat.asByteBuffer();
        boolean append = false;

        File file = new File(root + File.separator + "object");
        if (!file.exists())
            file.mkdirs();

        file = new File(file.getAbsolutePath() + File.separator + name);

        try {
            FileChannel wChannel = new FileOutputStream(file, append).getChannel();
            wChannel.write(bMat);
            wChannel.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static opencv_core.Mat deserializeMat(int rows, int cols, int type, String name) {
        File file = new File(root + File.separator + "object" + File.separator + name);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        byte[] bMat;
        try {
            // Read Bytes from file.
            bMat = IOUtils.toByteArray(new FileInputStream(file));
            System.out.println(bMat.length);
//            opencv_core.Mat nMat = new opencv_core.Mat(rows, cols, type);
//            nMat.data().put(bMat);
//            return nMat;
            return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
