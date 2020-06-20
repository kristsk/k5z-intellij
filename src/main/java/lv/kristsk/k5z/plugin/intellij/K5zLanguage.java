package lv.kristsk.k5z.plugin.intellij;

import com.intellij.lang.Language;

public class K5zLanguage extends Language {

    public static final K5zLanguage INSTANCE = new K5zLanguage();

    private K5zLanguage() {

        super("K5Z");
    }
}
