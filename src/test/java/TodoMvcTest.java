import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TodoMvcTest {
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void completesTask() {
        page.navigate("https://todomvc.com/examples/emberjs/");

        page.locator("#new-todo").fill("A");
        page.locator("#new-todo").press("Enter");
        page.locator("#new-todo").fill("B");
        page.locator("#new-todo").press("Enter");
        page.locator("#new-todo").fill("C");
        page.locator("#new-todo").press("Enter");
        assertThat(page.locator("#todo-list li")).hasText(new String[]{"A", "B", "C"});

        page.locator("#todo-list li:nth-of-type(2) .toggle").click();
        assertThat(page.locator("#todo-list li.completed")).hasText("B");
        assertThat(page.locator("#todo-list li:not(.completed)")).hasText(new String[]{"A", "C"});
    }
}
