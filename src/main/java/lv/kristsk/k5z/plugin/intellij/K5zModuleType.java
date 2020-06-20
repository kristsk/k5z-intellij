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

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Computable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class K5zModuleType extends ModuleType<K5zModuleBuilder> {

    public static final String MODULE_TYPE_ID = "K5Z_MODULE";

    public K5zModuleType() {

        super(MODULE_TYPE_ID);
    }

    public static K5zModuleType getInstance() {

        return (K5zModuleType) ModuleTypeManager.getInstance().findByID(MODULE_TYPE_ID);
    }

    @NotNull
    @Override
    public K5zModuleBuilder createModuleBuilder() {

        return new K5zModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {

        return "K5Z Module";
    }

    @NotNull
    @Override
    public String getDescription() {

        return "K5Z Module description";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(boolean isOpened) {

        return K5zIcons.FILE;
    }

    public boolean isValidSdk(@NotNull final Module module, final Sdk projectSdk) {

        return projectSdk != null && projectSdk.getSdkType() == K5zSdkType.getInstance();
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull K5zModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {

        List<ModuleWizardStep> steps = new ArrayList<>();

        ProjectWizardStepFactory factory = ProjectWizardStepFactory.getInstance();

        ModuleWizardStep projectJdkStep = factory.createProjectJdkStep(
            wizardContext,
            SdkType.findInstance(K5zSdkType.class),
            moduleBuilder,
            new Computable.PredefinedValueComputable<>(true),
            null,
            ""
        );
        steps.add(projectJdkStep);

        return steps.toArray(new ModuleWizardStep[0]);
    }
}
