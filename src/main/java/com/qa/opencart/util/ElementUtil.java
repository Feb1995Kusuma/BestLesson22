package com.qa.opencart.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.Factory.DriverFactory;



public class ElementUtil {
	
	private static final String urlFraction = null;
	private static final Duration timeOut = null;
	private WebDriver driver;
	private Duration i;
	private String title;
	private JavaScript jsUtil; 

	public ElementUtil(WebDriver driver)
	{
		this.driver = driver;
		jsUtil = new JavaScript(driver);
	}

	public By getBy(String locatorType, String locatorValue) {
		By locator = null;
		switch (locatorType) {
		case "id":
			locator = By.id(locatorValue);
			break;

		case "name":
			locator = By.name(locatorValue);
			break;

		case "className":
			locator = By.className(locatorValue);
			break;

		case "linkText":
			locator = By.linkText(locatorValue);
			break;

		case "partialLinkText":
			locator = By.partialLinkText(locatorValue);
			break;

		case "xpath":
			locator = By.xpath(locatorValue);
			break;

		case "cssSelector":
			locator = By.cssSelector(locatorValue);
			break;

		case "tagName":
			locator = By.tagName(locatorValue);
			break;

		default:
			System.out.println("Please pass the correct locator " + locatorType);
			break;
		}

		return locator;
	}

	public WebElement getElement(By locator) {
		WebElement element = driver.findElement(locator);
		if(Boolean.parseBoolean(DriverFactory.highlight)) {
			jsUtil.flash(element);
		}
		
		return element;
	}

	public WebElement getElement(String locatorType, String locatorValue) {
		return driver.findElement(getBy(locatorType, locatorValue));
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}
	
	public void doClear(By locator) {
		getElement(locator).clear();
	}

	public void doSendKeys(By locator, String firstName) {
		doClear(locator);
		getElement(locator).sendKeys(firstName);
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}

	public String doGetText(By locator) {
		return getElement(locator).getText();
	}

