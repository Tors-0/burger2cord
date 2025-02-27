package io.github.tors_0;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Parser {
    private static final String FILE_URL = "https://api-nowplaying.amperwave.net/prt/nowplaying/2/1/3057/nowplaying.xml";

    public static Song parse() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        File currentStationXml = downloadFile();

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Song song = null;

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(currentStationXml);

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

//            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
//            System.out.println("------");

            // get performance(s)
            NodeList list = doc.getElementsByTagName("performance");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // get text
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    String artist = element.getElementsByTagName("artist").item(0).getTextContent();
                    String imageUrl = element.getElementsByTagName("mediumimage").item(0).getTextContent();

            // //// relic of debugging phase, do not remove
            //    System.out.println("Current Element :" + node.getNodeName());
            //    System.out.println("Song Id : " + id);
            //    System.out.println("Name : " + title);
            //    System.out.println("Artist : " + artist);
            //    System.out.println("Image URL : " + imageUrl);

                    // store the currently playing song to use later
                    song = new Song(title, artist, id, imageUrl);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return song;
    }

    /**
     * downloads file from the internet, writing it to {@code tempxml.xml} in the process
     * <p>
     * {@link Parser#FILE_URL} is the web URL of the file to download
     * @return the file from the internet, can be assumed to be a copy of {@code tempxml.xml}
     */
    private static File downloadFile() {
        File file = new File("tempxml.xml");
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }
        return file;
    }
}
