package uos.jhoffjann.server.logic;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jannik on 19.11.14.
 */
public class Upload {

    // Uploads a MultipartFile to a specified path

    /**
     * Handles the Upload of MultipartFiles
     * @param path the path where Multipartfile is supposed to be saved
     * @param name the name
     * @param file the actual file
     * @return a File Object
     */

    public static File uploadFile(String path, String name, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            File tmpDir = new File(path);
            if (!tmpDir.exists())
                tmpDir.mkdirs();
            // Create file on server
            File serverFile = new File(tmpDir.getAbsolutePath() + File.separator + name
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg");
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
            return serverFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
