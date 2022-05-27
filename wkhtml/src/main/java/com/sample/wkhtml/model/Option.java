package com.sample.wkhtml.model;

import com.sample.wkhtml.enums.LogLevel;

/**
 * @author hongbo.pan
 * @date 2022/5/26
 */
public class Option {

    public final static String SEPARATOR = " ";

    /**
     * Collate when printing multiple copies (default)
     * @return
     */
    public static String collate() {
        return "--collate";
    }

    /**
     * Do not collate when printing multiple copies
     * @return
     */
    public static String noCollate() {
        return "--no-collate";
    }

    /**
     * Read and write cookies from and to the supplied cookie jar file
     * @return
     */
    public static String cookieJar(String path) {
        return "--cookie-jar" + SEPARATOR + path;
    }

    /**
     * Number of copies to print into the pdf file (default 1)
     * @return
     */
    public static String copies(Number number) {
        return "--copies" + SEPARATOR + number.toString();
    }

    /**
     * Change the dpi explicitly (this has no effect on X11 based systems) (default 96)
     * @return
     */
    public static String dpi(Number dpi) {
        return "--dpi" + SEPARATOR + dpi.toString();
    }

    /**
     * Display more extensive help, detailing less common command switches
     * @return
     */
    public static String extendedHelp() {
        return "--extended-help";
    }

    /**
     * PDF will be generated in grayscale
     * @return
     */
    public static String grayscale() {
        return "--grayscale";
    }

    /**
     * Display help
     * @return
     */
    public static String help() {
        return "--help";
    }

    /**
     * Output program html help
     * @return
     */
    public static String htmldoc() {
        return "--htmldoc";
    }

    /**
     * When embedding images scale them down to this dpi (default 600)
     * @return
     */
    public static String imageDpi(Integer dpi) {
        return "--image-dpi" + SEPARATOR + dpi.toString();
    }

    /**
     * When jpeg compressing images use this quality (default 94)
     * @return
     */
    public static String imageQuality(Integer quality) {
        return "--image-quality" + SEPARATOR + quality.toString();
    }

    /**
     * Output license information and exit
     * @return
     */
    public static String license() {
        return "--license";
    }

    /**
     * Set log level to: none, error, warn or info (default info)
     * @return
     */
    public static String logLevel(LogLevel logLevel) {
        return "--log-level" + SEPARATOR + logLevel.getLevel();
    }
}
