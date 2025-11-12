package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class ContactInformationPageTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setupTest() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
        page.navigate("https://depaul.bncollege.com/");
        page.setDefaultTimeout(60000);
    }

    @Test
    void testContactInformationPage() {
        page.waitForSelector("text=Help");
        page.getByText("Help").click();
        page.waitForSelector("text=Contact Us");
        page.getByText("Contact Us").click();

        page.waitForTimeout(4000);
        assertTrue(page.content().toLowerCase().contains("contact"), "Should show contact info");
    }

    @AfterEach
    void teardown() { context.close(); }
    @AfterAll
    static void cleanup() { browser.close(); playwright.close(); }
}


