/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.plugins.markdown.lang.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.plugins.markdown.lang.MarkdownFileType;
import org.intellij.plugins.markdown.lang.MarkdownLanguage;
import org.intellij.plugins.markdown.lang.psi.MarkdownElementVisitor;
import org.intellij.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.intellij.plugins.markdown.lang.psi.MarkdownRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MarkdownFile extends PsiFileBase implements MarkdownPsiElement {
  public MarkdownFile(FileViewProvider viewProvider) {
    super(viewProvider, MarkdownLanguage.INSTANCE);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownElementVisitor) {
      ((MarkdownElementVisitor)visitor).visitMarkdownFile(this);
      return;
    }

    if (visitor instanceof MarkdownRecursiveElementVisitor) {
      ((MarkdownRecursiveElementVisitor)visitor).visitMarkdownFile(this);
      return;
    }

    visitor.visitFile(this);
  }

  @NotNull
  public FileType getFileType() {
    return MarkdownFileType.INSTANCE;
  }

  @NotNull
  @Override
  public List<MarkdownPsiElement> getCompositeChildren() {
    return Arrays.asList(findChildrenByClass(MarkdownPsiElement.class));
  }

  @NotNull
  public Collection<MarkdownHeaderImpl> getHeaders() {
    final Collection<MarkdownHeaderImpl> list = ContainerUtil.newArrayList();
    accept(new MarkdownRecursiveElementVisitor() {
      @Override
      public void visitHeader(@NotNull MarkdownHeaderImpl header) {
        list.add(header);
      }
    });

    return list;
  }
}
