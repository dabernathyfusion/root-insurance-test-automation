package com.fusionalliance.example;

import com.fusionalliance.base.BaseTest;
import com.fusionalliance.test.context.junit.rules.ignore.browser.IgnoreDesktopBrowsers;
import com.fusionalliance.test.context.junit.rules.ignore.browser.IgnoreMobileBrowsers;
import org.junit.Test;

public class GoogleExampleIgnoredTest extends BaseTest {

  @IgnoreDesktopBrowsers
  @IgnoreMobileBrowsers
  @Test
  public void whenNavToHomepage_thenSomethingElseIsExpected() {
    throw new AssertionError("This test should fail if it isn't ignored; here for demo purposes");
  }
}