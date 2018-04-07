/* @author: Khurram */
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

public class MainClass {

	public MainClass() {
		extracDexFile("WordMaster.apk");
		try {
			org.jf.baksmali.main.main(new String[] { "-o",
					"extracted_smali_files", "classes.dex" });
			System.out.println("Smali files created");
		} catch (IOException e) {
			e.printStackTrace();
		}
		findSmaliFiles("extracted_smali_files/com/");
	}

	public static void main(String[] args) {
		new MainClass();
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

	public void findSmaliFiles(String folder) {
		File fileFolder = new File(folder);
		File[] files = fileFolder.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				findSmaliFiles(f.getAbsolutePath());
			} else {
				if (f.getAbsolutePath().endsWith(".smali")) {
					compareAndSave(f.getAbsolutePath());
				}
			}
		}
	}

	private void compareAndSave(String f) {
		try {
			FileInputStream opcodesFile = new FileInputStream("OpCodes.txt");
			FileOutputStream outPutFile = new FileOutputStream("output.txt",
					true);
			Scanner opcode = new Scanner(opcodesFile);
			while (opcode.hasNext()) {
				String opcodeLine = opcode.nextLine();
				opcodeLine = opcodeLine.trim();
				System.out.println("opcode line: " + opcodeLine);
				FileInputStream smaliFile = new FileInputStream(f);
				Scanner smali = new Scanner(smaliFile);
				while (smali.hasNext()) {
					String smaliLine = smali.nextLine();
					smaliLine = smaliLine.trim();
					System.out.println("smali line: " + smaliLine);
					if (smaliLine.contains(opcodeLine)) {
						System.out.println("matched");
						String lineToWrite = smaliLine + "\n";
						outPutFile.write(lineToWrite.getBytes());
						outPutFile.flush();
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
}