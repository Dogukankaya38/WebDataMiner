package org.example.utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Utility {
   // public static final String BASE_URL = "https://google.com/";
    public static String BASE_URL;

    public static void clickByIdViaJS(WebDriver driver, String id) {
        WebElement element = driver.findElement(By.id(id));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }
}
