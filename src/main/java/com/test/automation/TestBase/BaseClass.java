package com.test.automation.TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.test.automation.Utility.Util;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass 
{
	public Properties pro;
	public WebDriver driver;
	public static ExtentHtmlReporter reporter;
	public static ExtentReports reports;
	public static ExtentTest test;
	public Logger log;
	
	public BaseClass()
	{
		try 
		{
			log = Logger.getLogger("BaseClass");
			PropertyConfigurator.configure("./src/main/java/com/test/automation/Configuration/Config.Properties");
			File f = new File("./src/main/java/com/test/automation/Configuration/Config.Properties");
			log.info("<<<<<<<<<Reading the Property file>>>>>>>>>>>");
			FileInputStream FIS = new FileInputStream(f);
			pro = new Properties();
			log.info("<<<<<<<<<Loading the Property file>>>>>>>>>>>");
			pro.load(FIS);
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		 catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public WebDriver getdriver()
	{
		log.info("<<<<<<<<<Returning the driver instance>>>>>>>>>>>");
		return driver;
	}
	
	@BeforeSuite
	public void report_generation() throws Exception
	{
		String report_path = pro.getProperty("Report_Path")+date_time_stamp()+".html";
		File f = new File(report_path);
		reporter = new ExtentHtmlReporter(f);
		log.info("<<<<<<<<<Creating an object of Extent Reporter>>>>>>>>>>>");
		reports = new ExtentReports();
		log.info("<<<<<<<<<Creating an object of Extent Reports>>>>>>>>>>>");
		reports.attachReporter(reporter);
		log.info("<<<<<<<<<Attaching the reporter>>>>>>>>>>>");
	}
	
	@AfterSuite
	public void flush_report()
	{
		log.info("<<<<<<<<<Destroying the report instance>>>>>>>>>>>");
		reports.flush();
	}
	
	public void Launch_Browser(String browserName, String URL)
	{
		if(browserName.equalsIgnoreCase("Chrome"))
		{
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments("disable-notifications");
			opt.addArguments("incognito");
			opt.addArguments("start-maximized");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(opt);
		}
		else if(browserName.equalsIgnoreCase("Firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		log.info("Entering the URL");
		driver.get(URL);
		driver.manage().timeouts().pageLoadTimeout(Util.pageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Util.implicitlyWait, TimeUnit.SECONDS);
	}
	
	public void drop_down_selection(WebElement ele, String text)
	{
		Select select = new Select(ele);
		log.info("<<<<Selecting value from Drop Down>>>>>>>");
		select.selectByVisibleText(text);
	}
	
	public void handling_alert()
	{
		Alert alert = driver.switchTo().alert();
		log.info("<<<<Clicking Ok button from Alert window>>>>>>>");
		alert.accept();
	}
	
	public void verify_error_message(String xpathExpression, String expected_message)
	{
		String actual_message = driver.findElement(By.xpath(xpathExpression)).getText();
		log.info("<<<<Getting actual message from >>>>>>>"+xpathExpression);
		Assert.assertEquals(actual_message, expected_message);
		log.info("<<<<<<<<<<<Verifying the actual message with expected message>>>>>>>>>>>>>>>");
	}
	
	public void input(WebElement ele, String keysToSend)
	{
		log.info("<<<<<<<<<<<Entering the value in the text field>>>>>>>>>>>>>>>");
		ele.sendKeys(keysToSend);
	}
	
	public void click(WebElement ele)
	{
		log.info("<<<<<<<<<<<Clicking the button>>>>>>>>>>>>>>>");
		ele.click();
	}
	
	public Object[][] read_excel(String Sheet_Name) throws Exception
	{
		String path = pro.getProperty("Test_Data_Path");
		File f = new File(path);
		FileInputStream FIS = new FileInputStream(f);
		XSSFWorkbook wb = new XSSFWorkbook(FIS);
		XSSFSheet sheet = wb.getSheet(Sheet_Name);
		int rows = sheet.getLastRowNum();
		int columns = sheet.getRow(0).getLastCellNum();
		Object data[][] = new Object[rows][columns - 1];
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns-1; j++)
			{
				data[i][j] = sheet.getRow(i + 1).getCell(j + 1).toString();
			}
		}
		wb.close();
		return data;
	}
	
	public String date_time_stamp()
	{
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss a");
		String date = SDF.format(new Date());
		return date;
	}
	
	public String capture_screenshot(String Sub_Folder_Name, String image_name) throws IOException
	{
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir")+ "/Screenshot/" + Sub_Folder_Name + "/" + image_name +"_"+ date_time_stamp() +".png";
		File dest = new File(destination);
		FileUtils.copyFile(src, dest);
		return destination;
	}
}

