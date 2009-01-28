/*
 * Some static utility methods. (Particularly for readin and writing XML files.)
 */

package storymaps;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Just a class to hold some static utility methods, particularly for reading
 * and writing XML files.
 * 
 * @author seanh
 */
public class Util {
    
    /**
     * The XStream object used for serializing and desirializing objects to XML.
     */
    private static XStream xstream = new XStream();

    /**
     * Records whether or not the XStream object has been initialised.
     */
    private static boolean initialised = false;
    
    /**
     * Initialise the XStream object to its desired state, e.g. setup aliases.
     * 
     */
    private static void init(){
        if (!initialised) {
            xstream.alias("Main",Main.Memento.class);
            xstream.alias("CardStore",StoryCards.Memento.class);
            xstream.alias("DisabledStoryCard",DisabledStoryCard.Memento.class);
            xstream.alias("ProppFunction",Function.Memento.class);
            xstream.alias("StoryCard", StoryCard.Memento.class);
            xstream.alias("StoryMap", StoryMap.Memento.class);
            xstream.alias("Placeholder", Placeholder.Memento.class);
            xstream.alias("FunctionEditor", FunctionEditor.Memento.class);
            initialised = true;
        }
    }
    
    /**
     * Write an object out to a file as XML.
     * 
     * Uses XStream and preserves newlines in the XML.
     * 
     * @param o The object to be serialized.
     * @param filename The filename of the XML file to write. Should be a
     * relative filename, e.g. "/storymaps/data/functions.xml". 
     */
    public static void writeXML(Object o, String filename) {
        
        // Serialize the object to XML.
        init(); // Initialise the XStream object, if it hasn't been already.
        String xml = xstream.toXML(o);
        
        // Add header and footer content to the XML.
        String head = "";
        try {            
            InputStream is = Util.class.getResourceAsStream("/storymaps/data/stylesheet.xml");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line=br.readLine()) != null) {
                head = head + line + '\n';
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException when reading stylesheet.xml" + e);
        }                
        //xml = head + xml + "</doc>";
        
        // Write the XML out to file.
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            out.print(xml);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException when writing "+filename+" "+e);
        }
    }
        
    /**
     * Return an org.w3c.dom.Document object parsed from a given XML file.
     * 
     * @param filename The absolute path to the XML file to parse.
     * @return The Document parsed from the XML file. May return null if an
     * exception occurred while parsing the file.
     */
    private static org.w3c.dom.Document readDocumentAbsolute(String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            org.w3c.dom.Document document = parser.parse(filename);
            return document;
        } catch (ParserConfigurationException e) {
            System.out.println(e);
        } catch (SAXException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;        
    }

    /**
     * Return an org.w3c.dom.Document object parsed from a given XML file.
     * 
     * @param filename The relative path to the XML file to parse.
     * @return The Document parsed from the XML file. May return null if an
     * exception occurred while parsing the file.
     */
    private static org.w3c.dom.Document readDocumentRelative(String filename) {
        try {
            InputStream is = Util.class.getResourceAsStream(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            org.w3c.dom.Document document = parser.parse(is);
            is.close();
            return document;
        } catch (ParserConfigurationException e) {
            System.out.println(e);
        } catch (SAXException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;        
    }    
    
    /**
     * Return an object read in from an XML file created by method writeXML
     * above.
     * 
     * Uses XStream and preserves newlines in the XML.
     * 
     * @param filename The absolute path to the XML file to read.
     * @return Object The result of deserializing the XML. If an exception
     * occurs while reading and parsing the XML file this method may return
     * null.
     */
    public static Object readXMLAbsolute(String filename) {
        org.w3c.dom.Document document = readDocumentAbsolute(filename);
        return readXMLFromDocument(document);
    }

    /**
     * Return an object read in from an XML file created by method writeXML
     * above.
     * 
     * Uses XStream and preserves newlines in the XML.
     * 
     * @param filename The relative path to the XML file to read, e.g.
     * "/storymaps/data/functions.xml".
     * @return Object The result of deserializing the XML. If an exception
     * occurs while reading and parsing the XML file this method may return
     * null.
     */
    public static Object readXMLRelative(String filename) {
        org.w3c.dom.Document document = readDocumentRelative(filename);
        return readXMLFromDocument(document);
    }
    
    /**
     * Return an object deserialized from the given XML Document.
     * 
     * Uses XStream and preserves newlines in the XML.
     * 
     * @param document The XML document to deserialize.
     * @return The deserialized object.
     */
    private static Object readXMLFromDocument(org.w3c.dom.Document document) {
        init(); // Initialise the XStream object, if it hasn't been already.
        DomReader domreader = new DomReader(document);            
        Object o = xstream.unmarshal(domreader);
        return o;
    }        
}