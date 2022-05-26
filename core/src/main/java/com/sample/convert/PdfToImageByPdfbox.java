package com.sample.convert;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author hongbo.pan
 * @date 2022/5/24
 */
public class PdfToImageByPdfbox extends AbstractConvertFile {

    private final static String IMAGE_SUFFIX = "jpg";

    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10,
            300, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
            ThreadUtil.newNamedThreadFactory("pdfbox-", true));


    @SneakyThrows
    @Override
    protected List<byte[]> handle(byte[] sourceFile) {
        List<byte[]> destList = Lists.newArrayList();
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        PDDocument pd = Loader.loadPDF(sourceFile);
        int pageSize = pd.getNumberOfPages();
        pd.close();
        if (pageSize > 0) {
            List<CompletableFuture<byte[]>> futureList = Lists.newArrayList();
            for (int i = 0; i < pageSize; i++) {
                int finalI = i;
                futureList.add(CompletableFuture.supplyAsync(() -> toImage(finalI, sourceFile), threadPoolExecutor));
            }
            CompletableFuture<Void> resultFuture =
                    CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            resultFuture.join();
            destList = resultFuture.thenApply(v -> futureList.stream().map(future ->
                    future.join()).filter(Objects::nonNull).collect(Collectors.toList())).get();
        }
        return destList;
    }

    @SneakyThrows
    private byte[] toImage(int pageIndex, byte[] sourceFile) {
        long start = System.currentTimeMillis();
        @Cleanup ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
        PDDocument pdDocument = Loader.loadPDF(sourceFile);
        PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 120, ImageType.RGB);
        ImageIO.write(bufferedImage, IMAGE_SUFFIX, imgStream);
        pdDocument.close();
        System.out.println(Thread.currentThread().getName() + "-" + this.getClass().getName() + "-toImage-" + (System.currentTimeMillis() - start));
        return imgStream.toByteArray();
    }

    public static void main(String[] args) {
        AbstractConvertFile pdfToImageByPdfbox = new PdfToImageByPdfbox();
        List<byte[]> destList = pdfToImageByPdfbox.convert(FileUtil.readBytes("C:\\Users\\hongbo.pan\\Desktop\\0922.pdf"));
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < destList.size(); i++) {
            byte[] destByte = destList.get(i);
            FileUtil.writeBytes(destByte, "D:\\image\\" + uuid + File.separator
                    + (i + 1) + "." + IMAGE_SUFFIX);
        }
    }
}
