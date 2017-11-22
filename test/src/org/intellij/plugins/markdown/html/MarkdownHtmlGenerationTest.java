package org.intellij.plugins.markdown.html;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.intellij.plugins.markdown.ui.preview.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

public class MarkdownHtmlGenerationTest extends LightPlatformCodeInsightFixtureTestCase {
  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/html";
  }

  private void doTest(@NotNull String htmlText) {
    PsiFile mdFile = myFixture.configureByFile(getTestName(true) + ".md");

    assertTrue(MarkdownUtil.generateMarkdownHtml(mdFile.getVirtualFile(), mdFile.getText()).contains(htmlText));
  }

  public void testCodeFenceWithLang() {
    doTestByHtmlFile();
  }

  public void testCodeFenceWithoutLang() {
    doTestByHtmlFile();
  }

  public void testPlantUML1() {
    doTestPlantUML();
  }

  public void testPlantUML2() {
    doTestPlantUML();
  }

  public void testPuml() {
    doTestPlantUML();
  }

  void doTestPlantUML() {
    doTest("<img src=\"file:" + PathManager.getSystemPath());
  }

  void doTestByHtmlFile() {
    doTest(myFixture.configureByFile(getTestName(true) + ".html").getText());
  }
}
