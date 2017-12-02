package org.intellij.plugins.markdown.ui.preview;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.Alarm;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.plugins.markdown.extensions.MarkdownCodeFencePluginGeneratingProvider;
import org.intellij.plugins.markdown.lang.MarkdownFileType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.util.ArrayUtilRt.EMPTY_FILE_ARRAY;

public class MarkdownCodeFencePluginCache implements Disposable {
  public static final String MARKDOWN_FILE_PATH_KEY = "markdown-md5-file-path";

  @NotNull private Alarm myAlarm = new Alarm(this);

  @NotNull private final Collection<MarkdownCodeFencePluginCacheProvider> myCodeFencePluginCaches = ContainerUtil.newConcurrentSet();
  @NotNull private final Collection<File> myAdditionalCacheToDelete = ContainerUtil.newConcurrentSet();
  @NotNull private static final Collection<File> CODE_FENCE_PLUGIN_SYSTEM_PATHS = getPluginSystemPaths();

  public static MarkdownCodeFencePluginCache getInstance() {
    return ServiceManager.getService(MarkdownCodeFencePluginCache.class);
  }

  public MarkdownCodeFencePluginCache() {
    scheduleClearCache();

    VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileListener() {
      @Override
      public void fileDeleted(@NotNull VirtualFileEvent event) {
        if (MarkdownFileType.INSTANCE == event.getFile().getFileType()) {
          myAdditionalCacheToDelete.addAll(processSourceFileToDelete(event.getFile(), ContainerUtil.emptyList()));
        }
      }
    });
  }

  private static List<File> getPluginSystemPaths() {
    return Arrays.stream(MarkdownCodeFencePluginGeneratingProvider.Companion.getEP_NAME().getExtensions())
      .map(provider -> new File(provider.getCacheRootPath()))
      .collect(Collectors.toList());
  }

  public Collection<File> collectFilesToRemove() {
    return myCodeFencePluginCaches.stream()
      .flatMap(cacheProvider -> processSourceFileToDelete(cacheProvider.getFile(), cacheProvider.getAliveCachedFiles()).stream())
      .collect(Collectors.toList());
  }

  private static Collection<File> processSourceFileToDelete(@NotNull VirtualFile sourceFile, @NotNull Collection<File> aliveCachedFiles) {
    Collection<File> filesToDelete = ContainerUtil.newHashSet();
    for (File codeFencePluginSystemPath : CODE_FENCE_PLUGIN_SYSTEM_PATHS) {
      for (File sourceFileCacheDirectory : getChildren(codeFencePluginSystemPath)) {
        if (isCachedSourceFile(sourceFileCacheDirectory, sourceFile) && aliveCachedFiles.isEmpty()) {
          filesToDelete.add(sourceFileCacheDirectory);
          continue;
        }

        for (File imgFile : getChildren(sourceFileCacheDirectory)) {
          if (!isCachedSourceFile(sourceFileCacheDirectory, sourceFile) || aliveCachedFiles.contains(imgFile)) continue;

          filesToDelete.add(imgFile);
        }
      }
    }

    return filesToDelete;
  }

  @NotNull
  private static File[] getChildren(@NotNull File directory) {
    File[] files = directory.listFiles();
    return files != null ? files : EMPTY_FILE_ARRAY;
  }

  private static boolean isCachedSourceFile(@NotNull File sourceFileDir, @NotNull VirtualFile sourceFile) {
    return sourceFileDir.getName().equals(MarkdownUtil.md5(sourceFile.getPath(), MARKDOWN_FILE_PATH_KEY));
  }

  public void registerCacheProvider(@NotNull MarkdownCodeFencePluginCacheProvider pluginCacheProvider) {
    myCodeFencePluginCaches.add(pluginCacheProvider);
  }

  private void scheduleClearCache() {
    myAlarm.addRequest(() -> {
      Collection<File> filesToDelete = ContainerUtil.union(myAdditionalCacheToDelete, collectFilesToRemove());
      ApplicationManager.getApplication().invokeLater(() -> WriteAction.run(() -> FileUtil.asyncDelete(filesToDelete)));

      clear();

      scheduleClearCache();
    }, Registry.intValue("markdown.clear.cache.interval"));
  }

  private void clear() {
    myAdditionalCacheToDelete.clear();
    myCodeFencePluginCaches.clear();
  }

  @Override
  public void dispose() {
    Disposer.dispose(myAlarm);
  }
}