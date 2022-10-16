package com.piral.liveweatherapp.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleUtils {

    public static String getMessageFromBundle(Locale locale, String key, Object... paramaters) {
        String result = key;

        try {
            if (key != null) {
                result = ResourceBundleUtils.getResourceBundle(locale).getString(key);

                if (paramaters != null && paramaters.length > 0) {
                    result = MessageFormat.format(result, paramaters);
                }
            }
        } catch (MissingResourceException e) {
            throw e;
        }

        return result;
    }

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("i18n/messages", locale);
    }
}
