/* @author: Khurram */

package SmaliManifestExtractor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

//import com.zai.ReadXML;
//import com.zai.XMLPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class smali_Manifest_ExtractorFrame extends JFrame {

	private JPanel contentPane;

	
	private JTextField txtFolderPath;

	
	private JButton btnBrowse;
	private JLabel lblProgress;
	
	private JProgressBar progressBar;
	private JButton btnStartExtracting;
	private JSpinner spinPairSize;
	private int totalApkFiles = -1;
	private int pairCounter=0;
	private boolean bPermissions=false;
	private boolean bIFAct=false;
	private boolean bIFCat=false;
	 
	
	//Users/khurram/OtherData/Java/Smali_Manifest_Extractor
	private String pathToPermList="/Users/khurram/OtherData/Java/Smali_Manifest_Extractor/PermissionList.txt";
	private String pathToIFCatList="/Users/khurram/OtherData/Java/Smali_Manifest_Extractor/IFCatList.txt";
	private String pathToIFActList="/Users/khurram/OtherData/Java/Smali_Manifest_Extractor/IFActList.txt";
	
	// Please Change the Folder Path to APK according to your requirement 
	private String folderPath="/Users/khurram/Desktop/Temp/Malware";
	//private String folderPath="/home/rmsh07/Android/Experiment/Benign/9"; 
		
	//Please Change Pair Size according to requireMent 
	private int pairSize=1; 
	
	
	private JLabel lblOpcodeManifest;
	

	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					smali_Manifest_ExtractorFrame frame = new smali_Manifest_ExtractorFrame();
					frame.startExtraction();
				//	frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	 public void startExtraction()
	 {
		 
		 
		 File folder = new File(folderPath);
			for (File f : folder.listFiles()) {
				if (f.getName().endsWith(".APK")
						|| f.getName().endsWith(".apk")) {
					try {
						if (extractAndroidManifest(f
								.getAbsolutePath())){
							
							MainFrame object=new MainFrame(folderPath,f,pairSize);
							//counter++;
							
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				
			}
		 
		  
		// System.out.println("File Extracted : " +counter);
		 
	 }
	
	
	
	

	
	
	
	
	
	private int countApkFiles(File folder) {
		int count = 0;
		for (File f : folder.listFiles()) {
			if (f.getName().endsWith(".APK") || f.getName().endsWith(".apk")) {
				count++;
			}
		}
		return count;
	}
	private boolean extractAndroidManifest(String filePath) throws IOException {
	//Method for Feature Extraction 
	 String permissions[]=null;
     String IFAction[]=null;
     String IFCat[]=null;
     String temp1,temp2,temp3;
     temp1=getFeatureString(filePath, "Permission"); 
     temp2= getFeatureString(filePath, "IFAction"); 
     temp3=  getFeatureString(filePath, "IFCategory"); 
          if(temp1!=null)
     {
     	permissions=temp1.split("\n");
     	
     }
      if(temp2!=null)
      {
     	 IFAction=temp2.split("\n");
     	 
      }
      if(temp3!=null)
      {
     	 
     	 IFCat=temp3.split("\n"); 
      }
      


       
     
		if (bIFCat == true && bIFAct == true && bPermissions == true) {
         
			  
			
			return true;
		} 
		
		else
			return false;
	}

	public String getFeatureString(String filePath, String FeatureName) {

		File folder = new File(new File(folderPath), "OpCode&MAnifest");
		if (!folder.exists()) {
			folder.mkdir();
		}
		String xml = getXml(filePath, FeatureName);
		String arraytoWrite[]=xml.split("\n");
        String lineToWrite;
		if (xml != null) {
			File f = new File(filePath);
			String filename = f.getName();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(folder.getAbsoluteFile()
						+ File.separator + filename + "_AndroidManifest.txt",true);
				//fos.write(xml.getBytes());
				for(int i=0; i<arraytoWrite.length;i++)
				{
					if (pairCounter < pairSize) {
						lineToWrite = arraytoWrite[i]; // + ",";
						pairCounter++;
					} else {
						lineToWrite = arraytoWrite[i] + "\n";//\n";
						pairCounter = 0;
					}
					
					fos.write(lineToWrite.getBytes());
				}
				
				
				
				
				
				// System.out.println(xml);
				// System.out.println(xml);
				fos.flush();
				if (FeatureName == "Permission") {
					bPermissions = true;
				}
				if (FeatureName == "IFCategory") {
					bIFCat = true;
				}
				if (FeatureName == "IFAction") {
					bIFAct = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// return result;
		return xml;

	}

	// Get XML 4
	public String getXml(String path, String feature) {
		String xmlFeatures = null;
		ArrayList<String> permStrings = new ArrayList<String>();

		ZipFile zipFile;
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals("AndroidManifest.xml")) {
					System.out.println("Name: " + path + " " + entry.getName());
					InputStream stream = zipFile.getInputStream(entry);
					XMLPrinter.getXmlAsString(stream);
					xmlFeatures = XMLPrinter.getXml();

					if (feature == "Permission") {
						xmlFeatures = filterXml(xmlFeatures,
								ReadXML.KEY_USES_PERMISSION);

						xmlFeatures = removeDuplication(xmlFeatures,
								"Permission");

					}

					if (feature == "IFCategory") {
						xmlFeatures = filterXml(xmlFeatures,
								ReadXML.KEY_INTENT_FILTER_ACTION_CAT);
						xmlFeatures = removeDuplication(xmlFeatures,
								"IFCategory");
					}

					if (feature == "IFAction") {
						xmlFeatures = filterXml(xmlFeatures,
								ReadXML.KEY_INTENT_FILTER_ACTION);
						xmlFeatures = removeDuplication(xmlFeatures, "IFAction");

					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

			xmlFeatures = null;

		}
		return xmlFeatures;
	}

	private String filterXml(String xml, String type) {

		String result = "";
		ReadXML xmlReader;
		try {
			xmlReader = new ReadXML(xml);
			Document doc = xmlReader.getDomElement(xml);

			NodeList nl = doc.getElementsByTagName(type);
			for (int i = 0; i < nl.getLength(); i++) {
				Element elem = (Element) nl.item(i);
				result += elem.getAttribute("android:name") + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// Remove Duplication

	public String removeDuplication(String xml, String featureName)
			throws IOException {
		ArrayList<String> duplicatedString = new ArrayList<String>();
		ArrayList<String> listReturn = new ArrayList<String>();
		String returnString = "";

		String convArray[] = xml.split("\n");
		for (String x : convArray) {
			duplicatedString.add(x);

		}
		// Removing duplication

		Set<String> s = new LinkedHashSet<String>(duplicatedString);
		ArrayList<String> uniqueList = new ArrayList<String>(s);

		listReturn = removeUserDefinedFeatures(uniqueList, featureName);
		String arrayToReturn[] = new String[listReturn.size()];

		for (int i = 0; i < listReturn.size(); i++) {
			returnString += listReturn.get(i) + "\n";

		}

		return returnString;

	}

	public ArrayList<String> removeUserDefinedFeatures(
			ArrayList<String> listToFilter, String featureName)
			throws IOException {

		File featureFile = null;
		int equalizer = 0;
		String currentLine;
		ArrayList<String> results = new ArrayList<String>();
		if (featureName == "Permission") {
			featureFile = new File(pathToPermList);
		}
		if (featureName == "IFCategory") {
			featureFile = new File(pathToIFCatList);

		}
		if (featureName == "IFAction") {
			featureFile = new File(pathToIFActList);
		}

		BufferedReader androidList = new BufferedReader(new FileReader(
				featureFile));

		while ((currentLine = androidList.readLine()) != null) {
			equalizer = 0;

			for (int j = 0; j < listToFilter.size(); j++) {

				if (currentLine.equals(listToFilter.get(j)) && equalizer == 0) {

					results.add(currentLine);
					equalizer = 1;

				}

			}

		}
		return results;

	}

}
