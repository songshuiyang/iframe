package com.songsy.iframe.core.persistence.provider.constant;

public class CommonConstant {

    private static String[] INGORE_ATTR = {"version", "enable", "locked"};
    // 字符集
    public static final String CHAR_SPACE = " ";
    public static final String CHAR_SPACE_FULL = "　";
    public static final String CHAR_COMMA = ",";
    public static final String CHAR_TAB = "   ";
    public static final String CHAR_COMMA_FULL = "，";
    public static final String CHAR_ENTER = "\n";
    public static final String CHAR_DOUBLE_QUOTATION = "\"";

    public static final String[] DATE_PATTERNS = new String[] { "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyyMM", "yyyy/MM",
            "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyyMMddHHmmss",
            "yyyyMMddHHmm", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };
}
