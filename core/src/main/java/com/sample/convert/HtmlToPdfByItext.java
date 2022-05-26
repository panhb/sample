package com.sample.convert;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * @author hongbo.pan
 * @date 2022/5/24
 */
public class HtmlToPdfByItext extends AbstractConvertFile {

    @SneakyThrows
    @Override
    protected List<byte[]> handle(byte[] sourceFile) {
        @Cleanup ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(pdfStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        ConverterProperties props =  new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        //字体,支持中文  TODO
        FontProgram msyhbd = FontProgramFactory.createFont(IoUtil.readBytes(HtmlToPdfByItext.class.getResourceAsStream("/font/msyhbd.ttc")), 0, true);
        fontProvider.addFont(msyhbd);
        props.setFontProvider(fontProvider);
        String html = StrUtil.str(sourceFile, Charsets.UTF_8);
        List<IElement> iElements = HtmlConverter.convertToElements(html, props);
        Document document = new Document(pdfDocument, PageSize.A4, true);
        //去掉边框
        document.setMargins(0F, 0F, 0F, 0F);
        for (IElement iElement : iElements) {
            BlockElement blockElement = (BlockElement) iElement;
            blockElement.setMargins(0F, 0F, 0F, 0F);
            document.add(blockElement);
        }
        document.close();
        return Lists.newArrayList(pdfStream.toByteArray());
    }

    public static void main(String[] args) {
        AbstractConvertFile htmlToPdfByItext = new HtmlToPdfByItext();
        List<byte[]> destList = htmlToPdfByItext.convert(FileUtil.readBytes("C:\\Users\\hongbo.pan\\Desktop\\goodnews.html"));
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < destList.size(); i++) {
            byte[] destByte = destList.get(i);
            FileUtil.writeBytes(destByte, "D:\\pdf\\" + uuid + File.separator
                    + (i + 1) + ".pdf");
        }
    }
}
