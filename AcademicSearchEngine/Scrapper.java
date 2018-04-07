import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



class Record
{
	String title="N/A";
	String titleUrl="N/A";
	String author="N/A";
	String date="N/A";
	String pub="N/A";
	String doi="N/A";
	String abstr="N/A";
	String keywords="N/A";
	
	void Reset()
	{
		title="N/A";
		titleUrl="N/A";
		author="N/A";
		date="N/A";
		pub="N/A";
		doi="N/A";
		abstr="N/A";
		keywords="N/A";	
	}
	
	void Set(String tit,String tu,String ath,String dt,String pb,String di,String abs,String kw)
	{
		title=tit;
		titleUrl=tu;
		author=ath;
		date=dt;
		pub=pb;
		doi=di;
		abstr=abs;
		keywords=kw;	
	}
	
	public static String convertToStdHTML(Record rec)
	{
		String htmlRes=
			"<!--Start of Record!-->"
				+ "<div name=\"record\">"
					+ "<h2 name=\"title\">"
						+ "<a href=\""+rec.titleUrl+"\">"
							+ rec.title
						+ "</a>"
					+ "</h2>"
						
						+ "<h4 name=\"authorMeta\">"
							+ "Author Name : "
							+ "<span name=\"author\">"
								+ rec.author
							+ "</span>"
						+ "</h4>"
							
						+ "<h4 name=\"dateMeta\">"
							+ "Date of Publish : "
							+ "<span name=\"date\">"
								+ rec.date
							+ "</span>"
						+ "</h4>"
						
						+ "<h4 name=\"pubMeta\">"
							+ "Publication/Journal Name : "
							+ "<span name=\"pub\">"
								+ rec.pub
							+ "</span>"
						+ "</h4>"
							
						+ "<h4 name=\"doiMeta\">"
							+ "DOI/Publication No : "
							+ "<span name=\"doi\">"
								+ rec.doi
							+ "</span>"
						+ "</h4>"
						
						+ "<h4 name=\"abstrMeta\">"
							+ "Abstract : "
						+ "</h4>"
						
						+ "<span name=\"abstr\">"
							+ rec.abstr
						+ "</span>"
						
						+ "<h4 name=\"keywordsMeta\">"
							+ "Keywords : "
						+ "</h4>"
						+ "<span name=\"keywords\">"
							+ rec.keywords
						+ "</span>"
					+"</div>";
						
		
		return htmlRes;
	}
	
}

/*
enum WebSites
{
	OutputInderScience,
	OutputIEEE,
	OutputACM,
	OutputScienceDirect,
	OutputWiley,
	OutputSpringer,
	OutputIOSPress,
	OutputCiteSeerX
}*/

public class Scrapper {


	public static WebDriver driver;
	private ChromeDriverService service;
	
	public static int TOTAL_PAGES_DEPTH=1;
	private static int Browser = 3 ;
	private static String ChromePATH;
	static boolean enableProxy = false;
	static String PROXY = "";
	static ArrayList<Record> allRecords;
	
	public static String OutputInderScience="Output/InderScience.html";
	public static String OutputIEEE="Output/IEEEExplorer.html";
	public static String OutputACM="Output/ACMdigitalLibrary.html";
	public static String OutputScienceDirect="Output/ScienceDirect.html";
	public static String OutputWiley="Output/Wiley.html";
	public static String OutputSpringer="Output/Springer.html";
	public static String OutputIOSPress="Output/IOSPress.html";
	public static String OutputCiteSeerX="Output/CiteSeerX.html";
	public static String OutputALL="Output/OutputALL.html";
	public static String OutputProcessed="Output/OutputProcessed.html";
	
	
	
	//====================== Basic Functions ======================
	
	
	public void OpenDriver() throws IOException 
	{

		DesiredCapabilities cap = new DesiredCapabilities();
		if (enableProxy) 
		{
			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);

			cap.setCapability(CapabilityType.PROXY, proxy);
		}

		switch (Browser) 
		{
		case 1:
			driver = new InternetExplorerDriver(cap);
			break;
		case 2:
			driver = new FirefoxDriver(cap);
			break;
		case 3:
			service = new ChromeDriverService.Builder()
					.usingDriverExecutable(
							new File(ChromePATH))
					.usingAnyFreePort().build();
			service.start();

			driver = new RemoteWebDriver(service.getUrl(),
			        DesiredCapabilities.chrome());
			break;
		}
	}

	
	public static void populateList()
	{
		allRecords = new ArrayList<Record>();
		Record currentRec;//=new Record();
	//	currentRec.Reset();
		
		WebDriver driverLoad;
		
		driverLoad = new FirefoxDriver();
	
		String pathDir = new java.io.File(OutputALL).getAbsolutePath();
		driverLoad.navigate().to(pathDir);
		
		ArrayList<WebElement> webElements;

		webElements=(ArrayList<WebElement>) driverLoad.findElements(By.xpath("/html/body/div"));
				
//		System.out.println(webElements.size());
		
		for (WebElement webElement : webElements )  
		{
			currentRec=new Record();
			currentRec.title=webElement.findElement(By.name("title")).getText();
			currentRec.titleUrl=webElement.findElement(By.name("title")).findElement(By.tagName("a")).getAttribute("href").toString();
			currentRec.author=webElement.findElement(By.name("author")).getText();
			currentRec.date=webElement.findElement(By.name("date")).getText();
			currentRec.pub=webElement.findElement(By.name("pub")).getText();
			currentRec.doi=webElement.findElement(By.name("doi")).getText();
			currentRec.abstr=webElement.findElement(By.name("abstr")).getText();
			currentRec.keywords=webElement.findElement(By.name("keywords")).getText();

			allRecords.add(currentRec);
		}
		
		driverLoad.close();
	}
	public static void ProcessResults()
	{
		Record swap;
		
		for (int c = 0; c < ( allRecords.size() - 1 ); c++) 
		{
			for (int d = 0; d < allRecords.size() - c - 1; d++) 
		      {
		        if (allRecords.get(d).title.toUpperCase().compareTo(allRecords.get(d+1).title.toUpperCase()) > 0 )
		        {
		          swap = allRecords.get(d);
		          allRecords.set(d, allRecords.get(d+1));
		          allRecords.set(d+1, swap);
		        }
		      }
		}
		
//		for (int i=0;i<allRecords.size();i++)
//		{	
//			System.out.println("***************");
//			System.out.println(allRecords.get(i).title);
//			System.out.println(allRecords.get(i).titleUrl);
//			System.out.println(allRecords.get(i).author);
//			System.out.println(allRecords.get(i).date);
//			System.out.println(allRecords.get(i).pub);
//			System.out.println(allRecords.get(i).doi);
//			System.out.println(allRecords.get(i).abstr);
//			System.out.println(allRecords.get(i).keywords);
//		}

	}
	
	
	public static void WriteProcessedDataToFile()
	{
		StringBuilder code = new StringBuilder();
		
		for(int i=0;i<allRecords.size();i++)
		{
			code.append(Record.convertToStdHTML(allRecords.get(i)));
		}
		
		try {
			writeToFile(code.toString(),OutputProcessed, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openSite(String site)
	{
		driver.navigate().to(site);
	}
	
	
	public void SetEditText(By by, String qry) {
		WebElement Search_editbox = driver.findElement(by);
		Search_editbox.sendKeys(qry);
	}
	
	
	public void ExplicitWait(int sec) throws InterruptedException 
	{
		Thread.sleep(sec*1000);
	}
	
	
	public void Click(By by) {
		try{
		
			driver.findElement(by).click();
		}
		catch(org.openqa.selenium.NoSuchElementException e){
			UILayer.statusText.setText("Button Not Loaded Properly. Check your Internet Connection");
		}
	}
	
	
	public static Boolean IsExist(By by)
	{
		boolean present;
		try {
		   driver.findElement(by);
		   present = true;
		} catch (NoSuchElementException e) {
		   present = false;
		}
		return present;
	}
	
	
	public void SelectDropDown(By by, String value) {
		Select dropdown = new Select(driver.findElement(by));
		dropdown.selectByVisibleText(value);
	}
	
	
	public static void WaitClickableButton(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement wait_search = wait.until(ExpectedConditions
				.elementToBeClickable(by));
	}
	
	
	public boolean isAttribtuePresent(WebElement element, String attribute) {
	    Boolean result = false;
	    try {
	        String value = element.getAttribute(attribute);
	        if (value != null){
	            result = true;
	        }
	    } catch (Exception e) {}

	    return result;
	}
	
	
	public void ReplaceEditText(By by, String Text) {
		try
		{
		WebElement EditText = driver.findElement(by);
		EditText.click();
		EditText.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		EditText.sendKeys(Text);
		}
		catch(org.openqa.selenium.NoSuchElementException e)
		{
			UILayer.statusText.setText("No Internet Connection. ");
		}
		
	}
	
	
	public WebElement ReturnWebElement(By by)
	{
		return driver.findElement(by);
	}
	
	
	public static ArrayList<WebElement> FindElements(By by)
	{
		return (ArrayList<WebElement>) driver.findElements(by);
	}
	
	
	public static String readFromFile(String fileName) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String everything="";
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	        br.close();
	    }
	    return everything;
	}
	
	
	
	public static void writeToFile(String text, String fileName,boolean app)
			throws IOException {
		// text = driver.getPageSource();
		Writer writer ;
		
		if(app)
		{
			writer = new BufferedWriter(new FileWriter(fileName, true));
		}
		else
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "utf-8"));
		
		writer.write(text);
		
		writer.close();

	}
	
	public static void CombineResults() throws IOException
	{
		writeToFile(readFromFile(OutputInderScience), OutputALL, false);
		writeToFile(readFromFile(OutputIEEE), OutputALL, true);
		writeToFile(readFromFile(OutputACM), OutputALL, true);
		writeToFile(readFromFile(OutputScienceDirect), OutputALL, true);
		writeToFile(readFromFile(OutputWiley), OutputALL, true);
		writeToFile(readFromFile(OutputSpringer), OutputALL, true);
		writeToFile(readFromFile(OutputIOSPress), OutputALL, true);
		writeToFile(readFromFile(OutputCiteSeerX), OutputALL, true);		
	}
	
	public void saveScreenshot(String name) throws IOException {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(name)); // "screenshot.png"
	}
	
	
	public void closeBrowser() {
		if(Browser==3)
			service.stop();

		driver.close();
	}
	
	
	
	
	
	//======================= Databases Functions ==================

	
	//------------ACM------------------
	public void getTextData_ACM() throws IOException 
	{
	//	int perPage=0;
		
		WebElement nextButton;
		StringBuilder code = new StringBuilder();
		Record record = new Record();
	
		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			
			
			ArrayList<WebElement> webElements;
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			
			String kw;
												  
			webElements = FindElements(By.xpath("/html/body/div/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr"));
											 	
			for (int i=1;i<webElements.size()-1;i++ ) 
			{
				
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-2)+" ) Completed" );
				
				WebElement webElement=webElements.get(i);
				
