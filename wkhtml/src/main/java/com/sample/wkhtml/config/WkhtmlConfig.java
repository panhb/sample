package com.sample.wkhtml.config;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.sample.wkhtml.enums.WkhtmlType;
import com.sample.wkhtml.exceptions.WkhtmlException;

import java.util.stream.Collectors;

/**
 * @author hongbo.pan
 * @date 2022/5/27
 */
public class WkhtmlConfig {

    public static String findWkhtmlExecutable(WkhtmlType wkhtmlType) {
        OsInfo osInfo = SystemUtil.getOsInfo();
        String type = wkhtmlType.getType();
        String cmd = "which";
        if (osInfo.isWindows()) {
            String path = System.getenv("Path");
            if (!path.contains("wkhtmltopdf")) {
                throw new WkhtmlException("windows环境变量未配置");
            }
            cmd = "where";
        }
        cmd += " " + type;
        String result = RuntimeUtil.execForStr(cmd).trim();
        if (StrUtil.isBlank(result)) {
            throw new WkhtmlException("没有找到可执行命令" + wkhtmlType.getType());
        }
        if (StrUtil.containsBlank(result)) {
            throw new WkhtmlException("有空格:" + result);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findWkhtmlExecutable(WkhtmlType.PDF));
        System.out.println(findWkhtmlExecutable(WkhtmlType.IMAGE));
    }
}
