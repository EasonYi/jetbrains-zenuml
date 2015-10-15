package org.intellij.plugins.markdown.settings;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class MarkdownLAFListener implements LafManagerListener {
  private boolean isLastLAFWasDarcula = isDarcula(LafManager.getInstance().getCurrentLookAndFeel());

  @Override
  public void lookAndFeelChanged(LafManager source) {
    final UIManager.LookAndFeelInfo newLookAndFeel = source.getCurrentLookAndFeel();
    final boolean isNewLookAndFeelDarcula = isDarcula(newLookAndFeel);

    if (isNewLookAndFeelDarcula == isLastLAFWasDarcula) {
      return;
    }

    updateCssSettingsForced(isNewLookAndFeelDarcula);
  }

  public void updateCssSettingsForced(boolean isDarcula) {
    final MarkdownCssSettings currentCssSettings = MarkdownApplicationSettings.getInstance().getMarkdownCssSettings();
    if (isDefaultCssSettings(currentCssSettings)) {
      MarkdownApplicationSettings.getInstance().setMarkdownCssSettings(MarkdownCssSettings.getDefaultCssSettings(isDarcula));
    }
    isLastLAFWasDarcula = isDarcula;
  }

  public static boolean isDarcula(@NotNull UIManager.LookAndFeelInfo laf) {
    return laf.getName().contains("Darcula");
  }

  private static boolean isDefaultCssSettings(@NotNull MarkdownCssSettings settings) {
    return settings.equals(MarkdownCssSettings.DARCULA) || settings.equals(MarkdownCssSettings.DEFAULT);
  }
}