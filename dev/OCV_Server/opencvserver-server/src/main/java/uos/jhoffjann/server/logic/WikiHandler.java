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
 * class which handles the parsing of a Wikipedia Web Page
 */
public class WikiHandler {
    private static final Logger logger = LoggerFactory.getLogger(WikiHandler.class);

    /**
     * returns the first paragraph of the specified Wikipedia Article
     * as found here: http://stackoverflow.com/a/1579485
     * @param url the url of the Article
     * @return the paragraph
     */
    public static String getPlainSummary(String url) {
        try {
            Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements paragraphs = doc.select("#mw-content-text p");
            Element firstParagraph = paragraphs.first();
            logger.debug(firstParagraph.text());
            return firstParagraph.text();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "No Wikipedia Article found! You still can add a Description in the json file.";
        }
    }
}

