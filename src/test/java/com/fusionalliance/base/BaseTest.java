package com.fusionalliance.base;

import com.fusionalliance.properties.UrlsProvider;
import com.fusionalliance.properties.data.BrowserOrDevice;
import com.fusionalliance.properties.data.FullProperties;
import com.fusionalliance.reporting.sauce.SauceRestService;
import com.fusionalliance.requirements_coverage.RequirementsCoverage;
import com.fusionalliance.test.context.eyes.EyesService;
import com.fusionalliance.test.context.full.FullTestContext;
import com.fusionalliance.test.context.junit.rules.SauceTestWatcher;
import com.fusionalliance.test.context.junit.rules.ignore.browser.BrowserIgnoreRule;
import com.fusionalliance.test.runnable.RunnableTest;
import com.fusionalliance.test.runnable.TestService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Tyler Von Moll on 5/15/17.
 */
@RunWith(Parameterized.class)
public abstract class BaseTest {

  private static ApplicationContext applicationContext =
      new AnnotationConfigApplicationContext(FullTestContext.class);
  private static FullProperties fullProperties =
      applicationContext.getBean("fullProperties", FullProperties.class);

  @Parameterized.Parameters(name = "{index}: {0}")
  public static Collection generateParameters() {
    return fullProperties.getBrowsers();
  }

  @Parameterized.Parameter() public BrowserOrDevice browserOrDevice;

  @Rule public TestName testName = new TestName();

  @Rule public SauceTestWatcher sauceTestWatcher = new SauceTestWatcher();

  @Rule public BrowserIgnoreRule browserIgnoreRule = new BrowserIgnoreRule();

  protected final EyesService eyesService = applicationContext.getBean(EyesService.class);
  protected RunnableTest currentTest;
  protected String homePageUrl;
  protected RequirementsCoverage requirementsCoverage = new RequirementsCoverage();
  private final String testId = UUID.randomUUID().toString();
  private final TestService testService = applicationContext.getBean(TestService.class);

  @Before
  public void setUp() {
    currentTest = testService.startTest(testId, testName.getMethodName(), browserOrDevice);
    sauceTestWatcher.setSauceRestService(applicationContext.getBean(SauceRestService.class));
    sauceTestWatcher.setTest(currentTest);
    homePageUrl = applicationContext.getBean(UrlsProvider.class).getHomePageUrl();
  }

  @After
  public void tearDown() {
    eyesService.cleanUp(currentTest);
    testService.finishTest(testId);
  }
}