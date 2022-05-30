package com.sample.wkhtml.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.sample.wkhtml.enums.PageType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongbo.pan
 * @date 2022/5/26
 */
@Builder
@Data
public class Execute {

    /**
     * pages
     */
    private List<Page> pages;

    /**
     * options
     */
    private List<String> options;

    /**
     * outFilePath
     */
    private String outFilePath;

    @Override
    public String toString() {
        List<String> commands = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(options)) {
            commands.addAll(options);
        }
        commands.addAll(pages.stream().map(page -> {
            String content = page.getContent();
            if (PageType.HTML.equals(page.getPageType())) {
                String tempPath = "D:\\temp\\" + UUID.randomUUID().toString() + ".html";
                FileUtil.writeString(content, tempPath, Charsets.UTF_8);
                return tempPath;
            }
            return content;
        }).collect(Collectors.toList()));
        commands.add(outFilePath);
        return commands.stream().collect(Collectors.joining(" "));
    }
}
