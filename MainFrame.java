/* @author: Khurram */

// Used for making GUI
package SmaliManifestExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtFolderPath;

	private JButton btnBrowse;
	private JButton btnStart;
	private JProgressBar progressBar;
	private JLabel lblTotalFiles;
	private JSpinner spinPairSize;

	private File file;
	private int pairSize = 1;
	private int pairCount = 0;
	private int totalApkFiles = -1;
	private String pathToWrite;
	private String outPutFolder;
	String filename ;

	/**
	 * Launch the application.
	 */
	
	public MainFrame(String pathToWrite ,File filePath,int pairSize)
	{
		this.filename=filePath.getName();
	    this.file=filePath;
	    outPutFolder=pathToWrite;
		this.pairSize=pairSize;
		if (file.getName().endsWith(".APK")
				|| file.getName().endsWith(".apk")) {
			extractDexAndSmaliFiles(
					file.getAbsolutePath(),
					outPutFolder);
			
		}
	
	}
	


/*	private int countApkFiles(File folder) {
		int count = 0;
		for (File f : folder.listFiles()) {
			if (f.getName().endsWith(".APK") || f.getName().endsWith(".apk")) {
				count++;
			}
		}
		return count;
	}*/

	public void extractDexAndSmaliFiles(String apkPath, String outputFolder) {
		deleteSmaliFilesFolder();
		extracDexFile(apkPath);
		extractSmaliFiles("smali_files");
		findSmaliFiles("smali_files", outputFolder);
	}

	public void extracDexFile(String path) {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals("classes.dex")) {
					System.out.println("Name: " + entry.getName());
					InputStream stream = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(entry.getName());
					int read = -1;
					byte[] buffer = new byte[1024];
					while ((read = stream.read(buffer)) > 0) {
						fos.write(buffer, 0, read);
					}
					fos.close();
					stream.close();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (zipFile != null) {
				zipFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void extractSmaliFiles(String outputFolder) {
		try {
			org.jf.baksmali.main.main(new String[] { "-o", outputFolder,
					"classes.dex" });
			System.out.println("Smali files created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void findSmaliFiles(String folder, String outputFolder) {
		File fileFolder = new File(folder);
		File[] files = fileFolder.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				findSmaliFiles(f.getAbsolutePath(), outputFolder);
			} else {
				if (f.getAbsolutePath().endsWith(".smali")) {
					compareAndSave(f.getAbsolutePath(), outputFolder);
					//lblTotalFiles.setText("Total Files: " + totalApkFiles
						//	+ " Current File: " + f.getName());
				}
			}
		}
	}

	private void compareAndSave(String f, String outputFolder) {
		try {
			//FileInputStream opcodesFile = new FileInputStream("/home/rmsh07/Android/Experiment/Benign/BenignExperimentFiles/9/OpCodes.txt");
			FileInputStream opcodesFile = new FileInputStream("/Users/khurram/OtherData/Java/Smali_Manifest_Extractor/OpCodes.txt");

			
			
			File temp = new File(outputFolder);
			if (!temp.exists()) {
				temp.mkdirs();
			}
			File folder = new File(new File(outPutFolder), "OpCode&MAnifest");
			if (!folder.exists()) {
				folder.mkdir();
			}
			FileOutputStream outPutFile = new FileOutputStream(folder.getAbsoluteFile()
					+ File.separator + filename + "_AndroidManifest.txt",true);
			
			
			
			Scanner opcode = new Scanner(opcodesFile);
			while (opcode.hasNext()) {
				String opcodeLine = opcode.nextLine();
				opcodeLine = opcodeLine.trim();
				System.out.println("opcode line: " + opcodeLine);
				FileInputStream smaliFile = new FileInputStream(f);
				Scanner smali = new Scanner(smaliFile);
				//String line="\n";
				//outPutFile.write(line.getBytes());
				while (smali.hasNext()) {
					String smaliLine = smali.nextLine();
					smaliLine = smaliLine.trim();
					System.out.println("smali line: " + smaliLine);
					if (smaliLine.contains(opcodeLine)) {
						System.out.println("matched");
						String lineToWrite;
						
						if (pairCount < pairSize) {
							lineToWrite = opcodeLine;// + " ";
							pairCount++;
						} else {
							lineToWrite = opcodeLine + "\n";//\n";
							pairCount = 0;
						}
						
						outPutFile.write(lineToWrite.getBytes());
						
						
						outPutFile.flush();
						break;
					}
				}
				smali.close();
				smaliFile.close();
			}
			opcode.close();
			opcodesFile.close();
			outPutFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteSmaliFilesFolder() {
		File f = new File("smali_files");
		f.delete();
	}
}
