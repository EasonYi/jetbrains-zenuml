package org.intellij.plugins.markdown.actions;

import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;

@Ignore
public class MarkdownToggleBoldTest extends LightPlatformCodeInsightTestCase {

  public void testSimple() {
    doTest();
  }

  private void doTest() {
    configureByFile(getTestName(true) + "_before.zen");
    executeAction("org.intellij.plugins.markdown.ui.actions.styling.ZenUmlToggleBoldAction");
    checkResultByFile(getTestName(true) + "_after.zen");
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/actions/toggleBold/";
  }
}
