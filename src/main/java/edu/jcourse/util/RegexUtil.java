package edu.jcourse.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {
    public static final String EMAIL_REGEX = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    public static final String IMAGE_FORMAT_REGEX = ".+\\.(PNG|JPG|JPEG)$";
}