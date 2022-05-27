package com.sample.wkhtml.model;

import com.sample.wkhtml.enums.PageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hongbo.pan
 * @date 2022/5/26
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Page {

    private String content;

    private PageType pageType;

    public static Page html(String html) {
        return new Page(html, PageType.HTML);
    }

    public static Page url(String url) {
        return new Page(url, PageType.URL);
    }

    public static Page file(String file) {
        return new Page(file, PageType.FILE);
    }

}
