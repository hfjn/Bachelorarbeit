package uos.jhoffjann.server.controller;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.opencv_core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import uos.jhoffjann.server.common.AnalyzeResponse;
import uos.jhoffjann.server.common.Result;
import uos.jhoffjann.server.logic.OCV_Descriptor;
import uos.jhoffjann.server.logic.OCV_Matcher;
import uos.jhoffjann.server.logic.Serializer;
import uos.jhoffjann.server.logic.Upload;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * SpringMVC Controller that lives on the server and handles incoming HTTP requests.
 */
@Controller
public class OCVController {

    private static final Logger log = LoggerFactory.getLogger(OCVController.class);
    private final String root = System.getProperty("user.dir");


    //TODO add descriptors to a xml-File and read hem back from there

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "xml" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    /**
     * @return
     */
    @RequestMapping(value = "/analyze", method = RequestMethod.GET)
    public
    @ResponseBody
    AnalyzeResponse analyzeRequest() {
        return new AnalyzeResponse("Hi! If you want a picture analyzed just post it to this url", new Date());
    }

    /**
     * @param name
     * @param image
     * @return
     */
    @RequestMapping(value = "/analyze", method = RequestMethod.POST)
    public
    @ResponseBody
    AnalyzeResponse analyzeRequest(@RequestParam("name") String name, @RequestParam("file") MultipartFile image) {
        log.info("Request for Analyzing");
        try {
            if (!image.isEmpty()) {


                // Create a temporary directory to store the image
                // TODO check for images. Convert all images to .jpg

                File serverFile = Upload.uploadFile(root + File.separator + "tmpFiles", name, image);

                if (serverFile == null)
                    throw new FileUploadException("File upload was not successful!");

                log.info(new Date() + " - File was successfully uploaded!");

                opencv_core.Mat descriptors = OCV_Descriptor.getDescriptor(serverFile);

                ExecutorService pool = Executors.newFixedThreadPool(10);

                Set<Future<Result>> set = new HashSet<Future<Result>>();

                File dir = new File(root + File.separator + "object");

                // start a thread for each image
                if (dir.isDirectory()) { // make sure it's a directory
                    for (final File f : dir.listFiles(IMAGE_FILTER)) {
                        log.info(new Date() + " - Starting Analyzing");
                        String fileName = FilenameUtils.removeExtension(f.getName());
                        Callable<Result> callable = new OCV_Matcher(fileName, Serializer.deserializeMat(fileName), descriptors);
                        Future<Result> future = pool.submit(callable);
                        set.add(future);
                    }
                }
                // check Results
                Result best = null;
                for (Future<Result> future : set) {
                    if (best == null)
                        best = future.get();
                    else if (best.getMatches().size() < future.get().getMatches().size()) {
                        best = future.get();
                    }
                }
                if (best != null && best.getMatches().size() > 4) {
                    log.info(new Date() + " - Quantity of good matches: " + best.getMatches() + "");
                    // write best Result to json to make it better to understand
                    Gson gson = new Gson();
                    String json = gson.toJson(best);
                    try {
                        FileWriter writer = new FileWriter(root + File.separator + "results" + File.separator
                                + new Date() + "-" + best.getName() + ".json");
                        writer.write(json);
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new AnalyzeResponse("You're a looking at a " + best.getName(), new Date());
                } else {
                    return new AnalyzeResponse("Nothing found here", new Date());
                }


            } else {
                return new AnalyzeResponse("How about a picture?", new Date());
            }
        } catch (Exception e) {
            log.error(e.getMessage() + e.toString());
            e.printStackTrace();
            return new AnalyzeResponse("You probably did everything right. But there was an exception on the sever", new Date());
        }

    }

    /**
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public
    @ResponseBody
    String addRequest() {
        return "add";
        // return new AnalyzeResponse("Hi! If you want a picture analyzed just post it to this url", new Date());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    AnalyzeResponse addObject(@RequestParam("name") String name, @RequestParam("file") MultipartFile image) {
        log.info("Request for object adding");
        try {
            if (!image.isEmpty()) {
                File serverFile = Upload.uploadFile(root + File.separator + "object_images", name, image);

                if (serverFile == null)
                    throw new FileUploadException("There was a problem with the FileUpload");

                opencv_core.Mat descriptors = OCV_Descriptor.getDescriptor(serverFile);
                Serializer.serializeMat(name, descriptors);

                log.info(new Date() + " - File was successfully uploaded!");

                return new AnalyzeResponse("Wohoo. I got a picture.", new Date());

            } else {
                return new AnalyzeResponse("How about a picture?", new Date());
            }
        } catch (Exception e) {
            log.error(e.getMessage() + e.toString());
            e.printStackTrace();
            return new AnalyzeResponse("You probably did everything right. But there was an exception on the sever", new Date());
        }

    }

}
