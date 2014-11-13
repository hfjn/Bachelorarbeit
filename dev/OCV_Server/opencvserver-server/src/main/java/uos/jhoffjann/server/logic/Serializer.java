package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jhoffjann on 13.11.14.
 */
public class Serializer {


    final String root = System.getProperty("user.dir" + File.separator + "objects");

    public void serializeMat(String name, opencv_core.Mat sMat){
        ByteBuffer bMat = sMat.asByteBuffer();
        boolean append = false;
        File file = new File("root" + File.separator + name);
        try{
            FileChannel wChannel = new FileOutputStream(file, append).getChannel();
            wChannel.write(bMat);
            wChannel.close();
        }catch(IOException e){

        }
    }

    public opencv_core.Mat deserializeMat(String name){
        File file = new File("root" + File.separator + name);

        if(!file.exists())
            return null;

        opencv_core.CvMat Mat = new opencv_core.CvMat();
    }
}