//				perPage++;
//				if(perPage>=5)
//				{
//					break;
//				}
//				
				
//+++			code+="<h2>";
				
//+++			code+=webElement.findElement(By.xpath("td[2]/table/tbody/tr[1]/td/a")).getText();
//+++			code+="</h2>";						   
//+++			ArrayList<WebElement> authors;
				
			
				record.title=webElement.findElement(By.xpath("td[2]/table/tbody/tr[1]/td/a")).getText();
				
				
				
				//html/body/div/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[3]/td[2]/table/tbody/tr[1]/td/div
				
				
				
//+++			code+=webElement.findElement(By.xpath("td[2]/table/tbody/tr[1]/td/div")).getText();
				

				record.author=webElement.findElement(By.xpath("td[2]/table/tbody/tr[1]/td/div")).getText();
				
				
				/*authors=(ArrayList<WebElement>) webElement.findElements(By.xpath("div/a"));
				
				code+="<h4> Authors : </h4>";
				
				for (WebElement subElement : authors ) 
				{
				//	System.out.println("##"+subElement.getText());	
					code+=subElement.getText();
					code+="<br/>";
				}
				code+="<br/>";
				*/
				
//+++			code+="<h4> Keywords : </h4>";
				if(webElement.findElement(By.xpath("td[2]/table/tbody/tr[5]")).getText().contains("Keywords"))
				{	
					kw=webElement.findElement(By.xpath("td[2]/table/tbody/tr[5]/td/div")).getText();
					kw=kw.substring(kw.indexOf("Keywords:")+9);
					
//+++				code+=kw;
					record.keywords=kw;
				}
				
				else
					record.keywords="No keywords found";
//+++				code+="No keywords found";
													
				if(webElement.findElement(By.xpath("td[2]/table/tbody/tr[3]/td")).getText().contains("Publisher"))
			//	if(IsExist(By.xpath("")))
				{												
					kw=record.pub=webElement.findElement(By.xpath("td[2]/table/tbody/tr[3]/td")).getText();
					kw=kw.substring(kw.indexOf("Publisher:")+10);
					record.pub=kw;
				}
				
				else
					record.pub="Not found";
				
				
			//	if(IsExist(By.xpath("/html/body/div[2]/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[1]")))
				{
					record.date=webElement.findElement(By.xpath("td[2]/table/tbody/tr[2]/td[1]")).getText();
				}
				
			//	else
			//		record.date="Not found";
				
				
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.xpath("td[2]/table/tbody/tr[1]/td/a")))
				         .keyUp(Keys.SHIFT)
				         .build();
	
				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);
	
				// change focus to new tab
				driver.switchTo().window(newTab.get(0));
	
				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='ext-gen12']"));
				
