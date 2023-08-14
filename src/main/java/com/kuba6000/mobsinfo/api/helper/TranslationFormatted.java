package com.kuba6000.mobsinfo.api.helper;

import java.io.Serializable;

public class TranslationFormatted implements Serializable {

    public final String key;
    public final Object[] formatting;

    public TranslationFormatted(String key, Object... format) {
        this.key = key;
        for (Object o : format) {
            if (!(o instanceof String || o instanceof Character || o instanceof Number))
                throw new IllegalArgumentException();
        }
        this.formatting = format;
    }

    public String get() {
        return TranslationHelper.translateFormattedFixed(key, formatting);
    }

}
