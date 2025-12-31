package com.orangehrm.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener  implements ITestListener {

	// Triggered when a test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		// Start logging in Extent Reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started: " + testName);
	}

	// Triggered when a Test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
					"Test End: " + testName + " - ✔ Test Passed");
		} else {
			ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
		}}

	// Triggered when a Test Fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End: " + testName + " - ❌ Test Failed");
		}
		else {
			ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
		}}

	// Triggered when a Test skips
		@Override
		public void onTestSkipped(ITestResult result) {
			String testName = result.getMethod().getMethodName();
			ExtentManager.logSkip("Test Skipped " + testName);
		}

	//Trigger when suite starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the extent reports
		ExtentManager.getReporter();
	}


	//Trigger when suite ends
	@Override
	public void onFinish(ITestContext context) {
		// flush the extent reports
		ExtentManager.endTest();

	}

	
}
