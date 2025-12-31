package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitwait));
		logger.info("WebDriver instance is created");
	}

	// method to click element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			logger.info("clicked an element-->" + elementDescription);
		} catch (Exception e) {
			System.out.println("Unable to click Element: " + e.getMessage());
			logger.error("Unable to click an element");
		}
	}

	// method to enter text into a input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			logger.error("Unable to enter the value: " + e.getMessage());
		}
	}

	// Method to get text from an input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();

		} catch (Exception e) {
			logger.error("Unable to get the text: " + e.getMessage());
		}
		return " ";
	}

	// Method to compare two text
	public void compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				System.out.println("Text are Matching:" + actualText + " equals " + expectedText);
			} else {
				System.out.println("Text are not Matching:" + actualText + " not equals " + expectedText);
			}
		} catch (Exception e) {
			System.out.println("Unable to compare text: " + e.getMessage());
		}
	}

	// Method to check if element is displayed
	/*
	 * public boolean isDisplayed(By by) { try { waitForElementToBeVisible(by);
	 * boolean isDisplayed = driver.findElement(by).isDisplayed(); if (isDisplayed)
	 * { System.out.println("Element is visible"); return isDisplayed; } else {
	 * return isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not displayed: " + e.getMessage()); return
	 * false; } }
	 */
	
	
	// Simplified the method and remove redundant conditions
		public boolean isDisplayed(By by) {
			try {
				waitForElementToBeVisible(by);
//				applyBorder(by,"green");
				logger.info("Element is displayed " + getElementDescription(by));
				ExtentManager.logStep("Element is displayed: "+getElementDescription(by));
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
				return driver.findElement(by).isDisplayed();
			} catch (Exception e) {
//				applyBorder(by,"red");
				logger.error("Element is not displayed: " + e.getMessage());
				ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: ","Elemenet is not displayed: "+getElementDescription(by));
				return false;
			}
		}


	// simplified isDisplayed
//	public boolean isDisplayed(By by) {
//		try {
//			waitForElementToBeVisible(by);
////			logger.info("Element is displayed " + getElementDescription(by));
////			ExtentManager.logStep("Element is displayed: "+ getElementDescription(by));
////			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
////			return driver.findElement(by).isDisplayed();
//			logger.info("Element is displayed " + getElementDescription(by));
////			ExtentManager.logStep("Element is displayed: "+ getElementDescription(by));
////			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
//			return driver.findElement(by).isDisplayed();
//		} catch (Exception e) {
//			System.out.println("Element is not displayed: " + e.getMessage());
//			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: ","Elemenet is not displayed: "+getElementDescription(by));
//			return false;
//		}
//	}

	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
//				logger.info(""Page loaded successfully.");
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
//				logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
			System.out.println("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an element -- Added a semicolon ; at the end of the script string
	public void scrollToElement(By by) {
		try {
//				applyBorder(by,"green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
//				applyBorder(by,"red");
//				logger.error("Unable to locate element:" + e.getMessage());
			System.out.println("Unable to locate element:" + e.getMessage());
		}
	}

	// Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("Element is not clickable: " + e.getMessage());
		}
	}

	// Wait for Element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible:" + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid NullPointerException
		if (driver == null) {
			return "Driver is not initialized.";
		}
		if (locator == null) {
			return "Locator is null.";
		}

		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			// Get element attributes
			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			// Return a description based on available attributes
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			// Log exception for debugging
			e.printStackTrace(); // Replace with a logger in a real-world scenario
			return "Unable to describe element due to error: " + e.getMessage();
		}
	}

	// Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long strings
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}
}