//				System.out.println("=>Abstract");
//+++				code+="<h4>Abstract</h4>";
				
				
				try {
					ExplicitWait(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(IsExist(By.xpath("//*[@id='abstract']/div/div/p")))
					record.abstr=driver.findElement(By.xpath("//*[@id='abstract']/div/div/p")).getText();
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract']/div/div/p")).getAttribute("outerHTML");
				else
					record.abstr="No Abstract Found";
//+++					code+="<p>No Abstract Found</p>";
			
				
				record.titleUrl=driver.getCurrentUrl();
				
				
				
									 //*[@id="divmain"]/table[2]/tbody/tr/td/table/tbody/tr[4]/td/span[1]
			
				/*!!if(IsExist(By.xpath("//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[1]")))
					record.pub=driver.findElement(By.xpath("//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[1]")).getText();
				else
					record.pub="Not Found";*/
				
				//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[1]
				
				
				//*[@id="divmain"]/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a
				
//+++				code+="<h4>DOI:</h4>";
									 //*[@id="divmain"]/table[2]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a
									 //*[@id="divmain"]/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a
			
													//*[@id="divmain"]/table/tbody/tr/td[1]/table[3]
													
				int spaceIndex=-1;
				
				if(driver.findElement(By.xpath("//*[@id='divmain']")).getText().contains("doi") )
				{	
					kw=driver.findElement(By.xpath("//*[@id='divmain']")).getText();
					spaceIndex=kw.indexOf(" ");
					
					if(spaceIndex<=-1 || spaceIndex>=kw.length())
						kw=kw.substring(kw.indexOf("doi")+4);
					else
						kw=kw.substring(kw.indexOf("doi")+4);
					
					
//					System.out.println("1="+kw.indexOf(" "));
//+++				code+=kw;
					record.doi=kw;
				}
				
				else if(driver.findElement(By.xpath("//*[@id='divmain']")).getText().contains("ISBN") )
				{
					
					kw=driver.findElement(By.xpath("//*[@id='divmain']")).getText();
					spaceIndex=kw.indexOf(" ");
					if(spaceIndex<=-1 || spaceIndex>=kw.length())
						kw=kw.substring(kw.indexOf("ISBN")+5);
					else
						kw=kw.substring(kw.indexOf("ISBN")+5);
					
//					System.out.println("2="+kw.indexOf(" "));
//+++				code+=kw;
					record.doi=kw;
					
				}
				else
					record.doi="Not Found";
					
				
				
				/*!!if(IsExist(By.xpath("//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a")))
						record.doi=driver.findElement(By.xpath("//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a")).getText();
//+++					code+=driver.findElement(By.xpath("//*[@id='divmain']/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a")).getText();
				else
						record.doi="Not Found";
//+++					code+="<p>Not Found</p>";
*/
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
//				System.out.println("*****Cod:****"+code);
				
//				System.out.println("------Rec:"+record);
				
				// Closing New Tab
				driver.close();
	
				// change focus back to old tab
				driver.switchTo().window(oldTab);
	
			}
			
			//Click Next Page Button if it is not the last page
//			perPage=0;						 
			if(driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[1]/td/table/tbody/tr[2]/td")).getText().contains("Next"))
			{		
				WaitClickableButton((By.xpath("/html/body/div[2]/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[1]/td/table/tbody/tr[2]/td/a[10]")));
				nextButton = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[1]/td/table/tbody/tr[2]/td/a[10]"));
										
				nextButton.click();
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
		}
		writeToFile(code.toString(), OutputACM,false);
		
		System.out.println("******ACM Status: Text Extraction was Successful*********");
	}
	
	//------------IEEE------------------
	
	public void getTextData_IEEE() throws IOException 
	{
		//int perPage=0;
		
			String kw = null;
			WebElement nextButton;
			StringBuilder code = new StringBuilder();
			
			Record record = new Record();
			
			ArrayList<WebElement> pages = (ArrayList<WebElement>) driver.findElements(
					//By.xpath("//*[@id='search_results_form']/div[10]/ul/li"));
					By.xpath("//*[@id='search_results_form']/div[10]/ul/li"));
			
			//*[@id="LayoutWrapper"]/div[6]/div[2]/div[2]/section[3]/ul/li[1]/xpl-result/div/div[1]/h2/a
			
			
			System.out.println("=>"+pages);
			
			for (int pageNo = 2; pageNo <= TOTAL_PAGES_DEPTH+2; pageNo++) 
			{
				ArrayList<WebElement> webElements;
				UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
				int i=0;									
				webElements = FindElements(By.xpath("//*[@id='search_results_form']/ul/li"));
				for (WebElement webElement : webElements ) 
				{
				//**//**here
					/*perPage++;
					if(perPage>=3)
					{
						break;
					}*/
//*					i++;
//*					if (i<13)
//*						continue;
					
					UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-2)+" ) Completed" );
					++i;
					
					//System.out.println("**"+webElement.findElement(By.className("listItemName")).getText());
//+++					code+="<h2>";
//+++					code+=webElement.findElement(By.tagName("a")).getText();
//+++					code+="</h2>";
					
					record.title=webElement.findElement(By.tagName("a")).getText();
					
					Actions builder = new Actions(driver);
					Action openLinkInNewTab = builder
					         .keyDown(Keys.SHIFT)
					         .click(webElement.findElement(By.tagName("a")))
					         .keyUp(Keys.SHIFT)
					         .build();

					openLinkInNewTab.perform();
					
					String oldTab = driver.getWindowHandle();
					ArrayList<String> newTab = new ArrayList<String>(
							driver.getWindowHandles());
					newTab.remove(oldTab);

					// change focus to new tab
					driver.switchTo().window(newTab.get(0));

					// wait until page loads
					WaitClickableButton(By.xpath("//*[@id='abstract-details-tab']"));
					System.out.println("=>Abstract");
		
//+++					code+="<h4>Abstract</h4>";
					if(IsExist(By.xpath("//*[@id='articleDetails']/div/div[1]")))
						record.abstr=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[1]")).getText();
//+++						code+=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[1]")).getText();
					else
						record.abstr="<p>No Abstract Found</p>";
//+++						code+="<p>No Abstract Found</p>";
				
		
//+++					code+="<h4>DOI: </h4>";
					if(IsExist(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")))
						record.doi=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")).getText();
//+++						code+=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")).getText();

					else
						record.doi="<p>Not Found</p>";
//+++						code+="<p>Not Found</p>";

					
					
//+++					code+="<h4>KeyWords: </h4>";
					
					
					if(IsExist(By.xpath("//*[@id='abstract-keyword-tab']")))
					{
						Click(By.xpath("//*[@id='abstract-keyword-tab']"));
						
						WaitClickableButton(By.xpath("//*[@id='abstract-details-tab']"));
						
						try {
							ExplicitWait(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(driver.findElement(By.xpath("//*[@id='abstractKeywords']/div/div")).getText().contains("AUTHOR KEYWORDS"))
						{
							kw=driver.findElement(By.xpath("//*[@id='abstractKeywords']/div/div")).getText();
																    //*[@id="abstractKeywords"]/div/div/div[1]/div/ul
																	//*[@id="abstractKeywords"]/div/div
							
							//kw=kw.substring(kw.indexOf("AUTHOR KEYWORDS")+15,kw.indexOf("IEEE TERMS"));
//+++							code+=kw;
							record.keywords=kw;
								
						}
						else
							record.keywords="<p>No Keywords Found</p>";
//+++							code+="<p>No Keywords Found</p>";
					}
					
					else
						record.keywords="<p>No Keywords Found</p>";
//+++						code+="<p>No Keywords Found</p>";

					
					code.append(Record.convertToStdHTML(record));
					record.Reset();
					
					
					// Closing New Tab
					driver.close();

					// change focus back to old tab
					driver.switchTo().window(oldTab);

				}
				
				//Click Next Page Button if it is not the last page
				//perPage=0;
				if(pages.size()>pageNo)
				{		
					pages.get(pageNo).click();	
				
				} 
				
				else 
				{
					System.out.println("Last Page reached");
					break;
				}
			}
			writeToFile(code.toString(), OutputIEEE,false);
			System.out.println("******IEEE Status: Text Extraction was Successful*********");
	}
	
	
	
	//**************IEEE 2
	
		public void getTextData_IEEE2()
		{
			//int perPage=0;
			String kw = null;
			WebElement nextButton;
			StringBuilder code = new StringBuilder();
			Record record = new Record();
			
			for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
			{
				
				ArrayList<WebElement> webElements;
				UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
				
				webElements = FindElements(By.xpath("//*[@id='LayoutWrapper']/div[6]/div[2]/div[2]/section[3]/ul/li"));
				
				int i=0;
				for (WebElement webElement : webElements ) 
				{
					UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );	
					++i;
					/*perPage++;
					if(perPage>=8)
					{
						break;
					}*/
									
//+++					code += webElement.findElement(By.xpath("xpl-result/div/div[1]/h2/a")).getAttribute("outerHTML");
	
					record.title=webElement.findElement(By.xpath("xpl-result/div/div[1]/h2/a")).getText();
					
					System.out.println("==>"+webElement.findElement(By.xpath("xpl-result/div/div[1]/h2/a")).getText());
					
					record.author=webElement.findElement(By.xpath("//*[@id='LayoutWrapper']/div[6]/div[2]/div[2]/section[3]/ul/li[7]/xpl-result/div/div[1]/p")).getText();
					
					record.date=webElement.findElement(By.xpath("xpl-result/div/div[1]/div[1]/div[1]/span[1]")).getText();
					
					
				//	code += webElement.findElement(By.xpath("ul/li[3]")).getAttribute("outerHTML");
				//	code += webElement.findElement(By.xpath("ul/li[4]")).getAttribute("outerHTML");
					
					//Open New Tab to get Abstract and Keywords
					
					Actions builder = new Actions(driver);
					Action openLinkInNewTab = builder
					         .keyDown(Keys.SHIFT)
					         .click(webElement.findElement(By.tagName("a")))
					         .keyUp(Keys.SHIFT)
					         .build();

					openLinkInNewTab.perform();
					
					String oldTab = driver.getWindowHandle();
					ArrayList<String> newTab = new ArrayList<String>(
							driver.getWindowHandles());
					newTab.remove(oldTab);

					// change focus to new tab
					driver.switchTo().window(newTab.get(0));

					// wait until page loads
					WaitClickableButton(By.xpath("//*[@id='abstract-details-tab']"));
					System.out.println("=>Abstract");
//+++					code+="<h4>Abstract</h4>";
					if(IsExist(By.xpath("//*[@id='articleDetails']/div/div[1]")))
						record.abstr=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[1]")).getText();
//+++						code+=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[1]")).getText();
					else
						record.abstr="<p>No Abstract Found</p>";
//+++						code+="<p>No Abstract Found</p>";

					
					
					record.pub=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[2]/a[1]")).getText();
					
					
					
					record.titleUrl=driver.getCurrentUrl();
					
					
//+++					code+="<h4>DOI: </h4>";
					if(IsExist(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")))
						record.doi=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")).getText();
//+++						code+=driver.findElement(By.xpath("//*[@id='articleDetails']/div/div[2]/div/dl[2]/dd[2]")).getText();
					else
						record.doi="<p>Not Found</p>";
//+++						code+="<p>Not Found</p>";

					
					
//+++					code+="<h4>KeyWords: </h4>";
					
					
					if(IsExist(By.xpath("//*[@id='abstract-keyword-tab']")))
					{
						Click(By.xpath("//*[@id='abstract-keyword-tab']"));
						
						WaitClickableButton(By.xpath("//*[@id='abstract-details-tab']"));
						
						try {
							ExplicitWait(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(IsExist(By.id("abstractKeywords")))
						{
							
							record.keywords=driver.findElement(By.id("abstractKeywords")).getAttribute("outerHTML");
							
		/*					kw=driver.findElement(By.xpath("//*[@id='abstractKeywords']/div/div")).getText();
																    //*[@id="abstractKeywords"]/div/div/div[1]/div/ul
																	//*[@id="abstractKeywords"]/div/div
							
							//kw=kw.substring(kw.indexOf("AUTHOR KEYWORDS")+15,kw.indexOf("IEEE TERMS"));
//+++							code+=kw;
							record.keywords=kw;
								*/
						}
						else
							record.keywords="<p>No Keywords Found</p>";
//+++							code+="<p>No Keywords Found</p>";
					}
					
					else
						record.keywords="<p>No Keywords Found</p>";
//+++						code+="<p>No Keywords Found</p>";

		
					code.append(Record.convertToStdHTML(record));
					record.Reset();
					
					
					// Closing New Tab
					driver.close();

					// change focus back to old tab
					driver.switchTo().window(oldTab);

				}
				

				//Click Next Page Button if it is not the last page
				
				if(driver.findElement(By.xpath("//*[@id='LayoutWrapper']/div[6]/div[2]/div[2]/section[3]/nav/ul/li[8]")).getAttribute("class").contains("disabled"))				
				{	
					
					System.out.println("Last Page reached");
					break;
				} 
				
				else 
				{
					nextButton = driver
							.findElement(By
									.xpath("//*[@id='LayoutWrapper']/div[6]/div[2]/div[2]/section[3]/nav/ul/li[8]/a"));
				
					nextButton.click();
				}
				//perPage=0;
			}

			try 
			{
				writeToFile(code.toString(), OutputIEEE,false);
			} 
			catch (IOException e) 
			{
				UILayer.statusText.setText("Unable to write Data");	
			}
			System.out.println("******IEEE Status: Text Extraction was Successful*********");
		}
		
	
	//==========================
		
	//------------SCIENCE DIRECT------------------
	public void getTextData_SD()
	{
		//int perPage=0;
		Record record = new Record();
		WebElement nextButton;
		StringBuilder code = new StringBuilder();

		String date="";
		
		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			ArrayList<WebElement> webElements;
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			
			webElements = FindElements(By.className("detail"));
			int i=0;
			for (WebElement webElement : webElements ) 
			{
			
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );
				++i;
				
				/*perPage++;
				if(perPage>=8)
				{
					break;
				}*/
								
//+++				code += webElement.findElement(By.xpath("ul/li[2]")).getAttribute("outerHTML");
				record.title=webElement.findElement(By.xpath("ul/li[2]")).getText();
				
//+++				code += webElement.findElement(By.xpath("ul/li[3]")).getAttribute("outerHTML");
				record.pub=webElement.findElement(By.xpath("ul/li[3]")).getText();
				
//+++				code += webElement.findElement(By.xpath("ul/li[4]")).getAttribute("outerHTML");
				record.author=webElement.findElement(By.xpath("ul/li[4]")).getText();
				
				
				String x=record.pub; 
				int first = x.indexOf(",");
				int second =x.indexOf(",", first + 1);

				date=x.substring(second+1);
				
				System.out.println("date1:"+date);
				
				if(date.contains("Issues") || date.contains("Supplement"))
					date=date.substring(date.indexOf(",")+1);
				
				if(date.contains("Pages"))
					date=date.substring(0,date.indexOf(","));

				System.out.println("date:"+date);
				record.date=date;
				
				System.out.println("pub"+record.pub);
				System.out.println("date:"+record.date);
				//Open New Tab to get Abstract and Keywords
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.tagName("a")))
				         .keyUp(Keys.SHIFT)
				         .build();

				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='header-area']"));
				
				if(IsExist(By.className("doi")))
					record.doi=driver.findElement(By.className("doi")).getText();
//+++					code+=driver.findElement(By.className("doi")).getAttribute("outerHTML");
				else
					record.doi="<p>No DOI Found</p>";
//+++					code+="<p>No DOI Found</p>";
				
				
				record.titleUrl=driver.getCurrentUrl();
				
				
//***				record.date=driver.findElement(By.xpath("//*[@id='centerInner']/div[1]/div[2]/p[1]/a")).getText();
				
	
				if(IsExist(By.id("frag_2")))
				{
				
					if(driver.findElement(By.id("frag_2")).getText().contains("Abstract"))
					{
						System.out.println("==> Abstract found");
						
						WaitClickableButton(By.xpath("//*[@id='frag_2']/div[1]"));
						
						if(driver.findElement(By.xpath("//*[@id='frag_2']/div[1]")).getText().contains("Abstract"))
						{
//+++						code+=driver.findElement(By.xpath("//*[@id='frag_2']/div[1]")).getAttribute("outerHTML");
							record.abstr=driver.findElement(By.xpath("//*[@id='frag_2']/div[1]")).getText();
	//					System.out.println("===============IF");
						}
						else if(driver.findElement(By.xpath("//*[@id='frag_2']/div[2]")).getText().contains("Abstract"))
						{
//+++						code+=driver.findElement(By.xpath("//*[@id='frag_2']/div[2]")).getAttribute("outerHTML");
							record.abstr=driver.findElement(By.xpath("//*[@id='frag_2']/div[2]")).getText();
	//					System.out.println("===============ELSE");
						}
			
//+++						code+="<h3>Keywords</h3>";			
						if(IsExist(By.className("keyword")))
							record.keywords=driver.findElement(By.className("keyword")).getText();
//+++						code+=driver.findElement(By.className("keyword")).getAttribute("outerHTML");
					
					}
					
					else
					{
						System.out.println("==> No Abstract found");
						record.abstr="<h3>No Abstract or Keywords Found</h3>";
//+++						code+="<h3>No Abstract or Keywords Found</h3>";
					}
				}
				
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
				
				// Closing New Tab
				
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);

			}
			
		
			//Click Next Page Button if it is not the last page
			if (IsExist(By.xpath("//*[@id='rsRightCol']/div/div[3]/div[1]/input")))
			{					
				nextButton = driver
						.findElement(By
								.xpath("//*[@id='rsRightCol']/div/div[3]/div[1]/input"));
			
				nextButton.click();
				
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
			//perPage=0;
		}

		try 
		{
			writeToFile(code.toString(), OutputScienceDirect,false);
		} 
		catch (IOException e) 
		{
			UILayer.statusText.setText("Unable to write Data");	
		}
		System.out.println("******Science Direct Status: Text Extraction was Successful*********");
	}
	
	
	
//------------IOS PRESS------------------
	public void getTextData_IOS() throws IOException
	{
		//int perPage=0;
		
		WebElement nextButton;
		StringBuilder code = new StringBuilder();
		Record record = new Record();
		String x;
		
		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			ArrayList<WebElement> webElements;
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
												
			webElements = FindElements(By.xpath("//*[@id='aspnetForm']/table/tbody/tr[1]/td[2]/table[2]/tbody/tr/td[1]/table[3]/tbody/tr"));
			int i=0;
			for (WebElement webElement : webElements ) 
			{
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );
				++i;
				
				/*perPage++;
				if(perPage>=3)
				{
					break;
				}*/
				
				
//+++				code+="<h2>";
//+++				code+=webElement.findElement(By.className("listItemName")).getText();
//+++				code+="</h2>";
				
				record.title=webElement.findElement(By.className("listItemName")).getText();
				
				ArrayList<WebElement> subElements;
				subElements=(ArrayList<WebElement>) webElement.findElements(By.xpath("td[3]/div/table/tbody/tr"));
				
				
				
				for (WebElement subElement : subElements ) 
				{
					if(subElement.getText().contains("Journal"))
						record.pub=subElement.findElement(By.xpath("td[2]")).getText();
					
					if(subElement.getText().contains("Author"))
						record.author=subElement.findElement(By.xpath("td")).getText();
					
					if(subElement.getText().contains("Issue"))
					{
						x=subElement.findElement(By.xpath("td[2]")).getText();
						record.date=x.substring(x.indexOf("/")+1);
					}
					
					if(subElement.getText().contains("DOI"))
						record.doi=subElement.findElement(By.xpath("td[2]")).getText();
					
					
				//	System.out.println("##"+subElement.getText());	
//+++					code+=subElement.getAttribute("outerHTML");
//+++					code+="<br/>";
				}
//+++				code+="<br/>";
				
				
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.className("listItemName")).findElement(By.tagName("a")))
				         .keyUp(Keys.SHIFT)
				         .build();

				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='search-form']/div/button"));
				
