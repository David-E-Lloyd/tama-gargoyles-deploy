package com.example.tama_gargoyles;

import com.example.tama_gargoyles.repository.GargoyleRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedNPlayFeatureTest {

    private WebDriver driver;

    @Autowired
    GargoyleRepository gargoyleRepository;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void feedingGargoyleIncreasesHunger() {
        driver.get("http://localhost:8080/game");

        WebElement hungerSpan = driver.findElement(By.xpath("//strong[text()='Hunger:']/following-sibling::span"));
        int hungerBefore = Integer.parseInt(hungerSpan.getText());

        WebElement feedButton = driver.findElement(By.xpath("//button[text()='Feed']"));
        feedButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.getCurrentUrl().endsWith("/game"));

        WebElement hungerAfterSpan = driver.findElement(By.xpath("//strong[text()='Hunger:']/following-sibling::span"));
        int hungerAfter = Integer.parseInt(hungerAfterSpan.getText());

        assertThat(hungerAfter)
                .isGreaterThanOrEqualTo(hungerBefore)
                .isLessThanOrEqualTo(100);
    }

    @Test
    void playingGargoyleIncreasesHappiness() {
        driver.get("http://localhost:8080/game");

        WebElement happinessSpan = driver.findElement(By.xpath("//strong[text()='Happiness:']/following-sibling::span"));
        int happinessBefore = Integer.parseInt(happinessSpan.getText());

        WebElement playButton = driver.findElement(By.xpath("//button[text()='Play']"));
        playButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.getCurrentUrl().endsWith("/game"));

        WebElement happinessAfterSpan = driver.findElement(By.xpath("//strong[text()='Happiness:']/following-sibling::span"));
        int happinessAfter = Integer.parseInt(happinessAfterSpan.getText());

        assertThat(happinessAfter)
                .isGreaterThanOrEqualTo(happinessBefore)
                .isLessThanOrEqualTo(100);
    }
}
