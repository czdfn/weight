package com.chengsi.weightcalc.db;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ItextDemo {

    //表头
    public static final String[] tableHeader = { "企业中文名", "所属国家", "企业英文名",
            "2003年排名", "2004年排名", "2005年排名", "2006年排名", "2007年排名", "主要业务",
            "2003年营业额", "2004年营业额", "2005年营业额", "2006年营业额", "2007年营业额", "企业编号",
            "名次升降", "图片", "状况" };
    //数据表字段数
    private static final int colNumber = 18;
    //表格的设置
    private static final int spacing = 2;
    //表格的设置
    private static final int padding = 2;
    //导出Pdf文挡
    public static void exportPdfdocument() {
        // 创建文Pdf文挡
        Document document = new Document(new Rectangle(1500, 2000), 10, 10, 10,
                10);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(
                    "d:\\世界五百强企业名次表.pdf"));
            document.open();
            // 中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontChinese = new Font(bfChinese, 12, Font.ITALIC);
            // 创建有colNumber(18)列的表格
            PdfPTable datatable = new PdfPTable(colNumber);
            int[] cellsWidth = { 8, 5, 8, 3, 3, 3, 3, 2, 6, 4, 4, 4, 4, 2, 2,
                    2, 2, 2 };
            datatable.setWidths(cellsWidth);
            datatable.setWidthPercentage(100); // 表格的宽度百分比
            datatable.getDefaultCell().setPadding(padding);
            datatable.getDefaultCell().setBorderWidth(spacing);
//            datatable.getDefaultCell().setBackgroundColor(Color.);
            datatable.getDefaultCell().setHorizontalAlignment(
                    Element.ALIGN_CENTER);
            // 添加表头元素
            for (int i = 0; i < colNumber; i++) {
                datatable.addCell(new Paragraph(tableHeader[i], fontChinese));
            }
            datatable.setHeaderRows(1); // 表头结束
            datatable.getDefaultCell().setBorderWidth(1);

//            ResultSet rs = SheetDataSource.selectAllDataFromDB();
//            int rowIndex = 1;
//            while (rs.next()) {
//                if (rowIndex % 2 == 1) {
//                    datatable.getDefaultCell().setGrayFill(0.9f);
//                }
//                for (int i = 1; i <= colNumber; i++)
//                    datatable.addCell(new Paragraph(rs.getString(i),
//                            fontChinese));
//                if (rowIndex % 2 == 1) {
//                    datatable.getDefaultCell().setGrayFill(1.0f);
//                }
//                rowIndex++;
//            }
            document.add(datatable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }

    public static void main(String[] args) {
        exportPdfdocument();
    }
}
