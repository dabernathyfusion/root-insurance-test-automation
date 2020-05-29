package com.fusionalliance.app.pages.examples;

import com.fusionalliance.page.object.BasePage;
import com.fusionalliance.waitmon.WaitBuilder;
import java.time.temporal.ChronoUnit;
import io.appium.java_client.pagefactory.WithTimeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Tyler Von Moll on 7/27/16.
 *
 * <p>Description: Example page obj for Google's homepage. Note that actions are annotated
 * with @Step.
 */
public class GoogleMainPage extends BasePage {

  private final String expectedPageTitle = "Google";
  private final String homePageUrl;

  @FindBy(id = "lst-ib")
  @WithTimeout(time = 60, chronoUnit = ChronoUnit.SECONDS)
  private WebElement searchField; // @WithTimeout is an optional override of the default timeout.

  @FindBy(linkText = "Sign in")
  private WebElement accountSignInBtn;

  public GoogleMainPage(WebDriver driver, String homePageUrl) {
    super(driver, 30);
    this.homePageUrl = homePageUrl;
  }

  @Override
  protected void load() {
    driver.get(homePageUrl);
    new WaitBuilder(driver).waitUpTo(30).forPageTitleToBe(expectedPageTitle);
  }

  @Override
  protected void isLoaded() throws Error {
    assertThat(driver.getTitle()).isEqualTo(expectedPageTitle);
  }

  @Step
  public void enterSearchTerm(String searchTerm) {
    searchField.sendKeys(searchTerm);
  }

  @Step
  public void clickSignIn() {
    accountSignInBtn.click();
  }

  public boolean isSearchInputVisible() {
    return searchField.isDisplayed();
  }
}
