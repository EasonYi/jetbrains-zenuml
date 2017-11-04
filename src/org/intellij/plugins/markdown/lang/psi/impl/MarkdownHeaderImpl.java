package org.intellij.plugins.markdown.lang.psi.impl;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import org.intellij.plugins.markdown.lang.MarkdownElementTypes;
import org.intellij.plugins.markdown.lang.MarkdownTokenTypeSets;
import org.intellij.plugins.markdown.lang.psi.MarkdownRecursiveElementVisitor;
import org.intellij.plugins.markdown.lang.stubs.MarkdownStubBasedPsiElementBase;
import org.intellij.plugins.markdown.lang.stubs.MarkdownStubElement;
import org.intellij.plugins.markdown.lang.stubs.impl.MarkdownHeaderStubElement;
import org.intellij.plugins.markdown.lang.stubs.impl.MarkdownHeaderStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.intellij.plugins.markdown.highlighting.MarkdownHighlighterColors.*;

public class MarkdownHeaderImpl extends MarkdownStubBasedPsiElementBase<MarkdownStubElement> {
  TextAttributesKey[] HEADERS_TEXT_ATTRIBUTES_KEY = new TextAttributesKey[]{
    HEADER_PRESENTATION_LEVEL_1_ATTR_KEY,
    HEADER_PRESENTATION_LEVEL_2_ATTR_KEY,
    HEADER_PRESENTATION_LOW_LEVELS,
    HEADER_PRESENTATION_LOW_LEVELS,
    HEADER_PRESENTATION_LOW_LEVELS,
    HEADER_PRESENTATION_LOW_LEVELS
  };

  public MarkdownHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public MarkdownHeaderImpl(MarkdownHeaderStubElement stub, MarkdownHeaderStubElementType type) {
    super(stub, type);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownRecursiveElementVisitor) {
      ((MarkdownRecursiveElementVisitor)visitor).visitHeader(this);
      return;
    }

    super.accept(visitor);
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    String headerText = getHeaderText();
    String text = headerText == null ? "Invalid header: " + getText() : headerText;

    return new PresentationData(text, "", null, HEADERS_TEXT_ATTRIBUTES_KEY[getHeaderNumber() - 1]);
  }

  @Nullable
  private String getHeaderText() {
    if (!isValid()) {
      return null;
    }
    final PsiElement contentHolder = findChildByType(MarkdownTokenTypeSets.INLINE_HOLDING_ELEMENT_TYPES);
    if (contentHolder == null) {
      return null;
    }

    return StringUtil.trim(contentHolder.getText());
  }

  private int getHeaderNumber() {
    final IElementType type = getNode().getElementType();
    if (MarkdownTokenTypeSets.HEADER_LEVEL_1_SET.contains(type)) {
      return 1;
    }
    if (MarkdownTokenTypeSets.HEADER_LEVEL_2_SET.contains(type)) {
      return 2;
    }
    if (type == MarkdownElementTypes.ATX_3) {
      return 3;
    }
    if (type == MarkdownElementTypes.ATX_4) {
      return 4;
    }
    if (type == MarkdownElementTypes.ATX_5) {
      return 5;
    }
    if (type == MarkdownElementTypes.ATX_6) {
      return 6;
    }
    throw new IllegalStateException("Type should be one of header types");
  }


  @Override
  public String getName() {
    return getHeaderText();
  }
}
