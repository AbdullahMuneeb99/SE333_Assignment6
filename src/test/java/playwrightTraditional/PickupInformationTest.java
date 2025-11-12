package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class PickupInformationTest {
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
    void testPickupInformation() {
        page.waitForSelector("text=Store Information");
        page.getByText("Store Information").click();
        page.waitForSelector("text=Loop Campus");
        String content = page.content().toLowerCase();
        assertTrue(content.contains("loop") || content.contains("pickup"), "Pickup info visible");
    }

    @AfterEach
    void teardown() { context.close(); }
    @AfterAll
    static void cleanup() { browser.close(); playwright.close(); }
}


