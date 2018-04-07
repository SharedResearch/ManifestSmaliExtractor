/* @author: Khurram */

//For reading manifest file
package SmaliManifestExtractor;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReadXML {

	// this is main tag
	public static final String KEY_MANIFEST = "manifest"; // parent node
	// this is the tag where permissions are defined
	public static final String KEY_USES_PERMISSION = "uses-permission";
	public static final String KEY_INTENT_FILTER_ACTION = "action";
	public static final String KEY_RECEIVER = "receiver";
	public static final String KEY_INTENT_FILTER_ACTION_CAT="category";
   
	
    
	// our document which holds all the xml file
	private Document doc;

	// constructor, which actually loads data
	public ReadXML(String xml) throws ParserConfigurationException,
			SAXException, IOException {
		doc = getDomElement(xml);
	}

	// can call it without giving xml in constructor
	public Document getDomElement(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		// if doc is already initialized, through constructor, return that one
		if (doc != null) {
			return doc;
		}
		// 
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	

		DocumentBuilder db = dbf.newDocumentBuilder();

		// create inputsource object for xml reading
		// there are too many tutorials on Internet
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml));
		doc = db.parse(is);

		// return DOM
		return doc;
	}

	public Node getValue(Element item, String str) {
		// get nodelist by tag name, either manifest or uses-permission
		NodeList n = item.getElementsByTagName(str);
		// and return its value
		return this.getElementValue(n.item(0));
	}

	public final Node getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child;
					}
				}
			}
		}
		return null;
	}

}