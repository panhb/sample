package com.sample.wkhtml.model;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Charsets;
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

    public static Page forHtml(String html) {
        return new Page(html, PageType.HTML);
    }

    public static Page forUrl(String url) {
        return new Page(url, PageType.URL);
    }

    public static Page forFile(String file) {
        return new Page(file, PageType.FILE);
    }

    public static Page forByte(byte[] bytes) {
        return Page.forHtml(StrUtil.str(bytes, Charsets.UTF_8));
    }

}
