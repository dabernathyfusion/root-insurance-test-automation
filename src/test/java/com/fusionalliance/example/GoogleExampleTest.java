package com.fusionalliance.example;

import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.selenium.Eyes;
import com.fusionalliance.app.pages.examples.GoogleMainPage;
import com.fusionalliance.base.BaseTest;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Tyler Von Moll on 7/27/16.
 *
 * <p>Description: A couple of examples for Chrome.
 */
public class GoogleExampleTest extends BaseTest {

  private GoogleMainPage googleMainPage;

  @Before
  public void setUp() {
    super.setUp();
    googleMainPage = new GoogleMainPage(currentTest.getWebDriverSession().getDriver(), homePageUrl);
    googleMainPage.get();
  }

  @Features("GOOG-1001")
  @Stories("GOOG-1001-F001")
  @Test
  public void whenNavToHomepage_thenSearchBarIsVisible() {
    requirementsCoverage.writeToFile("GOOG-1001-F001 -- search bar is visible on homepage");

    Eyes eyes = eyesService.openEyes(currentTest, MatchLevel.LAYOUT2);
    eyes.checkWindow("Google Main Page");
    eyes.close();

    assertThat(googleMainPage.isSearchInputVisible()).as("Check search input visibility").isTrue();
  }
}