/*
 * Copyright (c) 2014, Krists Krigers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package lv.kristsk.k5z.plugin.intellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class K5zSdkType extends SdkType {

    public static final String K5Z_SDK_NAME = "K5Z SDK";

    public static K5zSdkType getInstance() {

        return SdkType.findInstance(K5zSdkType.class);
    }

    public K5zSdkType() {

        super(K5Z_SDK_NAME);
    }

    @Override
    public String suggestHomePath() {

        return null;
    }

    @Override
    public boolean isValidSdkHome(String path) {

        return true;
    }

    @Override
    public Icon getIcon() {

        return K5zIcons.FILE;
    }

    @NotNull
    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome) {

        return "K5Z " + getVersionString(sdkHome);
    }

    @Override
    public String getVersionString(@NotNull Sdk sdk) {

        return getVersionString(sdk.getHomePath());
    }

    @Override
    public String getVersionString(String sdkHome) {

        return "0.1";
    }

    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {

        return null;
    }

    @Override
    public void setupSdkPaths(@NotNull Sdk sdk) {

        VirtualFile homeDirectory = sdk.getHomeDirectory();

        if (sdk.getSdkType() != this || homeDirectory == null) {
            return;
        }

        final VirtualFile sdkSourcesRoot = homeDirectory;

        final SdkModificator sdkModificator = sdk.getSdkModificator();

        ApplicationManager.getApplication().runWriteAction(() -> {

            sdkModificator.addRoot(sdkSourcesRoot, OrderRootType.CLASSES);
            sdkModificator.addRoot(sdkSourcesRoot, OrderRootType.SOURCES);
        });

        sdkModificator.setVersionString(getVersionString(sdk));
        sdkModificator.commitChanges();

        sdkSourcesRoot.refresh(false, false);
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {

    }

    @NotNull
    @Override
    public String getPresentableName() {

        return K5Z_SDK_NAME;
    }

    @Override
    public boolean isRootTypeApplicable(@NotNull OrderRootType type) {

        return type == OrderRootType.CLASSES || type == OrderRootType.SOURCES;
    }

    public static Collection<VirtualFile> getLibraryDirectories() {

        Collection<VirtualFile> directories = new ArrayList<>();

        ProjectJdkTable jdkTable = ProjectJdkTable.getInstance();
        List<Sdk> sdks = jdkTable.getSdksOfType(jdkTable.getSdkTypeByName(K5zSdkType.K5Z_SDK_NAME));

        if (!sdks.isEmpty()) {

            Sdk sdk = sdks.get(0);

            directories.add(sdk.getHomeDirectory());

            VirtualFile[] files = sdk.getRootProvider().getFiles(OrderRootType.CLASSES);

            Collections.addAll(directories, files);
        }

        return directories;
    }

    public static K5zFileRoot resolveToK5zFileRoot(Collection<VirtualFile> libraryDirectories, Project project, String relativePathAndName) {

        PsiManager psiManager = PsiManager.getInstance(project);

        PsiElement targetElement = null;

        for (VirtualFile directory : libraryDirectories) {
            VirtualFile targetVirtualFile = directory.findFileByRelativePath(relativePathAndName);

            if (targetVirtualFile != null) {
                targetElement = psiManager.findFile(targetVirtualFile);
            }
        }

        return targetElement != null ? (K5zFileRoot) targetElement : null;
    }
}
