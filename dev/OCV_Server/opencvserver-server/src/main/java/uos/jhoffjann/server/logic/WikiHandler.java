package uos.jhoffjann.server.logic;

/**
 * Created by Jannik on 28.11.14.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * http://en.wikipedia.org/w/api.php?format=json&action=query&prop=revisions&titles=Hollywood&rvprop=content&rvsection=0&rvparse&continue
 */

public class WikiHandler {
    private static final Logger logger = LoggerFactory.getLogger(WikiHandler.class);


    public static String getPlainSummary(String url) {
        try {
            Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements paragraphs = doc.select("#mw-content-text p");
            Element firstParagraph = paragraphs.first();
            logger.debug(firstParagraph.text());
            return firstParagraph.text();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "Nothing found here!";
        }
    }
}