	public String doGetAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}

	public boolean doIsDisplayed(By locator) {
		return getElement(locator).isDisplayed();
	}

	public boolean doIsEnabled(By locator) {
		return getElement(locator).isEnabled();
	}

	public void clickOnElement(By locator, String value) {
		List<WebElement> eleList = getElements(locator);
		System.out.println(eleList.size());
		for (WebElement e : eleList) {
			if (e.getText().equals(value)) {
				e.click();
				break;
			}
		}
	}

	public List<String> getLinksTextList(By locator) {
		List<String> eleTextList = new ArrayList<String>();

		List<WebElement> eleList = getElements(locator);
		System.out.println("element count: " + eleList.size());

		for (WebElement e : eleList) {
			String text = e.getText();
			if (!text.isEmpty()) {
				eleTextList.add(text);
			}
		}
		return eleTextList;
	}

	public boolean isElementExist(By locator) {
		List<WebElement> listEle = getElements(locator);
		if (listEle.size() > 0) {
			System.out.println("element is present");
			return true;
		}
		System.out.println("element is not present");
		return false;
	}

	/************************ Drop Down Utils (Select tag) ***********************/

	public void doSelectByVisibleText(By locator, String text) {
		Select select = new Select(getElement(locator));
		select.selectByVisibleText(text);
	}

	public void doSelectByValue(By locator, String value) {
		Select select = new Select(getElement(locator));
		select.selectByValue(value);
	}

	public void doSelectByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public List<String> getDropDownOptionsList(By locator) {
		List<String> optionsValList = new ArrayList<String>();
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList = select.getOptions();

		System.out.println(optionsList.size());

		for (WebElement e : optionsList) {
			String text = e.getText();
			optionsValList.add(text);
		}
		return optionsValList;
	}

	public void selectDropDownValue(By locator, String value) {
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList = select.getOptions();
		System.out.println(optionsList.size());

		for (WebElement e : optionsList) {
			String text = e.getText();
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

	public void clickDropDownValue(By locator, String value) {
		List<WebElement> optionsList = getElements(locator);
		System.out.println(optionsList.size());
		for (WebElement e : optionsList) {
			String text = e.getText();
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

	/************************* User Actions Utils *************************/

	public void twoLevelMenuHandle(By parentMenu, By childMenu) throws InterruptedException {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(parentMenu)).perform();
		Thread.sleep(2000);
		getElement(childMenu).click();
	}

	public void threeLevelMenuHandle(By parentMenu1, By parentMenu2, By childMenu) throws InterruptedException {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(parentMenu1)).perform();
		Thread.sleep(2000);
		act.moveToElement(getElement(parentMenu2)).perform();
		Thread.sleep(2000);
		getElement(childMenu).click();
	}

	public void doActionsSendKeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(getElement(locator), value).perform();
	}

	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(getElement(locator)).perform();
	}

	/*************************** Wait Utils *****************************/

	public Alert waitForAlert(Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public String getAlertText(int timeOut) {
		String text = waitForAlert(timeOut).getText();
		acceptAlert(timeOut);
		return text;
	}

	private Alert waitForAlert(int timeOut) {
		// TODO Auto-generated method stub
		return null;
	}

	public void acceptAlert(int timeOut) {
		waitForAlert(timeOut).accept();
	}

	public void dismissAlert(int timeOut) {
		waitForAlert(timeOut).dismiss();
	}

	public void sendKeysOnAlert(int timeOut, String value) {
		waitForAlert(timeOut).sendKeys(value);
	}

	public String waitForTitleContains(String titleValue, Duration defaultTimeOut) {
		WebDriverWait wait = new WebDriverWait(driver, defaultTimeOut);
		if (wait.until(ExpectedConditions.titleContains(titleValue))) {
			return driver.getTitle();
		}
		return null;
	}

	public String waitForTitleIs(String fullTitle, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		if (wait.until(ExpectedConditions.titleIs(fullTitle))) {
			return driver.getTitle();
		}
		return null;
	}
	
	public String waitForUrlContains(String urlFraction, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,timeOut);
		if(wait.until(ExpectedConditions.urlContains(urlFraction))) {
			return driver.getCurrentUrl();
		}
		return null;
	}
	
	public String waitForFullUrl(String urlValue, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		if(wait.until(ExpectedConditions.urlToBe(urlValue))) {
			return driver.getCurrentUrl();
		}
		return null;
	}
	
	
	public void waitForFrameAndSwitchToIt(String frameName, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}
	
	public void waitForFrameAndSwitchToIt(By frameLocator, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}
	
	public void waitForFrameAndSwitchToIt(int frameIndex, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	
	public void waitForFrameAndSwitchToIt(WebElement frameElement, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does not necessarily mean that the element is visible.
	 * 
	 * @param accountPageTitle
	 * @param defaultTimeOut
	 * @return
	 */
	public Boolean waitForElementPresence(By accountPageTitle, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.titleIs(title));
	}
	
	/**
	 * 
	 * @param locator
	 * @param timeOut
	 * @param intervalTime
	 * @return
	 */
	public WebElement waitForElementPresence1(By locator, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	public WebElement waitForElementWithFluentWait(By locator, int timeOut, long pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime))
				.ignoring(NoSuchElementException.class);
		
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	
	public WebDriver waitForFrameWithFluentWait(By locator, int timeOut, long pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime))
				.ignoring(NoSuchElementException.class);
		
		return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	public WebElement retryingElement(By locator, int maxAttempts) {
		WebElement element = null;
		int attempts = 0;
		while(attempts < maxAttempts) {
			try {
				element = driver.findElement(locator);
				break;
			}
			catch(Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("element is not found in attempt : " + (attempts+1));
			}
			
			
			attempts++;
		}
		return element;
	}

	public boolean doGetTitle(String loginPageTitle, int defaultTimeOut) {
		return true;
	}

	

	

	public String waitForTitleContains1(String titleValue, Duration timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		if (wait.until(ExpectedConditions.titleContains(titleValue))) {
			return driver.getTitle();
		}
		return null;	}

	public List<WebElement> waitForElementsToBeVisible(By locator, int timeOut) {
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
	return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	
	
	}

	public String waitForTitleIs1(String fullTitle) {
		WebDriverWait wait = new WebDriverWait(driver, i);
		if (wait.until(ExpectedConditions.titleIs(fullTitle))) {
			return driver.getTitle();
		}
		return null;
	}
	
	public String doGetText1(By locator) {
		return getElement(locator).getText();
	}

	public void doSendKeys(By locator,By firstName, String firstName1) {
		doClear(locator);
		getElement(locator).sendKeys(firstName1);
	}

	public void doSendKeys1(By searchfield, String productName) {
		// TODO Auto-generated method stub
		
	}

	public String waitForUrlContains1(String UrlFraction, int defaultTimeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		if(wait.until(ExpectedConditions.urlContains(UrlFraction))) {
			return driver.getCurrentUrl();
		}
		return null;
	}

	public WebElement waitForElementPresence2(By locator, int timeOut) {
		//WebDriverWait wait = new WebDriverWait(driver, timeOut);
		//WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebDriverWait some_element  = new WebDriverWait(driver, Duration.ofSeconds(100));
		return some_element .until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	

	
	


}