//+++				code+="<h3>Abstract</h3>";
	
				
				
				if(driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/div[1]")).getText().contains("Log in or register to view or purchase instant access"))
				{
					System.out.println("==>Paid");
//++++					code+=driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/header/div/div[2]")).getText();
					record.abstr=driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/header/div/div[2]")).getText();
				}
				else
				{
					Click(By.xpath("/html/body/div[4]/div[1]/div/div[1]/div[2]/button/span[1]"));
					System.out.println("=>Abstract");
					try {
						ExplicitWait(2);
					} catch (InterruptedException e) {
						
					}
											  
					if(driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/div[1]/div[2]")).getText().contains("This journal is no longer published by IOS Press"))
//++++						code+="<p>No Abstract Found</p>";
						record.abstr="No Abstract Found";
						
					else	
//++++						code+=driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/div[1]/div[2]")).getText();
						record.abstr=driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/div[1]/div[2]")).getText();
				}
			
				
			//	/html/body/div[4]/div[1]/div/div[1]/div[2]/button/span[1]
				
				
				
//				code+="<h4>KeyWords</h4>";
//				if(IsExist(By.xpath("//*[@id='aspnetForm']/table/tbody/tr[1]/td[2]/table[2]/tbody/tr/td[1]/div[4]")))
//					code+=driver.findElement(By.xpath("//*[@id='aspnetForm']/table/tbody/tr[1]/td[2]/table[2]/tbody/tr/td[1]/div[4]")).getAttribute("outerHTML");
//				else
//					code+="<p>No Keywords Found</p>";

				
				record.titleUrl=driver.getCurrentUrl();	
				
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
				// Closing New Tab
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);

			}
			
			//Click Next Page Button if it is not the last page
			//perPage=0;
			if(driver.findElement(By.id("ctl00_MainPageContent_ctl03_ctl00_ctl00_ctl00_ctl00")).getText().contains("Next"))
			{		
				WaitClickableButton((By.xpath("//*[@id='ctl00_MainPageContent_ctl03_ctl00_ctl00_ctl00_ctl00']/tbody/tr/td[2]/a[5]")));
				nextButton = driver.findElement(By.xpath("//*[@id='ctl00_MainPageContent_ctl03_ctl00_ctl00_ctl00_ctl00']/tbody/tr/td[2]/a[5]"));
										
				nextButton.click();
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
		}
		writeToFile(code.toString(), OutputIOSPress,false);
		System.out.println("******IOS-Press Status: Text Extraction was Successful*********");
	}

		
	//------------SPRINGER------------------
	public void getTextData_SPRINGER() throws IOException
	{
		//int perPage=0;
		Record record = new Record();
		WebElement nextButton;
		StringBuilder code = new StringBuilder();

		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			
			ArrayList<WebElement> webElements;
			WaitClickableButton(By.xpath("//*[@id='results-list']/li"));
			
			webElements = FindElements(By.xpath("//*[@id='results-list']/li"));
			int i=0;
			for (WebElement webElement : webElements ) 
			{
			/*	perPage++;
				if(perPage>=3)
				{
					break;
				}
			*/
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );
				++i;

				System.out.println("**"+webElement.findElement(By.tagName("a")).getText());
//+++				code+="<h2>";
//+++				code+=webElement.findElement(By.tagName("a")).getText();
//+++				code+="</h2>";
				record.title=webElement.findElement(By.tagName("a")).getText();
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.tagName("a")))
				         .keyUp(Keys.SHIFT)
				         .build();
				
				openLinkInNewTab.perform();

				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

				// wait until page loads
				WaitClickableButton(By.id("title"));
				
				if(IsExist(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[3]/dd[3]")))
					record.author=driver.findElement(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[3]/dd[3]")).getText();
				
				else if((IsExist(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[3]/dd[1]/ul/li/a[1]"))))
					record.author=driver.findElement(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[3]/dd[1]/ul/li/a[1]")).getText();
			
				else
					record.author="Not found";
				
				if(IsExist(By.xpath("//*[@id='dt-abstract-about-book-chapter-copyright-year']")))
				{
//+++					code+=driver.findElement(By.xpath("//*[@id='dt-abstract-about-book-chapter-copyright-year']")).getAttribute("outerHTML");								   
					
//+++					System.out.println("**2"+driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-copyright-year']")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-copyright-year']")).getAttribute("outerHTML");
					record.date=driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-copyright-year']")).getText();
				}
				
				else if(IsExist(By.xpath("//*[@id='abstract-about-cover-date']")))
					record.date=driver.findElement(By.xpath("//*[@id='abstract-about-cover-date']")).getText();
				
				else
					record.date="Not found";
				
				
/*				if(IsExist(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[1]/dt[6]")))
				{
//+++					System.out.println("**3"+driver.findElement(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[1]/dt[6]")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract-about']/div[2]/div/div/dl[1]/dt[6]")).getAttribute("outerHTML");
				}
*/
				if(IsExist(By.xpath("//*[@id='abstract-about-book-chapter-doi']")))
				{
//+++					System.out.println("**4"+driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-doi']")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-doi']")).getAttribute("outerHTML");
					record.doi=driver.findElement(By.xpath("//*[@id='abstract-about-book-chapter-doi']")).getText();
				}
				
				
				else if(IsExist(By.xpath("//*[@id='abstract-about-doi']")))
					record.doi=driver.findElement(By.xpath("//*[@id='abstract-about-doi']")).getText();
				
				else
					record.doi="Not found";
				
				
				record.abstr=driver.findElement(By.id("kb-nav--main")).getText();
				
				
				if(IsExist(By.xpath("//*[@id='dt-abstract-about-publisher']")))
				{
//+++					System.out.println("**5"+driver.findElement(By.xpath("//*[@id='dt-abstract-about-publisher']")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='dt-abstract-about-publisher']")).getAttribute("outerHTML");
//+++					System.out.println("**6"+driver.findElement(By.xpath("//*[@id='abstract-about-publisher']")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract-about-publisher']")).getAttribute("outerHTML");
					record.pub=driver.findElement(By.xpath("//*[@id='abstract-about-publisher']")).getText();
				}
				
				else if(IsExist(By.id("abstract-about-publication")))
					record.pub=driver.findElement(By.id("abstract-about-publication")).getText();
				
				else
					record.pub="Not found";
				
//+++				code+="<h4>Keywords</h4>";
				
				if(IsExist(By.xpath("//*[@id='abstract-about-keywords']")))
				{
//+++					System.out.println("**7"+driver.findElement(By.xpath("//*[@id='abstract-about-keywords']")).getAttribute("outerHTML"));
//+++					code+=driver.findElement(By.xpath("//*[@id='abstract-about-keywords']")).getAttribute("outerHTML");
					
					record.keywords=driver.findElement(By.xpath("//*[@id='abstract-about-keywords']")).getText();
					
				}
				else
					record.keywords="No Keywords Found";
					
//+++				code+="<p>No Keywords Found<p>";
				
				System.out.println("=>Extracting Details");
				
				
				record.titleUrl=driver.getCurrentUrl();
				
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
				// Closing New Tab
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);
				
			}
			
			//Click Next Page Button if it is not the last page
			//perPage=0;
			
			System.out.println(driver.findElement(By.className("next")).getTagName().toString());
			
			if(driver.findElement(By.className("next")).getTagName().toString().compareTo("a")==0)
			{	
				System.out.println("Next Page");					
				WaitClickableButton((By.className("next")));
				nextButton = driver.findElement(By.className("next"));
										
				nextButton.click();
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
			
		}
						
		writeToFile(code.toString(), OutputSpringer,false);
		System.out.println("******Springer Status: Text Extraction was Successful*********");
	}

	
	//------------CiteSeerX------------------
	public void getTextData_CiteSeerX() throws IOException
	{
		//int perPage=0;
		Record record = new Record();		
		WebElement nextButton;
		StringBuilder code = new StringBuilder();
		String date="";
		
		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			
			ArrayList<WebElement> webElements;
			WaitClickableButton(By.xpath("//*[@id='result_list']/div"));
			
			webElements = FindElements(By.xpath("//*[@id='result_list']/div"));
			int i = 0;
			for (WebElement webElement : webElements ) 
			{
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );
				i++;
				
				/*perPage++;
				if(perPage>=3)
				{
					break;
				}*/
	
			
				System.out.println("**"+webElement.findElement(By.tagName("a")).getText());
//+++				code+="<h2>";
//+++				code+=webElement.findElement(By.tagName("a")).getText();
//+++				code+="</h2>";
				
				record.title=webElement.findElement(By.tagName("a")).getText();
				
			
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.tagName("a")))
				         .keyUp(Keys.SHIFT)
				         .build();

				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='viewHeader']/h2"));

				
				if(IsExist(By.xpath("//*[@id='viewHeader']/h2")))
				{
				
					date=driver.findElement(By.xpath("//*[@id='viewHeader']/h2")).getText();
					System.out.println("***"+date);
					if(date.contains("(") && date.contains(")"))
					{
						record.date=date.substring(date.indexOf('(')+1,date.indexOf(')'));
					}
					else
						record.date="Not Found";
				}

				String url=driver.getCurrentUrl();
				url=url.substring(url.indexOf("?")+1,url.indexOf("&"));
	
				record.doi=url;
				
//+++				code+=url;
				
			
				if(IsExist(By.id("docAuthors")))
				{
//+++				code+="<h4>Author</h4>";
//+++				code+=driver.findElement(By.id("docAuthors")).getText();
					record.author=driver.findElement(By.id("docAuthors")).getText();
				}
					
				record.titleUrl=driver.getCurrentUrl();
				
				if(IsExist(By.id("abstract")))
				{
//+++				code+=driver.findElement(By.id("abstract")).getAttribute("outerHTML");								   
					record.abstr=driver.findElement(By.id("abstract")).getAttribute("outerHTML");
				}
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
	
				System.out.println("=>Abstract");
				
				// Closing New Tab
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);
				
			}
			
			//Click Next Page Button if it is not the last page
			//perPage=0;
			
			//System.out.println(driver.findElement(By.xpath("//*[@id='pager']/a")).getText().contains("Next"));
			
			if(driver.findElement(By.xpath("//*[@id='pager']/a")).getText().contains("Next"))
			{	
				System.out.println("Next Page");					
				WaitClickableButton((By.xpath("//*[@id='pager']/a")));
				nextButton = driver.findElement(By.xpath("//*[@id='pager']/a"));
										
				nextButton.click();
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
			
		}
				
		writeToFile(code.toString(), OutputCiteSeerX,false);
		System.out.println("******CiteSeerX Status: Text Extraction was Successful*********");
	}

	
