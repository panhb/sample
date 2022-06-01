package com.sample.convert;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import com.sample.convert.enums.FileType;
import com.sample.wkhtml.Wkhtml;
import com.sample.wkhtml.model.Execute;
import com.sample.wkhtml.model.Page;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

/**
 * @author hongbo.pan
 * @date 2022/5/24
 */
public class HtmlToImageByWkhtml extends AbstractConvertFile {

    @Override
    protected FileType sourceFileType() {
        return FileType.HTML;
    }

    @Override
    protected FileType destFileType() {
        return FileType.IMAGE;
    }

    @SneakyThrows
    @Override
    protected List<byte[]> handle(byte[] sourceFile) {
        String outPutFilePath = "D:\\tmp\\"+UUID.randomUUID().toString()+".jpg";
        Execute execute = Execute.builder()
                .pages(Lists.newArrayList(Page.forByte(sourceFile)))
                .outFilePath(outPutFilePath).build();
        Wkhtml.toImage(execute);
        return Lists.newArrayList(FileUtil.readBytes(outPutFilePath));
    }

    public static void main(String[] args) {
        AbstractConvertFile htmlToPdfByWkhtml = new HtmlToImageByWkhtml();
        List<byte[]> destList = htmlToPdfByWkhtml.convert(FileUtil.readBytes("C:\\Users\\hongbo.pan\\Desktop\\goodnews.html"));
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < destList.size(); i++) {
            byte[] destByte = destList.get(i);
            FileUtil.writeBytes(destByte, "D:\\pdf\\" + uuid + File.separator
                    + (i + 1) + ".jpg");
        }
    }
}
