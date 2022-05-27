package com.sample.wkhtml.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hongbo.pan
 * @date 2022/5/26
 */
@AllArgsConstructor
@Getter
public enum WkhtmlType {

    PDF("wkhtmltopdf"),
    IMAGE("wkhtmltoimage");

    private String type;
}
