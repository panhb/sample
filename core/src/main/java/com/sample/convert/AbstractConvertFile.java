package com.sample.convert;

import cn.hutool.crypto.digest.MD5;
import com.sample.convert.enums.FileType;

import java.util.List;

/**
 * @author hongbo.pan
 * @date 2022/5/24
 */
public abstract class AbstractConvertFile {

    /**
     * sourceFileType
     * @return
     */
    protected abstract FileType sourceFileType();

    /**
     * destFileType
     * @return
     */
    protected abstract FileType destFileType();

    /**
     * 转换文件
     * @param sourceFile
     * @return byte[]
     */
    public List<byte[]> convert(byte[] sourceFile) {
        byte[] newSourceFile = preHandleSourceFile(sourceFile);
        List<byte[]> destFile = handle(newSourceFile);
        afterConvert(sourceFile, newSourceFile, destFile);
        return destFile;
    }

    /**
     * preHandleSourceFile
     * @param sourceFile
     */
    protected byte[] preHandleSourceFile(final byte[] sourceFile){
        return sourceFile;
    }

    /**
     * 执行转换
     * @param sourceFile
     * @return byte[]
     */
    protected abstract List<byte[]> handle(byte[] sourceFile);

    /**
     * afterConvert
     * @param sourceFile
     * @param newSourceFile
     * @param destFile
     */
    protected void afterConvert(byte[] sourceFile, byte[] newSourceFile, List<byte[]> destFile){
        String sourceMd5Hex = MD5.create().digestHex(sourceFile);
    }
}
