package com.example.tama_gargoyles;

import com.example.tama_gargoyles.model.Gargoyle;
import com.example.tama_gargoyles.model.User;
import com.example.tama_gargoyles.repository.GargoyleRepository;
import com.example.tama_gargoyles.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RenameFeatureTest {

    private WebDriver driver;

    @Autowired
    GargoyleRepository gargoyleRepository;

    @Autowired
    UserRepository userRepository;

    private Gargoyle gargoyle;

    @BeforeAll
    void setupDriver() {
        driver = new ChromeDriver();
    }

    @BeforeEach
    void setupData() {
        // Create user
        User user = new User("test@example.com");
        user = userRepository.save(user);

        // Create gargoyle
        gargoyle = new Gargoyle();
        gargoyle.setName("OldName");
        gargoyle.setHunger(50);
        gargoyle.setHappiness(50);
        gargoyle.setUser(user);

        gargoyle = gargoyleRepository.save(gargoyle);
    }

    @AfterEach
    void cleanup() {
        gargoyleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    void teardown() {
        driver.quit();
    }

    @Test
    void renamingGargoyleUpdatesName() {
        // Go directly to rename page
        driver.get("http://localhost:8080/gargoyle/" + gargoyle.getId() + "/rename");

        // Find input field
        WebElement nameInput = driver.findElement(By.name("name"));
        nameInput.clear();
        nameInput.sendKeys("Sir Screams-a-Lot");

        // Submit form
        nameInput.submit();

        // Reload from DB
        Gargoyle updated = gargoyleRepository.findById(gargoyle.getId()).orElseThrow();

        // Assert name changed
        assertThat(updated.getName()).isEqualTo("Sir Screams-a-Lot");
    }
}
