package org.defendev.selenium.demo;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



public class SeleniumSetupPocTest {

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void openApplicationAndInterceptRequests(){

        System.setProperty("webdriver.chrome.driver", "C:/java_installs/chromedriver-win64/chromedriver.exe");
        final WebDriver driver = new ChromeDriver();

        final DevToolsRequestHeaderExtractor headerExtractor = DevToolsRequestHeaderExtractor.enableExtraction(driver);

        /*
         * Global, per-driver default wait, called an implicit wait.
         * It affects all subsequent findElement / findElements calls.
         */
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        /*
         * The WebDriverWait object is reusable but 100% thread-safe. Used for making explicit waits.
         * Explicit wait is created by making a call to wait.until().
         */
        final WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8011/webcntx/confidential-spa/");

        wait.until(ExpectedConditions.urlContains("/webcntx/login"));

        /*
         * In XPath the // (double backslash) means "search recursively through all descendants starting
         * from this point.
         *
         * Also:
         *  "//a[contains(., 'sprin6authz')]"       <- search for <a> element
         *  "//*[contains(., 'sprin6authz')]"       <- search for any element
         *
         */
        final WebElement sprin6authzLoginLink = driver.findElement(By.xpath("//a[contains(., 'sprin6authz')]"));
        sprin6authzLoginLink.click();

        wait.until(ExpectedConditions.urlContains("localhost:8010/defendev-authz/sign-in"));

        final WebElement mockUserButton = driver.findElement(By.xpath("//button[@data-username='isaac_newton']"));
        mockUserButton.click();

        wait.until(ExpectedConditions.urlContains("webcntx/confidential-spa/"));

        final WebElement makeXhrCallButton = driver.findElement(By.xpath("//button[contains(., 'Make XHR call')]"));
        final WebElement makeXhrCallStatus = driver.findElement(By.xpath("//span[@id='shallow-xhr-status']"));
        final WebElement makeFetchCallButton = driver.findElement(By.xpath("//button[contains(., 'Make Fetch call')]"));
        final WebElement makeFetchCallStatus = driver.findElement(By.xpath("//span[@id='shallow-fetch-status']"));

        makeXhrCallButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(makeXhrCallStatus, "ranodm int is"));

        makeFetchCallButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(makeFetchCallStatus, "ranodm int is"));

        final Optional<String> cookieHeaderOptional = headerExtractor.extractedCookieHeader();

        assertThat(cookieHeaderOptional).isPresent();
        final String cookieHeader = cookieHeaderOptional.get();
        assertThat(cookieHeader).isNotEmpty().contains("RSESSION=");

        /*
           RSESSION=cf93e60d-9f97-450d-8235-c2594a527787
        */
        System.out.println("------- " + cookieHeader);
    }


}
