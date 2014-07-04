package org.test.httpclient;

import java.nio.charset.Charset;

public class BaseConst {

    /**
     * separator: ##
     */
    public static final String SEPARATOR = "##";

    public static final long SECOND = 1000;

    public static final long MINUTE = 60 * SECOND;

    public static final long HOUR = 60 * MINUTE;

    public static final long DAY = 24 * HOUR;

    public static final long MONTH = 30 * DAY;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    public static final String NEWLINE = LINE_SEPARATOR;
    
    public static final String NEWLINE_WINDOWS = "\r\n";

    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static final Charset GBK = Charset.forName("GBK");
}
