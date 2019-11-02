package com.test.automation.Listeners;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.test.automation.TestBase.BaseClass;

public class MyListeners extends BaseClass implements IRetryAnalyzer, IAnnotationTransformer, ITestListener
{
	int Counter = 0, retry_limit = 2;
	
	@Override
	public void onTestStart(ITestResult result) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestSuccess(ITestResult result) 
	{
		try 
		{
			Object current_instance = result.getInstance();
			driver = ((BaseClass)current_instance).getdriver();
			log.info("<<<<<<<<<<<<<<<Capturing Screenshot and creating a folder name: Passed_Test_Cases >>>>>>>>>>>>>>>>>>");
			String path = capture_screenshot("Passed_Test_Cases", result.getName());
			test = reports.createTest(result.getName());
			log.info("<<<<<<<<<<<<<<<Creating label name and changing the color to Green>>>>>>>>>>>>>>>>>>");
			test.pass(MarkupHelper.createLabel(result.getName(), ExtentColor.GREEN));
			log.info("<<<<<<<<<<<<<<<Integrating the image in extent report by capturing image from Source path>>>>>>>>>>>>>>>>>>");
			test.pass(result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTestFailure(ITestResult result) 
	{
		try 
		{
			Object current_instance = result.getInstance();
			driver = ((BaseClass)current_instance).getdriver();
			log.info("<<<<<<<<<<<<<<<Capturing Screenshot and creating a folder name: Failed_Test_Cases >>>>>>>>>>>>>>>>>>");
			String path = capture_screenshot("Failed_Test_Cases", result.getName());
			test = reports.createTest(result.getName());
			log.info("<<<<<<<<<<<<<<<Creating label name and changing the color to Red>>>>>>>>>>>>>>>>>>");
			test.fail(MarkupHelper.createLabel(result.getThrowable().getMessage(), ExtentColor.RED));
			log.info("<<<<<<<<<<<<<<<Integrating the image in extent report by capturing image from Source path>>>>>>>>>>>>>>>>>>");
			test.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onTestSkipped(ITestResult result) 
	{
		try 
		{
			Object current_instance = result.getInstance();
			driver = ((BaseClass)current_instance).getdriver();
			log.info("<<<<<<<<<<<<<<<Capturing Screenshot and creating a folder name: Skipped_Test_Cases >>>>>>>>>>>>>>>>>>");
			String path = capture_screenshot("Skipped_Test_Cases", result.getName());
			test = reports.createTest(result.getName());
			log.info("<<<<<<<<<<<<<<<Creating label name and changing the color to Blue>>>>>>>>>>>>>>>>>>");
			test.skip(MarkupHelper.createLabel(result.getThrowable().getMessage(), ExtentColor.BLUE));
			log.info("<<<<<<<<<<<<<<<Integrating the image in extent report by capturing image from Source path>>>>>>>>>>>>>>>>>>");
			test.skip(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) 
	{
		annotation.setRetryAnalyzer(com.test.automation.Listeners.MyListeners.class);
	}

	@Override
	public boolean retry(ITestResult result) 
	{
		if(Counter < retry_limit)
		{
			Counter++;
			return true;
		}
		else
		{
			Counter = 0;
			return false;
		}
	}
}
