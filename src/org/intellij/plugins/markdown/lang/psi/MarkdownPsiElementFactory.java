// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.intellij.plugins.markdown.lang.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.testFramework.LightVirtualFile;
import org.intellij.plugins.markdown.lang.MarkdownLanguage;
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl;
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarkdownPsiElementFactory {
  private MarkdownPsiElementFactory() { }

  @Nullable
  public static MarkdownFile createFile(@NotNull Project project, @NotNull String text) {
    final LightVirtualFile virtualFile = new LightVirtualFile("temp.rb", MarkdownLanguage.INSTANCE, text);
    return (MarkdownFile)((PsiFileFactoryImpl)PsiFileFactory.getInstance(project))
      .trySetupPsiForFile(virtualFile, MarkdownLanguage.INSTANCE, true, true);
  }

  @NotNull
  public static MarkdownCodeFenceImpl createCodeFence(@NotNull Project project, @Nullable String language, @NotNull String text) {
    text = StringUtil.isEmpty(text) ? "" : "\n" + text;
    String content = "```" + StringUtil.notNullize(language) + text + "\n" + "```";
    final MarkdownFile file = createFile(project, content);

    if (file == null) {
      throw new RuntimeException("Cannot create a new markdown file. Text: " + content);
    }

    return (MarkdownCodeFenceImpl)file.getFirstChild().getFirstChild();
  }
}