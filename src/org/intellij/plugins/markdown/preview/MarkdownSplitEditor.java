package org.intellij.plugins.markdown.preview;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretAdapter;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import org.intellij.plugins.markdown.preview.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;

public class MarkdownSplitEditor extends SplitFileEditor {
  public MarkdownSplitEditor(@NotNull TextEditor mainEditor,
                             @NotNull MarkdownPreviewFileEditor secondEditor) {
    super(mainEditor, secondEditor);

    mainEditor.getEditor().getCaretModel().addCaretListener(new MyCaretListener(secondEditor));
  }

  @NotNull
  @Override
  public String getName() {
    return "Markdown split editor";
  }

  private static class MyCaretListener extends CaretAdapter {
    @NotNull
    private final MarkdownPreviewFileEditor myPreviewFileEditor;

    public MyCaretListener(@NotNull MarkdownPreviewFileEditor previewFileEditor) {
      myPreviewFileEditor = previewFileEditor;
    }

    @Override
    public void caretPositionChanged(CaretEvent e) {
      final Editor editor = e.getEditor();
      if (editor.getCaretModel().getCaretCount() != 1) {
        return;
      }

      final int offset = editor.logicalPositionToOffset(e.getNewPosition());
      myPreviewFileEditor.scrollToSrcOffset(offset);
    }
  }
}
