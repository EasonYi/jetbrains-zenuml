package org.intellij.plugins.markdown.settings;

import com.intellij.util.xmlb.annotations.Attribute;
import org.intellij.plugins.markdown.ui.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;

public final class MarkdownPreviewSettings {
  public static final MarkdownPreviewSettings DEFAULT = new MarkdownPreviewSettings();

  @Attribute("DefaultSplitLayout")
  @NotNull
  private SplitFileEditor.SplitEditorLayout mySplitEditorLayout = SplitFileEditor.SplitEditorLayout.SPLIT;

  @Attribute("UseGrayscaleRendering")
  private boolean myUseGrayscaleRendering = false;

  @Attribute("AutoScrollPreview")
  private boolean myIsAutoScrollPreview = true;

  public MarkdownPreviewSettings() {
  }

  public MarkdownPreviewSettings(@NotNull SplitFileEditor.SplitEditorLayout splitEditorLayout,
                                 boolean useGrayscaleRendering,
                                 boolean isAutoScrollPreview) {
    mySplitEditorLayout = splitEditorLayout;
    myUseGrayscaleRendering = useGrayscaleRendering;
    myIsAutoScrollPreview = isAutoScrollPreview;
  }

  @NotNull
  public SplitFileEditor.SplitEditorLayout getSplitEditorLayout() {
    return mySplitEditorLayout;
  }

  public boolean isUseGrayscaleRendering() {
    return myUseGrayscaleRendering;
  }

  public boolean isAutoScrollPreview() {
    return myIsAutoScrollPreview;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MarkdownPreviewSettings settings = (MarkdownPreviewSettings)o;

    if (myUseGrayscaleRendering != settings.myUseGrayscaleRendering) return false;
    if (myIsAutoScrollPreview != settings.myIsAutoScrollPreview) return false;
    if (mySplitEditorLayout != settings.mySplitEditorLayout) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mySplitEditorLayout.hashCode();
    result = 31 * result + (myUseGrayscaleRendering ? 1 : 0);
    result = 31 * result + (myIsAutoScrollPreview ? 1 : 0);
    return result;
  }

  public interface Holder {
    void setMarkdownPreviewSettings(@NotNull MarkdownPreviewSettings settings);

    @NotNull
    MarkdownPreviewSettings getMarkdownPreviewSettings();
  }
}