//------------InderScience------------------
	public void getTextData_Inderscience() throws IOException, InterruptedException
	{
		//int perPage=0;
		
		WebElement nextButton;
		StringBuilder code = new StringBuilder();
		Record record = new Record();

		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			ArrayList<WebElement> webElements;
			WaitClickableButton(By.xpath("//*[@id='main']/div/table"));
			
			webElements = FindElements(By.xpath("//*[@id='main']/div/table"));
								
			for (int i = 1; i < webElements.size() - 1; i++)
			{	
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-2)+" ) Completed" );
				
//				perPage++;
//				if(perPage>=3)
//				{
//					break;
//				}

//+++				code+="<h2>";
//+++				code+=webElements.get(i).findElement(By.xpath("tbody/tr[1]/td[3]")).getText();
//+++				code+="</h2>";
				
				record.title=webElements.get(i).findElement(By.xpath("tbody/tr[1]/td[3]")).getText();
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElements.get(i).findElement(By.xpath("tbody/tr[3]/td[2]/a[1]")))
				         .keyUp(Keys.SHIFT)
				         .build();

				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

			//	System.out.println("Code1: "+code);
				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='main']/div/div/table/tbody"));
			//	System.out.println("Code2: "+code);
				ArrayList<WebElement> subElements = null;
				
				subElements = FindElements(By.xpath("//*[@id='main']/div/div/table/tbody/tr"));
													
				//for (WebElement subElement : subElements ) 
				for (int k=1; k < subElements.size() - 2; k++)
				{
				//	System.out.println("Code: "+k+" :"+code);
					
//+++					code+="<h4>";
//+++					code+=subElements.get(k).findElement(By.xpath("td[2]")).getText();
//+++					code+="</h4>";
	
					if(subElements.get(k).findElement(By.xpath("td[2]")).getText().contains("Author"))
						record.author=subElements.get(k).findElement(By.xpath("td[3]")).getText();
					
					else if(subElements.get(k).findElement(By.xpath("td[2]")).getText().contains("Journal"))
						record.pub=subElements.get(k).findElement(By.xpath("td[3]")).getText();
					
					else if(subElements.get(k).findElement(By.xpath("td[2]")).getText().contains("Abstract"))
						record.abstr=subElements.get(k).findElement(By.xpath("td[3]")).getText();
					
					else if(subElements.get(k).findElement(By.xpath("td[2]")).getText().contains("Keywords"))
						record.keywords=subElements.get(k).findElement(By.xpath("td[3]")).getText();
					
					else if(subElements.get(k).findElement(By.xpath("td[2]")).getText().contains("DOI"))
						record.doi=subElements.get(k).findElement(By.xpath("td[3]")).getText();
					
					
					
//+++					code+="<p>";
//+++					code+=subElements.get(k).findElement(By.xpath("td[3]")).getText();
//+++					code+="</p>";
				}
				
