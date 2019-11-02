package com.test.automation.TestCases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.test.automation.Pages.LoginPage;
import com.test.automation.TestBase.BaseClass;

public class LoginPage_TC extends BaseClass
{
	LoginPage LP; 
	
	@BeforeMethod
	@Parameters({"browser","url"})
	public void browser_init(String browserName, String URL)
	{
		Launch_Browser(browserName, URL);
	}
	
	@DataProvider
	public Object[][] getData() throws Exception
	{
		Object data[][] = read_excel("Login_Data");
		return data;
	}
	
	@Test(dataProvider = "getData")
	public void test_login_to_app(String Shift_Name, String	UserName, String Password, String XPATH, String	Expected_Message)
	{
		LP = new LoginPage(driver);
		LP.login_to_app(Shift_Name, UserName, Password, XPATH, Expected_Message);
	}
	
	@AfterMethod
	public void tear_down()
	{
		driver.quit();
	}
}
