package socialnetwork.helpers;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Roman on 19.08.2017.
 */
public class StringHelper {
    public static String replaceSymbols(String string) {
        return StringUtils.replaceEach(string, new String[]{"&", "\"", "<", ">", "'", "/",}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&apos;", "&#x2F;"});
    }
}
