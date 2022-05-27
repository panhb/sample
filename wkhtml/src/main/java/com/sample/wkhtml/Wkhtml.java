package com.sample.wkhtml;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.sample.wkhtml.config.WkhtmlConfig;
import com.sample.wkhtml.enums.WkhtmlType;
import com.sample.wkhtml.exceptions.WkhtmlException;
import com.sample.wkhtml.model.Execute;
import com.sample.wkhtml.model.Page;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.concurrent.Future;

/**
 * @author hongbo.pan
 * @date 2022/5/26
 */
@Log
public class Wkhtml {

    public static void toPdf(Execute execute) {
        execute(execute, WkhtmlType.PDF);
    }

    public static void toImage(Execute execute) {
        if (CollectionUtil.isNotEmpty(execute.getPages()) && execute.getPages().size() > 1) {
            throw new WkhtmlException("image page size > 1");
        }
        execute(execute, WkhtmlType.IMAGE);
    }

    @SneakyThrows
    private static void execute(Execute execute, WkhtmlType wkhtmlType) {
        if (CollectionUtil.isEmpty(execute.getPages())) {
            throw new WkhtmlException("page is null");
        }
        if (StrUtil.isBlank(execute.getOutFilePath())) {
            throw new WkhtmlException("outFilePath is null");
        }
        String commandParams = execute.toString();
        String executable = WkhtmlConfig.findWkhtmlExecutable(wkhtmlType);
        String cmd = executable + " " + commandParams;
        log.info(cmd);
        Process process = RuntimeUtil.exec(cmd);
        Future<byte[]> resultFuture = ThreadUtil.execAsync(() -> IoUtil.readBytes(process.getInputStream()));
        Future<byte[]> errorFuture = ThreadUtil.execAsync(() -> IoUtil.readBytes(process.getErrorStream()));
        process.waitFor();
        if (process.exitValue() != 0) {
            log.info(StrUtil.str(errorFuture.get(), Charsets.UTF_8));
        }
        log.info(StrUtil.str(resultFuture.get(), Charsets.UTF_8));
    }

    public static void main(String[] args) {
        Execute execute = Execute.builder()
                .pages(Lists.newArrayList(Page.file("C:\\Users\\hongbo.pan\\Desktop\\goodnews.html")))
                .outFilePath("C:\\Users\\hongbo.pan\\Desktop\\2.pdf").build();
        toPdf(execute);
        execute.setOutFilePath("C:\\Users\\hongbo.pan\\Desktop\\2.jpg");
        toImage(execute);
    }
}
