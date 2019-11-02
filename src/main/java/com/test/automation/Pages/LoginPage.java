package com.test.automation.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.test.automation.TestBase.BaseClass;

public class LoginPage extends BaseClass
{
	public LoginPage(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id = "ddlShift")WebElement shift_drop_down;
	@FindBy(name= "txtUserId")WebElement login_ID;
	@FindBy(xpath = "//input[@id='txtPassword']")WebElement password;
	@FindBy(id = "btnLogn")WebElement login_button;
	
	public void login_to_app(String Shift_Name, String	UserName, String Password, String XPATH, String	Expected_Message)
	{
		drop_down_selection(shift_drop_down,Shift_Name);
		input(login_ID,UserName);
		input(password,Password);
		click(login_button);
		if(Shift_Name.equalsIgnoreCase("Select")||UserName.equalsIgnoreCase("")||Password.equalsIgnoreCase(""))
		{
			handling_alert();
		}
		else
		{
			verify_error_message(XPATH, Expected_Message);
		}
	}
}
