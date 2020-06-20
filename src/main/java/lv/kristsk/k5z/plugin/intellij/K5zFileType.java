package lv.kristsk.k5z.plugin.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class K5zFileType extends LanguageFileType {

    public static final String FILE_EXTENSION = "k5z";
    public static final K5zFileType INSTANCE = new K5zFileType();

    protected K5zFileType() {

        super(K5zLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {

        return "K5Z File";
    }

    @NotNull
    @Override
    public String getDescription() {

        return "K5Z language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {

        return FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {

        return K5zIcons.FILE;
    }
}
