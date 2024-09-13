import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmazonSoundbarScraper {

    public static void main(String[] args) {
        // Path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // Update with your path

        // Set up Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open Amazon India
            driver.get("https://www.amazon.in");

            // Search for 'lg soundbar'
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("lg soundbar");
            searchBox.submit();

            // Wait for the results to load
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));

            // Initialize a map to store product names and prices
            Map<String, Double> products = new HashMap<>();

            // Locate product elements
            List<WebElement> productElements = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));

            for (WebElement product : productElements) {
                try {
                    String name = product.findElement(By.xpath(".//h2//span")).getText();
                    String priceWhole = product.findElement(By.xpath(".//span[@class='a-price-whole']")).getText();
                    String priceFraction = product.findElement(By.xpath(".//span[@class='a-price-fraction']")).getText();
                    double price = Double.parseDouble(priceWhole + "." + priceFraction);
                    products.put(name, price);
                } catch (Exception e) {
                    // If price is not present, set it to 0
                    String name = product.findElement(By.xpath(".//h2//span")).getText();
                    products.put(name, 0.0);
                }
            }

            // Sort products by price
            List<Map.Entry<String, Double>> sortedProducts = products.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toList());

            // Print the sorted products
            for (Map.Entry<String, Double> entry : sortedProducts) {
                System.out.println(entry.getValue() + " " + entry.getKey());
            }

        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}
