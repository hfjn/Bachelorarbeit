package uos.jhoffjann.server.logic;

/**
 * Created by Jannik on 28.11.14.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * http://en.wikipedia.org/w/api.php?format=json&action=query&prop=revisions&titles=Hollywood&rvprop=content&rvsection=0&rvparse&continue
 */

public class WikiHandler {
    private static final Logger logger = LoggerFactory.getLogger(WikiHandler.class);

    private static DocumentBuilderFactory dbf;

    static {
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    }

    private static XPathFactory xpathf = XPathFactory.newInstance();
    private static String xexpr = "//html/body//div[@id='bodyContent']/p[1]";


    public static String getPlainSummary(String url) {
        try {
            // Open Wikipage
            logger.debug(url);
            System.out.println(url);
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1) Gecko/20090616 Firefox/3.5");
            InputStream uio = uc.getInputStream();
            InputSource src = new InputSource(uio);

            //Construct Builder
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document docXML = builder.parse(src);

            //Apply XPath
            XPath xpath = xpathf.newXPath();
            XPathExpression xpathe = xpath.compile(xexpr);
            String s = xpathe.evaluate(docXML);

            //Return Attribute
            if (s.length() == 0) {
                return null;
            } else {
                return s;
            }
        } catch (IOException ioe) {
            logger.error("Cant get XML", ioe);
            return null;
        } catch (ParserConfigurationException pce) {
            logger.error("Cant get DocumentBuilder", pce);
            return null;
        } catch (SAXException se) {
            logger.error("Cant parse XML", se);
            return null;
        } catch (XPathExpressionException xpee) {
            logger.error("Cant parse XPATH", xpee);
            return null;
        }
    }
}

