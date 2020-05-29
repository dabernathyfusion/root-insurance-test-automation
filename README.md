Root Insurance UI Automation
=======================

In _src/main/resources_, you should see a file named _config.xml_. This demonstrates most of the recognized properties that can be set.


A short name for the app and version being tested (e.g. Portfolio). This will assume the value that you entered at project creation for `project-name`.

* local
* browserstack (appropriate account is required)
* sauce (appropriate account is required)

* Usually test, stage, prod, etc. These can be custom names (so long as there is a matching tag in the `<urls>` section).

* See also environment above; these are usually test, stage, prod, etc. The example config demonstrates the typical way to use this. In some cases, you may need to define a `locale` (e.g., if you are dealing with an app that has both an EU and US site). 

* Note that when adding urls with locales, you'll want to bind both sets of urls to fields in `BaseTest`. Continuing in the example above, you would modify `.setUp()` to be:

    
    euHomePageUrl = applicationContext.getBean(UrlsProvider.class).getHomePageUrl("EU");
    usHomePageUrl = applicationContext.getBean(UrlsProvider.class).getHomePageUrl("US");
    
* Use this only when you are integrating with the API(s) of the app.
    * scheme - http or https
    * host-name - the root domain name for the api

* These two properties set the behavior of the screenshot and html attachments for the allure report. The default if not set is 'false' for both.

Any number of browser configurations can be specified here. The composition of these browser configs is as follows:

    <browser>
        <name> REQUIRED - safari, chrome, ie</name>
        <version> REQUIRED - browser version (if testing mobile web, this should be the os + os_version (e.g. iOS 10.2)) </version>
        <os> only needed for mobile web and desktop browsers on Browserstack and Sauce (defaults to any available) - Windows, OS X, iOS, Android </os>
        <os-version> only needed for mobile web and desktop browsers on Browserstack and Sauce (defaults to any available) - Mavericks, 10, etc. </os-version>
        <device> only needed if iPhone or android specified for name; note that mobile is only available on Browserstack </device>
    </browser>


* As of v2.8.8 of this project, you can take advantage of Chrome's mobile emulation feature. This opens the possibility of checking site responsiveness without the added complexity of proper mobile simulators and emulators. The option is `chromeMobileEmulation`, and the value is the name of the emulator from Chrome that  you would like to use. See [this issue](https://github.com/fusion-alliance/ui-automation-sdk/issues/163) for more details and additional links.

