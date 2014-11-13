package uos.jhoffjann.server.logic;

import org.bytedeco.javacpp.opencv_core;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

/**
 * Created by jhoffjann on 13.11.14.
 * 
 */
public class XMLHandler {
    // static
    public static final int READ = 0;
    public static final int WRITE = 1;

    // variable
    private File file;
    private boolean isWrite;
    private Document doc;
    private Element rootElement;

    public TaFileStorage() {
        file = null;
        isWrite = false;
        doc = null;
        rootElement = null;
    }

    /**
     *
     * @param filePath
     * @param flags
     */
    public void open(String filePath, int flags) {
        try {
            if (flags == READ) {
                open(filePath);
            } else {
                create(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param filePath
     */
    public void open(String filePath) {
        try {
            file = new File(filePath);
            if (file == null || file.isFile() == false) {
                System.err.println("Can not open file: " + filePath);
            } else {
                isWrite = false;
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
                doc.getDocumentElement().normalize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param filePath
     */
    public void create(String filePath) {
        try {
            file = new File(filePath);
            if (file == null) {
                System.err.println("Can not write file: " + filePath);
            } else {
                isWrite = true;
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

                rootElement = doc.createElement("opencv_storage");
                doc.appendChild(rootElement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param tag
     * @return
     */
    public opencv_core.Mat readMat(String tag) {
        if (isWrite) {
            System.err.println("Try read from file with write flags");
            return null;
        }

        NodeList nodelist = doc.getElementsByTagName(tag);
        opencv_core.Mat readMat = null;

        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String type_id = element.getAttribute("type_id");
                if ("opencv-matrix".equals(type_id) == false) {
                    System.out.println("Fault type_id ");
                }

                String rowsStr = element.getElementsByTagName("rows").item(0).getTextContent();
                String colsStr = element.getElementsByTagName("cols").item(0).getTextContent();
                String dtStr = element.getElementsByTagName("dt").item(0).getTextContent();
                String dataStr = element.getElementsByTagName("data").item(0).getTextContent();

                int rows = Integer.parseInt(rowsStr);
                int cols = Integer.parseInt(colsStr);
                int type = 5;

                Scanner s = new Scanner(dataStr);

                if ("f".equals(dtStr)) {
                    type = 5;
                    readMat = new opencv_core.Mat(rows, cols, type);
                    float fs[] = new float[1];
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (s.hasNextFloat()) {
                                fs[0] = s.nextFloat();
                            } else {
                                fs[0] = 0;
                                System.err.println("Unmatched number of float value at rows=" + r + " cols=" + c);
                            }
                            readMat.put
                            readMat.put(r, c, fs);
                        }
                    }
                }
            }
        }
        return readMat;
    }

    /**
     *
     * @param tag
     * @param mat
     */

    public void writeMat(String tag, opencv_core.Mat mat) {
        try {
            if (isWrite == false) {
                System.err.println("Try write to file with no write flags");
                return;
            }

            Element matrix = doc.createElement(tag);
            matrix.setAttribute("type_id", "opencv-matrix");
            rootElement.appendChild(matrix);

            Element rows = doc.createElement("rows");
            rows.appendChild(doc.createTextNode(String.valueOf(mat.rows())));

            Element cols = doc.createElement("cols");
            cols.appendChild(doc.createTextNode(String.valueOf(mat.cols())));

            Element dt = doc.createElement("dt");
            String dtStr = "f";
            int type = mat.type();
            dt.appendChild(doc.createTextNode(dtStr));

            Element data = doc.createElement("data");
            String dataStr = dataStringBuilder(mat);
            data.appendChild(doc.createTextNode(dataStr));

            // append all to matrix
            matrix.appendChild(rows);
            matrix.appendChild(cols);
            matrix.appendChild(dt);
            matrix.appendChild(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param mat
     * @return
     */
    private String dataStringBuilder(opencv_core.Mat mat) {
        StringBuilder sb = new StringBuilder();
        int rows = mat.rows();
        int cols = mat.cols();
        int type = mat.type();
        float fs[] = new float[1];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mat.get(r, c, fs);
                sb.append(String.valueOf(fs[0]));
                sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     *
     */
    public void release() {
        try {
            if (isWrite == false) {
                System.err.println("Try release of file with no write flags");
                return;
            }

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(file);

            // write to xml file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // do it
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