//+++				code+="<br/>";
				
				
				if(record.pub.contains("-"))
					record.date=record.pub.substring(record.pub.indexOf("-")-5, record.pub.indexOf("-"));
				else
					record.date="Not found";
				
				
				record.titleUrl=driver.getCurrentUrl();
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
				
				// Closing New Tab
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);
				
			}
			
			//Click Next Page Button if it is not the last page
//			perPage=0;
			
			
			if(driver.findElement(By.xpath("//*[@id='main']/div/table[1]/tbody/tr[1]/td[2]/table/tbody/tr")).getText().contains("Next"))
			{	
				System.out.println("Next Page");					
				WaitClickableButton((By.xpath("//*[@id='main']/div/table[1]/tbody/tr[1]/td[2]/table/tbody/tr/td[12]/a")));
				nextButton = driver.findElement(By.xpath("//*[@id='main']/div/table[1]/tbody/tr[1]/td[2]/table/tbody/tr/td[12]/a"));
										
				nextButton.click();
				ExplicitWait(2);
			} 
			
			else 
			{
				System.out.println("Last Page reached");
				break;
			}
		}
				
		writeToFile(code.toString(), OutputInderScience,false);
		System.out.println("******InderScience Status: Text Extraction was Successful*********");
	}

	//------------Wiley------------------
	public void getTextData_Wiley() throws IOException
	{
		//int perPage=0;
		
		WebElement nextButton = null;
		StringBuilder code = new StringBuilder();
		Record record = new Record();

		for (int pageNo = 1; pageNo <= TOTAL_PAGES_DEPTH; pageNo++) 
		{
			UILayer.statusText.setText("Data Collection In Progress... (Page "+pageNo+" / "+TOTAL_PAGES_DEPTH+" )" );
			
			ArrayList<WebElement> webElements;
			
			WaitClickableButton(By.xpath("//*[@id='searchResults']/form"));
			
			webElements = FindElements(By.xpath("//*[@id='searchResultsList']/ol/li"));
			
			int i=0;
			for (WebElement webElement : webElements ) 
			{
				
				UILayer.currPageStatus.setText("( "+i+" / "+(webElements.size()-1)+" ) Completed" );
				i++;
				
				/*perPage++;
				if(perPage>=3)
				{
					break;
				}*/
			
				System.out.println("**"+webElement.findElement(By.tagName("a")).getText());
//+++				code+="<h2>";
//+++				code+=webElement.findElement(By.tagName("a")).getText();
//+++				code+="</h2>";
				
				
				record.title=webElement.findElement(By.tagName("a")).getText();
				
				
				Actions builder = new Actions(driver);
				Action openLinkInNewTab = builder
				         .keyDown(Keys.SHIFT)
				         .click(webElement.findElement(By.tagName("a")))
				         .keyUp(Keys.SHIFT)
				         .build();

				openLinkInNewTab.perform();
				
				String oldTab = driver.getWindowHandle();
				ArrayList<String> newTab = new ArrayList<String>(
						driver.getWindowHandles());
				newTab.remove(oldTab);

				// change focus to new tab
				driver.switchTo().window(newTab.get(0));

				// wait until page loads
				WaitClickableButton(By.xpath("//*[@id='articleTitle']/h1"));

				
				if(IsExist(By.id("authors")))
				{
//+++					code+="<h4>Author</h4>";
//+++					code+=driver.findElement(By.id("authors")).getText();								   
					record.author=driver.findElement(By.id("authors")).getText();
				}
				
				if(IsExist(By.id("publishedOnlineDate")))
				{
//+++					code+="<h4>Date</h4>";
//+++					code+=driver.findElement(By.id("publishedOnlineDate")).getText();								   
					record.date=driver.findElement(By.id("publishedOnlineDate")).getText();	
				}
				
				if(IsExist(By.id("doi")))
				{
//+++					code+="<h4>DOI</h4>";
//+++					code+=driver.findElement(By.id("doi")).getText();								   
					record.doi=driver.findElement(By.id("doi")).getText();
				}
				
				
				if(IsExist(By.className("keywordLists")))
				{
//+++					code+=driver.findElement(By.className("keywordLists")).getText();
					record.keywords=driver.findElement(By.className("keywordLists")).getText();
				}
				
				if(IsExist(By.className("para")))
				{
//+++					code+=driver.findElement(By.className("para")).getText();
					record.abstr=driver.findElement(By.className("para")).getText();
				}
				
				
				if(IsExist(By.id("productTitle")))
				{
//+++					code+=driver.findElement(By.className("para")).getText();
					record.pub=driver.findElement(By.id("productTitle")).getText();
				}

				
				record.titleUrl=driver.getCurrentUrl();
				
				code.append(Record.convertToStdHTML(record));
				record.Reset();
				
				
				// Closing New Tab
				driver.close();

				// change focus back to old tab
				driver.switchTo().window(oldTab);
				
			}
			
				
				
			//Click Next Page Button if it is not the last page
			//perPage=0;
			
			Boolean nxt=false;
		//	if(IsExist(By.xpath("//*[@id='searchResultsList']/div[1]")))
			
				ArrayList<WebElement> Nextlinks;
				Nextlinks = FindElements(By.xpath("//*[@id='searchResultsList']/div[1]/ol/li"));
				for (WebElement Nextlink : Nextlinks ) 
				{
					if(nxt)
					{
						System.out.println("Next Page");	
						nextButton = Nextlink.findElement(By.tagName("a"));	
						nextButton.click();
						nxt=false;
						break;
					}
					
					if(isAttribtuePresent(Nextlink, "Class"))
					{
						nxt=true;
					}
					
					else
					{
						nxt=false;
					}	
					
				}
			
			
			if(nxt)
			{
				System.out.println("Last Page reached");
				break;
			}
			
		}
		
		writeToFile(code.toString(), OutputWiley,false);
		System.out.println("******Wiley Status: Text Extraction was Successful*********");
	}

	
	//=============================DataBases Drivers===========================
	
	
	//------------ACM------------------
	public static void ACMdriver(Scrapper webSrcapper, String qry) throws IOException 
	{
		//System.out.println(UILayer.endYearComboBox.getSelectedItem().toString());
		
		webSrcapper.openSite("http://dl.acm.org/");
		UILayer.statusText.setText("Opening WebSite");
		webSrcapper.SetEditText(By.name("query"), qry);
		webSrcapper.Click(By.name("Go"));

		
		if(UILayer.checkBoxYearsFilter.isSelected())
		{
			// Filters Publication Year
			
			webSrcapper.Click(By.xpath("//*[@id='Refine by Publications']/a[1]"));
			webSrcapper.SelectDropDown(By.name("from_pubyr"), UILayer.startYearComboBox.getSelectedItem().toString());
			webSrcapper.SelectDropDown(By.name("to_pubyr"), UILayer.endYearComboBox.getSelectedItem().toString());
			webSrcapper.Click(By.name("submit"));
		}
		
		webSrcapper.getTextData_ACM();
		webSrcapper.saveScreenshot(OutputACM.replace("html", "png"));
	}
		
	//------------IEEE------------------
	public static void IEEEdriver(Scrapper webSrcapper, String qry) throws IOException 
	{	

		if(UILayer.checkBoxYearsFilter.isSelected())
		{	
			webSrcapper.openSite("//http://ieeexplore.ieee.org/search/searchresult.jsp?queryText="+qry+"&ranges="+UILayer.startYearComboBox.getSelectedItem().toString()+"_"+UILayer.endYearComboBox.getSelectedItem().toString()+"_"+"Year&searchField=Search_All");		
		}
		else
		{
			webSrcapper.openSite("http://ieeexplore.ieee.org/search/searchresult.jsp?newsearch=true&queryText="+qry);		
		}	
		
		
			
//		webSrcapper.SetEditText(By.id("input-basic"), qry);
//		webSrcapper.Click(By.xpath("//button[@type='submit']"));
	
//		//Optional Filters Content Type
//		webSrcapper.Click(By.id("Content_Type_4291944246"));
//		webSrcapper.Click(By.id("Content_Type_4294965216"));
//		webSrcapper.WaitClickableButton(By.className("facet-submit-refinement"));
//		webSrcapper.Click(By.className("facet-submit-refinement"));
//
//		//Optional Filters Publication Year
//		webSrcapper.ReplaceEditText(By.id("text_startyear"), "2000");
//		webSrcapper.ReplaceEditText(By.id("text_endyear"), "2015");
//		webSrcapper.WaitClickableButton(By.id("submit-pub-year"));
//		webSrcapper.Click(By.id("submit-pub-year"));

		webSrcapper.getTextData_IEEE2();
		webSrcapper.saveScreenshot(OutputIEEE.replace("html", "png"));

	}
	
	//------------SCIENCE DIRECT------------------
	public static void SCIENCE_DIRECTdriver(Scrapper webSrcapper, String qry) throws IOException 
	{
		webSrcapper.openSite("http://www.sciencedirect.com/science/search");
		webSrcapper.SetEditText(By.id("SearchText"), qry);
		
		
		if(UILayer.checkBoxYearsFilter.isSelected())
		{
			//Optional Filters
			webSrcapper.Click(By.id("dateSelectRadio"));
			webSrcapper.SelectDropDown(By.xpath("//*[@id='sdBody']/div[1]/table/tbody/tr[2]/td/form/div/div/div/div/div[10]/div/select[1]"),  UILayer.startYearComboBox.getSelectedItem().toString());
			webSrcapper.SelectDropDown(By.xpath("//*[@id='sdBody']/div[1]/table/tbody/tr[2]/td/form/div/div/div/div/div[10]/div/select[2]"),  UILayer.endYearComboBox.getSelectedItem().toString());
		}
		
		webSrcapper.Click(By.xpath("//*[@id='sdBody']/div[1]/table/tbody/tr[2]/td/form/div/div/div/div/div[11]/div/input"));
		
		webSrcapper.getTextData_SD();
		webSrcapper.saveScreenshot(OutputScienceDirect.replace("html", "png"));

	}
	
	//------------IOS PRESS------------------
	public static void IOS_PRESSdriver(Scrapper webSrcapper, String qry) throws IOException, InterruptedException 
	{	
		if(UILayer.checkBoxYearsFilter.isSelected())
		{
			webSrcapper.openSite("http://iospress.metapress.com/content?k="+qry+"&"+"db="+UILayer.startYearComboBox.getSelectedItem().toString()+"0101&de="+UILayer.endYearComboBox.getSelectedItem().toString()+"0101");		
		}	
		else
		{	
			webSrcapper.openSite("http://iospress.metapress.com/content/?k="+qry);
		}
		
		//http://iospress.metapress.com/content?k=page+rank&db=20110101&de=20150101
		
		
		
		
		//webSrcapper.SetEditText(By.id("ctl00_PageSidebar_ctl00_Sidebarplaceholder_ctl00_FindTextBox_MainTextBox"), qry);
		//webSrcapper.Click(By.id("ctl00_PageSidebar_ctl00_Sidebarplaceholder_ctl00_FindTextBox_GoButton"));
		
		//Optional Filters
		
//		webSrcapper.ExplicitWait(5);
//		webSrcapper.WaitClickableButton(By.xpath("//*[@id='aspnetForm']/table/tbody/tr[1]/td[2]/table[2]/tbody/tr/td[2]/div[8]/ul/li[2]/a"));
//		webSrcapper.Click(By.xpath("//*[@id='aspnetForm']/table/tbody/tr[1]/td[2]/table[2]/tbody/tr/td[2]/div[8]/ul/li[2]/a"));
//		
					
		webSrcapper.getTextData_IOS();
		webSrcapper.saveScreenshot(OutputIOSPress.replace("html", "png"));
	}
	
	//------------Scopus------------------
	
	public static void SCOPUSdriver(Scrapper webScrapper,String qry)
	{
		webScrapper.openSite("http://www.elsevier.com/advanced-search");
		
		webScrapper.SetEditText(By.xpath("//*[@id='columns-layout']/div[1]/div/form/div[1]/fieldset[1]/input[1]"), qry);
		
		
		if(UILayer.checkBoxYearsFilter.isSelected())
		{
		
			//Optional Filters
			webScrapper.SelectDropDown(By.xpath("//*[@id='columns-layout']/div[1]/div/form/div[1]/fieldset[3]/div[1]/select"), UILayer.startYearComboBox.getSelectedItem().toString());
			webScrapper.SelectDropDown(By.xpath("//*[@id='columns-layout']/div[1]/div/form/div[1]/fieldset[3]/div[2]/select"), UILayer.endYearComboBox.getSelectedItem().toString());
		}
		
		/*
		 * SCOPUS.COM requires login...
		 * elsevier.com not giving desired results..
		 * */
		
		
//		webScrapper.Click(By.xpath("//*[@id='item-22']"));
//		
//		webScrapper.Click(By.id("item-22"));
	}
	
	
	
	//------------Springer------------------
	public static void SPRINGERdriver(Scrapper webSrcapper, String qry) throws IOException 
	{

		//		webSrcapper.openSite("http://link.springer.com/");
		//		webSrcapper.SetEditText(By.id("query"), qry);
		//		webSrcapper.Click(By.id("search"));
			
		webSrcapper.openSite("http://link.springer.com/search?query="+qry);
		webSrcapper.getTextData_SPRINGER();
		WaitClickableButton(By.xpath("//*[@id='results-list']/li"));
		webSrcapper.saveScreenshot(OutputSpringer.replace("html", "png"));

	}
	
	
	
	//------------CITESEERX------------------
		public static void CITESEERXdriver(Scrapper webSrcapper, String qry) throws IOException 
		{
			
			
			//http://citeseerx.ist.psu.edu/search?q=text%3A%28graph+algorithm%29+AND+year%3A%5B2011+TO+2015%5D&sort=cite&t=doc

			webSrcapper.openSite("http://citeseerx.ist.psu.edu/");
			webSrcapper.SetEditText(By.xpath("//*[@id='search_docs']/form/div[1]/input[1]"), qry);
			webSrcapper.Click(By.xpath("//*[@id='search_docs']/form/div[1]/input[2]"));
			
			
			//webSrcapper.openSite("http://link.springer.com/search?query="+qry);
			
			webSrcapper.getTextData_CiteSeerX();
			WaitClickableButton(By.xpath("//*[@id='result_list']/div"));
			webSrcapper.saveScreenshot(OutputCiteSeerX.replace("html", "png"));

		}
		
	//------------Inderscience------------------
		public static void INDERSCIENCEdriver(Scrapper webSrcapper, String qry) 
		{

			webSrcapper.openSite("http://www.inderscience.com/");
			
			webSrcapper.ReplaceEditText(By.xpath("//*[@id='nav']/form/input[1]"), qry);
			webSrcapper.Click(By.xpath("//*[@id='nav']/form/input[6]"));
			
			try 
			{
				webSrcapper.getTextData_Inderscience();
			}
			catch (InterruptedException e) 
			{
				UILayer.statusText.setText("Internet Connection Timed Out. Check yout Internet");
			
				
			} catch (IOException e) {
				
				UILayer.statusText.setText("Internet Connection Timed Out. Check yout Internet");
			}
			
			WaitClickableButton(By.xpath("//*[@id='main']/div/table"));
			try {
				
				webSrcapper.saveScreenshot(OutputInderScience.replace("html", "png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				UILayer.statusText.setText("ScreenShot Not Saved");
			}

		}

		
//------------Wiley------------------
		public static void WILEYdriver(Scrapper webSrcapper, String qry) throws IOException, InterruptedException 
		{
			webSrcapper.openSite("http://onlinelibrary.wiley.com/advanced/search");
			System.out.println("1");
			webSrcapper.SetEditText(By.id("searchRowCriteria[0].queryString"), qry);
			System.out.println("2");
			
//			webSrcapper.Click(By.id("between"));
			
//			webSrcapper.SetEditText(By.id("startYear"), UILayer.startYearComboBox.getSelectedItem().toString());
			
//			webSrcapper.SetEditText(By.id("endYear"), UILayer.startYearComboBox.getSelectedItem().toString());
			
			webSrcapper.Click(By.xpath("//*[@id='advancedSearch']/form/div/input"));
			
			
			webSrcapper.getTextData_Wiley();
			WaitClickableButton(By.xpath("//*[@id='searchResults']/form"));
			webSrcapper.saveScreenshot(OutputWiley.replace("html", "png"));
		}
		
	
	
	//======================= Main =================================
	public static void main1(String qry) throws IOException, InterruptedException 
	{
		//Browsers
		
		UILayer.statusText.setText("Initilizing Browser...");
		
		
		int Explorer = 1;
		int Firefox = 2;
		int Chrome = 3;

		//Initialization Settings
		ChromePATH = "chromedriver.exe";
		//Browser = Firefox;
                Browser = Firefox;
		//	PROXY = "192.168.1.10:8080";
		//enableProxy = true;		
		//String qry = "Page Rank";

		//Opening New Driver
		Scrapper webSrcapper = new Scrapper();
		webSrcapper.OpenDriver();
		
		UILayer.statusText.setText("Establishing Internet Connection...");
		
		if(UILayer.chckbxAcm.isSelected())
		{
			ACMdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		if(UILayer.chckbxIeee.isSelected())
		{
			IEEEdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		
		if(UILayer.chckbxScienceDirect.isSelected())
		{
			SCIENCE_DIRECTdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
	
		if(UILayer.chckbxIosPress.isSelected())
		{
			IOS_PRESSdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		if(UILayer.chckbxSpringer.isSelected())
		{
			SPRINGERdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		
		if(UILayer.chckbxSiteSeerX.isSelected())
		{	
			CITESEERXdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		
		if(UILayer.chckbxInderScience.isSelected())
		{	
			INDERSCIENCEdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		if(UILayer.chckbxWiley.isSelected())
		{
			WILEYdriver(webSrcapper, qry);
		//	webSrcapper.closeBrowser();
		}
		
		webSrcapper.closeBrowser();
		UILayer.statusText.setText("Completed");
			
	}
}