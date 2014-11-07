package uos.jhoffjann.server.controller;

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
import uos.jhoffjann.server.logic.OCV;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * SpringMVC Controller that lives on the server side and handles incoming HTTP requests. It is basically a servlet but
 * using the power of SpringMVC we can avoid a lot of the raw servlet and request/response mapping uglies that
 * servlets require and instead just deal with simple, clean Java Objects. For more information on SpringMVC see:
 * http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html
 */
@Controller
public class WelcomeController {

    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
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
     * This method is exposed as a REST service. The @RequestMapping parameter tells Spring that when a request comes in
     * to the server at the sub-url of '/welcome' (e.g. http://localhost:8080/opencvserver-server/welcome)
     * it should be directed to this method.
     * <p/>
     * In normal SpringMVC you would typically handle the request, attach some data to the 'Model' and redirect to a
     * JSP for rendering. In our REST example however we want the result to be an XML response. Thanks to some Spring
     * magic we can just return our bean, annotate it with @ResponseBody and Spring will magically turn this into XML
     * for us.
     * <p/>
     * We really didn't need the whole WelcomeMessage object here and could just have easily returned a String. That
     * wouldn't have made a very good example though, so the WelcomeMessage is here to show how Spring turns objects
     * into XML and back again for easy REST calls. The 'date' parameter was added just to give it some spice.
     *
     * @param name the name of the person to say hello to. This is pulled from the input URL. In this case we use a
     *             request parameter (i.e. ?name=someone), but you could also map it directly into the URL if you
     *             prefer. See the very good SpringMVC documentation on this for more information.
     * @return
     */

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
                byte[] bytes = image.getBytes();

                // Create a temporary directory to store the image
                String root = System.getProperty("user.dir");
                File dir = new File(root + File.separator + "tmpFiles");

                if (!dir.exists())
                    dir.mkdirs();

                // Create file on server
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name + new Date());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                log.info("File was successfully uploaded!");

                // TODO Start analyze thread for every picture in logos give back best one
                ExecutorService pool = Executors.newFixedThreadPool(10);

                Set<Future<Result>> set = new HashSet<Future<Result>>();

                dir = new File(root + File.separator + "object");

                if (dir.isDirectory()) { // make sure it's a directory
                    for (final File f : dir.listFiles(IMAGE_FILTER)) {
                        log.info("starting Analyzing");
                        Callable<Result> callable = new OCV(f, serverFile);
                        Future<Result> future = pool.submit(callable);
                        set.add(future);
                    }
                }
                Result best = null;
                for (Future<Result> future : set){
                    if(best == null)
                       best = future.get();
                    else if(best.getMatches() < future.get().getMatches()){
                        best = future.get();
                    }
                }

                if(best != null && best.getMatches() > 4){
                    return new AnalyzeResponse("You're a looking at a " + best.getName(), new Date());
                }
                else{
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    AnalyzeResponse addObject(@RequestParam("name") String name, @RequestParam("file") MultipartFile image) {
        log.info("Request for object adding");
        try {
            if (!image.isEmpty()) {
                byte[] bytes = image.getBytes();

                // Create a directory to store the image
                String root = System.getProperty("user.dir");
                File dir = new File(root + File.separator + "object");

                if (!dir.exists())
                    dir.mkdirs();

                // Create file on server
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                stream.close();

                log.info("File was successfully uploaded!");

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
