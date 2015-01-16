package uos.jhoffjann.server.logic;

import com.google.gson.Gson;
import uos.jhoffjann.server.model.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jannik on 28.11.14.
 */
public class ResultSaver implements Runnable{

    private File dir;
    private Result best;

    /**
     * A constructor
     * @param dir the file's path
     * @param best the result
     */

    public ResultSaver(File dir, Result best){
        this.dir = dir;
        this.best = best;

    }

    /**
     * Saves the stuff
     */
    public void run(){
        // write best Result to json to make it better to understand
        Gson gson = new Gson();
        String json = gson.toJson(best);
        try {
            FileWriter writer = new FileWriter(dir.getAbsolutePath() + File.separator
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "-" + best.getName() + ".json");
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