* If your run-location is local, then the `<version>`, `<os>`, and `<os-version>` properties are ignored for desktop browsers.
* If you have an Applitools Eyes account, you'll need to set your api key to the following environment variable on your local machine: `APPLITOOLS_KEY` (see [this support post](http://support.applitools.com/customer/en/portal/articles/2118694-the-runner-key-api-key-) for instructions on retrieving this key).
* To run Chrome, you'll need to set the environment variable `CHROMEDRIVER_HOME` on your local machine. This should point to the location of the chromedriver binary on your computer (this can be downloaded [from Google](https://sites.google.com/a/chromium.org/chromedriver/)).
* To run Firefox, you'll need to set the environment variable `GECKODRIVER_HOME` on your local machine. This should point to the location of the geckodriver binary on your computer (this can be downloaded [from Mozilla](https://github.com/mozilla/geckodriver/releases))
* In order to run mobile browsers locally, you will need to have Appium installed. The server will be started/stopped automatically for you. The `<device>` and `<platform>` properties will depend on your installed simulators/emulators. Real device testing is not yet supported.
    * As of Appium 1.7, the best practice is also to specify a `wdaLocalPort` for each mobile simulator/emulator. This is accomplished with an other option (YAML example):
    
    ```YAML
    otherOptions:
      - option:
        key: wdaLocalPort
        value: 8101
    ```
    * As of Appium 1.8, you can have multiple versions of chromedriver to handle the different versions of Chrome installed on Android emulators or devices. This is accomplished through `otherOptions` (example below). For more details, check out the [Appium Docs](http://appium.io/docs/en/writing-running-appium/web/chromedriver/). The naming convention for the different versions of chromedriver should be `chromedriver_MAJOR.MINOR`. If you have downloaded version 2.42, for example, the binary name in your chromedrivers directory should be `chromedriver_2.42`.
          
    ```YAML
      otherOptions:
      - key: chromedriverExecutableDir
        value: <absolute-path-to-directory-with-chromedriver-bins>
    ```
      

These are the browserstack-specific properties (including your own username and automate-key). These can be reviewed on [Browserstack's website](https://www.browserstack.com/automate/java). Note that the default vm 'resolution' capability is 1920x1080.

You'll need to set your username and access key with the following environment variables on your local machine:

`BROWSERSTACK_USER`
`BROWSERSTACK_ACCESSKEY`

These are the sauce-specific properties. These can be reviewed on Sauce's website in the ["Optional Sauce Testing Features" section](https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options).

You'll need to set your username and access key with the following environment variables on your local machine:

`SAUCE_USERNAME`
`SAUCE_ACCESS_KEY`

If using the sauce connect proxy with the default tunnel name, no additional setup is necessary. If you are using named tunnels, you will need to add `<TUNNEL_IDENTIFIER>` to the top level of the _config.xml_ file (or pass it in as a maven argument with `-DTUNNEL_IDENTIFIER="tunnel name here"`).

You'll find abstract base classes in both _src/main/app/pages_ and _src/test/app_. These should serve as the parent classes for your page objects and tests, respectively. There is an example page object (navigating to Google's homepage) and corresponding test that demonstrate how classes could be written.

There is a random String generator included in the `test.common` dependency. If you need a random alphanumeric String that is 10 characters long, for example, you would simply do the following:

`String randomString = RandomString.alphanumeric().generate(10);`

This scaffold assumes the page object model with Selenium's abstract `LoadableComponent` class. Whenever you create a new page object class and extend `BasePage`, IntelliJ will remind you (via a red underline) to implement the `load()` and `isLoaded()` methods in addition to creating a constructor.

When you inherit from `BasePage` and find elements using `@FindBy`, you are implicitly waiting for those elements to be present before attempting to act on them. There are times when you may need to add an explicit wait for other element-related or browser-related reasons. In the `waitmon` dependency, you'll find the `WaitBuilder`. The Javadoc comments demonstrate appropriate usage.

There are some additional comments in BaseTest itself describing what is happening therein, but you'll notice that it also includes common `setUp()` and `tearDown()` methods. These can be Overwritten normally in derivative test classes, but it would likely behoove you in most situations to still make the call (at least) to `super.setUp()` if you do.

* Requirements Coverage File (Optional)
    * In the example test class (GoogleExampleTest), you'll see a `.writeToFile()` call for the Requirements Coverage file. You'll notice a corresponding line in _src/main/resources/requirements_coverage.txt_. This call should be made in every test to send that information over to the coverage file (note that sorting should be automatic and duplicate lines won't be written).
* Allure annotations
    * `@Features` and `@Stories` should be included for each test method as well.
 
So long as your config file is in place, you should be able to run a test (or tests) directly in IntelliJ or via maven's cli (`$ mvn clean test`). If you'd like to overwrite any of the properties set in your config file, simply include them as a property in the maven cli call (for example: **run-location** in my .xml config is "local", but I'd like for it to be "browserstack" at runtime. Instead of modifying the xml, I could run the tests with the following command: `$ mvn clean test -Drun-location=browserstack`).

You'll notice that `BaseTest` is set up to use JUnit's Parameterized runner. This is what makes it possible to specify multiple browser configurations. Parallel runs can still be achieved via maven cli using these properties:

* -DparallelTarget
    * This defaults to classes. With the Parameterized runner, each browser configuration is considered its own class. Leaving this target as the default, then, means that one test will run at a time, but your different browser configurations will run simultaneously for that test.
    * another option here is to set the target to methods, which will run all the methods in a single test class simultaneously for a single browser configuration (then it'll either go to the next class for that same configuration or the next configuration in the same test class).
* -DparallelCount
    * This specifies the maximum number of parallel threads. Note that this doesn't necessarily mean that you will always have the max number available running at once. This is especially important to prevent using more than our available simultaneous VMs on Browserstack or Sauce (in addition to ensuring that you don't overload your local machine if running locally).
* -DparallelPerCore
    * `true` or `false` Defaults to `false`. See [these notes](https://github.com/fusion-alliance/archetype-web-automation-scaffold/issues/16#issuecomment-372395315) for more information on this property.

After running the desired tests via maven's cli, simply run `$ mvn site`. The generated report will be in _target/site/allure-maven-plugin.html_. You will need to open it up in a browser to view the report (in intelliJ: right-click on the file -> Open In Browser).