package com.chengsi.weightcalc.utils.print;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-02-28.
 */
public class PrintMeasure {


    public static Phrase spaceline = new Phrase(Chunk.NEWLINE);
    Map<String, String> map = new HashMap<>();
    String type;

    public PrintMeasure() {

    }

    public PrintMeasure(Map<String, String> map, String type) {
        this.map = map;
        this.type = type;
    }

    public void print() throws IOException, DocumentException {
        try {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "pdfdemo");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("TAG", "Pdf Directory created");
            }

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            File myFile = new File(pdfFolder + timeStamp + ".pdf");

            OutputStream output = new FileOutputStream(myFile);
//---------------------设置字体-------------------------
            BaseFont bfChinese = BaseFont.createFont("assets/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese, 22, Font.BOLD);
            Font bold_chinese = new Font(bfChinese, 14, Font.BOLD);
            Rectangle rect = new Rectangle(PageSize.A4);
            Document doc = new Document(rect);
            PdfWriter.getInstance(doc, output);
            doc.open();

            CustomCell border = new CustomCell();
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(60);
            PdfPCell cell = new PdfPCell();
            Paragraph pname = new Paragraph("    水 尺 计 重 计 算 单    ", font);
            pname.setAlignment(Element.ALIGN_CENTER);
            pname.setSpacingAfter(5f);
            cell.addElement(pname);
            cell.setPaddingBottom(5f);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setCellEvent(border);
            cell.setColspan(3);
            table.addCell(cell);
            doc.add(table);

            for (int row = 1; row < 15; row++) {
                switch (row) {
                    case 1:
                        table = new PdfPTable(8);
                        table.setWidthPercentage(100);
                        PdfPCell cell1 = new PdfPCell();
                        PdfPCell cell2 = new PdfPCell();
                        PdfPCell cell3 = new PdfPCell();
                        PdfPCell cell4 = new PdfPCell();
                        PdfPCell cell5 = new PdfPCell();
                        PdfPCell cell6 = new PdfPCell();
                        for (int i = 1; i <= 8; i++) {
                            switch (i) {
                                case 1:
                                    Paragraph pmv = new Paragraph("船名:.", bold_chinese);
                                    pmv.setAlignment(Element.ALIGN_CENTER);
                                    cell1.addElement(pmv);
                                    cell1.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell1);
                                    break;
                                case 2:
                                    pname = new Paragraph(map.get("ship_name"), bold_chinese);
                                    pname.setAlignment(Element.ALIGN_LEFT);
                                    cell2.addElement(pname);
                                    cell2.setBorder(Rectangle.NO_BORDER);
                                    cell2.setColspan(2);
                                    table.addCell(cell2);
                                    break;
                                case 4:
                                    Paragraph pd = new Paragraph("类型:", bold_chinese);
                                    pd.setAlignment(Element.ALIGN_CENTER);
                                    cell3.addElement(pd);
                                    cell3.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell3);
                                    break;
                                case 5:
                                    Paragraph ptime = new Paragraph(map.get("check_type").equals("1")?"前测":(map.get("check_type").equals("2")?"中测":"后测"), bold_chinese);
                                    ptime.setAlignment(Element.ALIGN_LEFT);
                                    cell4.addElement(ptime);
                                    cell4.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell4);
                                    break;
                                case 6:
                                    Paragraph pd1 = new Paragraph("日期:", bold_chinese);
                                    pd1.setAlignment(Element.ALIGN_CENTER);
                                    cell5.addElement(pd1);
                                    cell5.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell5);
                                    break;
                                case 7:
                                    Paragraph ptime1 = new Paragraph(map.get("check_time"), bold_chinese);
                                    ptime1.setAlignment(Element.ALIGN_LEFT);
                                    cell6.addElement(ptime1);
                                    cell6.setBorder(Rectangle.NO_BORDER);
                                    cell6.setColspan(2);
                                    table.addCell(cell6);
                                    break;
                            }

                        }
                        doc.add(table);
                        break;
                    case 2:
                        table = new PdfPTable(8);
                        table.setWidthPercentage(100);
                        table.setSpacingBefore(5f);
                        cell = new PdfPCell(new Phrase("一、校正后平均水尺", bold_chinese));
                        cell.setColspan(7);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("单位：米", bold_chinese));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        doc.add(table);
                        break;

                    case 3:
                        table = createTable3();
                        doc.add(table);
                        break;
                    case 4:
                        Paragraph title = new Paragraph("拱陷校正后平均吃水D/M = (Fm + Am +6Mm)/8 = "+new DecimalFormat("0.000").format(Double.valueOf(map.get("jiaozhenghou_average"))), bold_chinese);// 设置标题
                        title.setAlignment(Element.ALIGN_LEFT);// 设置对齐方式
                        title.setLeading(1f);// 设置行间距
                        title.setSpacingBefore(5f);
                        doc.add(title);
                        break;
                    case 5:
                        title = new Paragraph("二、计算平均水尺对应的排水量", bold_chinese);// 设置标题
                        title.setAlignment(Element.ALIGN_LEFT);// 设置对齐方式
                        title.setSpacingBefore(25f);
                        title.setSpacingAfter(5f);
                        title.setLeading(1f);// 设置行间距
                        doc.add(title);
                        break;
                    case 6:
                        table = createTable2();
                        table.setSpacingBefore(5f);
                        table.setSpacingAfter(5f);
                        doc.add(table);
                        break;
                    case 7:
                        title = new Paragraph("三、排水量纵倾校正", bold_chinese);// 设置标题
                        title.setAlignment(Element.ALIGN_LEFT);// 设置对齐方式
                        title.setLeading(1f);// 设置行间距
                        title.setSpacingBefore(15f);
                        title.setSpacingAfter(5f);
                        doc.add(title);
                        break;
                    case 8:
                        table = createTable1();
                        table.setSpacingBefore(5f);
                        table.setSpacingAfter(10f);
                        doc.add(table);
                        break;
                    case 9:
                        title = new Paragraph("纵倾校正后排水量/载重量 = "+ new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_before"))), bold_chinese);// 设置标题
                        title.setAlignment(Element.ALIGN_LEFT);// 设置对齐方式
                        title.setLeading(1f);// 设置行间距
                        title.setSpacingBefore(15f);
                        title.setSpacingAfter(5f);
                        doc.add(title);
                        break;
                    case 10:
                        table = new PdfPTable(8);
                        table.setWidthPercentage(100);
                        cell1 = new PdfPCell();
                        cell3 = new PdfPCell();
                        cell4 = new PdfPCell();
                        cell5 = new PdfPCell();
                        for (int i = 1; i <= 8; i++) {
                            switch (i) {
                                case 1:
                                    Paragraph pmv = new Paragraph("四、船用物料计算", bold_chinese);
                                    pmv.setAlignment(Element.ALIGN_LEFT);
                                    cell1.addElement(pmv);
                                    cell1.setBorder(Rectangle.NO_BORDER);
                                    cell1.setColspan(2);
                                    table.addCell(cell1);
                                    break;
                                case 3:
                                    Paragraph pd = new Paragraph("单位：M/T", bold_chinese);
                                    pd.setAlignment(Element.ALIGN_CENTER);
                                    cell3.addElement(pd);
                                    cell3.setBorder(Rectangle.NO_BORDER);
                                    cell3.setColspan(2);
                                    cell3.setPaddingRight(15f);
                                    table.addCell(cell3);
                                    break;
                                case 5:
                                    Paragraph ptime = new Paragraph("六、定量备料计算", bold_chinese);
                                    ptime.setAlignment(Element.ALIGN_CENTER);
                                    cell4.addElement(ptime);
                                    cell4.setPaddingLeft(15f);
                                    cell4.setBorder(Rectangle.NO_BORDER);
                                    cell4.setColspan(2);
                                    table.addCell(cell4);
                                    break;
                                case 7:
                                    Paragraph pd1 = new Paragraph("单位：M/T", bold_chinese);
                                    pd1.setAlignment(Element.ALIGN_RIGHT);
                                    cell5.addElement(pd1);
                                    cell5.setBorder(Rectangle.NO_BORDER);
                                    cell5.setColspan(2);
                                    table.addCell(cell5);
                                    break;
                            }
                        }
                        doc.add(table);
                        break;
                    case 11:
                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setSpacingBefore(10f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("燃油", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("zy"))+Double.valueOf(map.get("qy")) + Double.valueOf(map.get("rhy")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("实际排水量", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);

                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("淡水", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("ds")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("减船用物料", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                        }

                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);

                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("压舱水", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("ycs")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("轻载及定量备料", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")) - Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")) - Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);


                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("合计", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);
                        break;
                    case 12:
                        table = new PdfPTable(8);
                        table.setWidthPercentage(100);
                        cell1 = new PdfPCell();
                        cell3 = new PdfPCell();
                        cell4 = new PdfPCell();
                        cell5 = new PdfPCell();
                        for (int i = 1; i <= 8; i++) {
                            switch (i) {
                                case 1:
                                    Paragraph pmv = new Paragraph("五、港水密度校正", bold_chinese);
                                    pmv.setAlignment(Element.ALIGN_LEFT);
                                    cell1.addElement(pmv);
                                    cell1.setBorder(Rectangle.NO_BORDER);
                                    cell1.setColspan(2);
                                    table.addCell(cell1);
                                    break;
                                case 3:
                                    Paragraph pd = new Paragraph("单位：M/T", bold_chinese);
                                    pd.setAlignment(Element.ALIGN_CENTER);
                                    cell3.addElement(pd);
                                    cell3.setBorder(Rectangle.NO_BORDER);
                                    cell3.setColspan(2);
                                    cell3.setPaddingRight(15f);
                                    table.addCell(cell3);
                                    break;
                                case 5:
                                    Paragraph ptime = new Paragraph("七、预计算货重", bold_chinese);
                                    ptime.setAlignment(Element.ALIGN_CENTER);
                                    cell4.addElement(ptime);
                                    cell4.setPaddingLeft(15f);
                                    cell4.setBorder(Rectangle.NO_BORDER);
                                    cell4.setColspan(2);
                                    table.addCell(cell4);
                                    break;
                                case 7:
                                    Paragraph pd1 = new Paragraph("单位：M/T", bold_chinese);
                                    pd1.setAlignment(Element.ALIGN_RIGHT);
                                    cell5.addElement(pd1);
                                    cell5.setBorder(Rectangle.NO_BORDER);
                                    cell5.setColspan(2);
                                    table.addCell(cell5);
                                    break;
                            }
                        }
                        doc.add(table);
                        break;
                    case 13:
                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setSpacingBefore(10f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("轻载排水量", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qz")))));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(""));
//                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("shijipaishuizaizhong")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(""));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qz")))));
                                break;
                        }cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("实际排水量", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")))));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qiancepaishiuliang")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qiancepaishuiliang")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")))));
                                break;
                        }

                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);

                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("表载密度", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0000").format(Double.valueOf(map.get("bzmd")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("减船用物料", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);

                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qiancechuanyongwuliao")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                        }

                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);

                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("实测密度", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0000").format(Double.valueOf(map.get("scmd")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("减轻载、定量备料", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("qz"))) + "+" + map.get("cs")));
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")) - Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")) - Double.valueOf(map.get("jianchuanyongwuliao")))));
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("qz")) + Double.valueOf(map.get("cs")))));
                                break;
                        }cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        doc.add(table);

                        table = new PdfPTable(9);
                        table.setWidthPercentage(100f);
                        table.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell = new PdfPCell(new Phrase("密度校正后排水量", bold_chinese));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("weight_after")))));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setPadding(2f);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        switch (type){
                            case "FRONT":
                                cell = new PdfPCell(new Phrase("卸载重量", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0").format(Double.valueOf(map.get("weight_package")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                doc.add(table);
                                break;
                            case "MID":
                                cell = new PdfPCell(new Phrase("减污油、污水排放", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianwuyoupaifang_mid")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setBorder(Rectangle.NO_BORDER);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase("卸载重量", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0").format(Double.valueOf(map.get("weight_package")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                doc.add(table);
                                break;
                            case "BACK":
                                cell = new PdfPCell(new Phrase("减污油、污水排放", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("jianwuyoupaifang_back")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(""));
                                cell.setBorder(Rectangle.NO_BORDER);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase("卸载重量", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0").format(Double.valueOf(map.get("weight_package")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                doc.add(table);
                                break;
                            case "CONSTANT":
                                cell = new PdfPCell(new Phrase("卸载重量", bold_chinese));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(new DecimalFormat("0").format(Double.valueOf(map.get("weight_package")))));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                                cell.setPadding(2f);
                                cell.setColspan(2);
                                table.addCell(cell);
                                doc.add(table);
                                break;
                        }
                        break;
                    case 14:
                        table = new PdfPTable(10);
                        table.setSpacingBefore(20f);
                        table.setWidthPercentage(100f);
                        cell = new PdfPCell(new Phrase("计算:", bold_chinese));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("", bold_chinese));
                        cell.setBorder(Rectangle.BOTTOM);
                        cell.setColspan(3);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("", bold_chinese));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("复核:", bold_chinese));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("", bold_chinese));
                        cell.setBorder(Rectangle.BOTTOM);
                        cell.setColspan(3);
                        table.addCell(cell);
                        doc.add(table);
                }
            }
            doc.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }


    public PdfPTable createTable1() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("assets/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font normal_chinese = new Font(bfChinese, 12);
        Font bold_chinese = new Font(bfChinese, 14, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPTable table1 = new PdfPTable(new float[]{2, 1, 1, 2, 1, 2});
        table.setWidthPercentage(100f);
        table1.setWidthPercentage(100f);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("漂心", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(map.get("LCF")+"米", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(1);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table1.addCell(cell);
        if (map.get("check_status").equals("1")){
            cell = new PdfPCell(new Phrase("纵倾力矩(M2-M1)/Dz =("+map.get("M2")+"-"+map.get("M1")+")/"+map.get("DZ")+"="+new DecimalFormat("0.0").format(Double.valueOf(map.get("zongqingliju"))), normal_chinese));
        }else{
            cell = new PdfPCell(new Phrase("纵倾力矩(M2-M1)/Dz", normal_chinese));
        }
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingLeft(10);
        cell.setColspan(4);
        table1.addCell(cell);
        String wording = "             =("+map.get("chishuicha_after") +"*"+map.get("LCF")+"*"+map.get("tpc")+"*100)/"+ new DecimalFormat("0.00").format(Double.valueOf(map.get("ship_length")))+ "+50*"+map.get("chishuicha_after")+"*"+ map.get("chishuicha_after")+"*" + new DecimalFormat("0.0").format(Double.valueOf(map.get("zongqingliju"))) +"/" + new DecimalFormat("0.00").format(Double.valueOf(map.get("ship_length")));
        Phrase jiaozhengphrase3 = new Phrase("             = " + new DecimalFormat("0.0").format(Double.valueOf(map.get("jiaozhi"))), normal_chinese);
        Phrase jiaozhengphrase2 = new Phrase(wording,normal_chinese);
        Phrase jiaozhengphrase1 = new Phrase("校正值Z=(Tc*LCF*TPC*100)/LBP+(50*Tc*Tc/LBP)*dM/dZ", normal_chinese);
        Paragraph pp = new Paragraph();
        pp.add(jiaozhengphrase1);
        if (map.get("check_status").equals("1")) {
            pp.add(spaceline);
            pp.add(jiaozhengphrase2);
            pp.add(spaceline);
            pp.add(jiaozhengphrase3);
        }else{
            pp.add(spaceline);
            pp.add(new Phrase(""));
            pp.add(spaceline);
            pp.add(new Phrase(""));
            pp.add(spaceline);
        }
        cell = new PdfPCell(pp);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(10);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setColspan(6);
        table1.addCell(cell);

        cell = new PdfPCell(table1);
        cell.setColspan(6);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(cell);
        return table;
    }

    public PdfPTable createTable2() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("assets/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font bold_chinese = new Font(bfChinese, 12, Font.BOLD);
        Font normal_chinese = new Font(bfChinese, 12);
        PdfPTable table = new PdfPTable(1);
        PdfPTable table1 = new PdfPTable(new float[]{2, 1, 1, 2, 1, 2});
        table.setWidthPercentage(100f);
        table1.setWidthPercentage(100f);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("接近平均水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(map.get("near_shuichi")+"米", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("接近平均水尺对应的排水量/载重量", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(3);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("near_weight")))+"M/T", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);


        cell = new PdfPCell(new Phrase("差额水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("chaeshuichi")))+"厘米", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("TPC", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("tpc")))+"MT/CM", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("差额重量", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("chaezhongliang")))+"M/T", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("实际水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("shijishuichi")))+"米", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("实际水尺对应的排水量/载重量", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(3);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.0").format(Double.valueOf(map.get("shijipaishuizaizhong")))+"M/T", normal_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(table1);
        cell.setColspan(6);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(cell);
        return table;
    }

    public PdfPTable createTable3() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("assets/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font bold_chinese = new Font(bfChinese, 12, Font.BOLD);
        Font normal_chinese = new Font(bfChinese, 12);
        PdfPTable table = new PdfPTable(1);
        PdfPTable table1 = new PdfPTable(new float[]{1.5f, 1, 1, 2f, 1.5f, 1});
        table.setWidthPercentage(100f);
        table1.setWidthPercentage(100f);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("部位", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("测视水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("平均水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("水尺标记与艏艉柱距离", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("艏艉校正", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("校正后水尺", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.addCell(cell);
        //加入嵌套表格
        PdfPTable celltable = new PdfPTable(2);
        cell = new PdfPCell(new Phrase("艏", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("左", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("右", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);

        cell = new PdfPCell(celltable);
        cell.setRowspan(2);
        cell.setBorderWidth(0);//设置表格的边框宽度为1
        cell.setPadding(0);//设置表格与上一个表格的填充为10
        table1.addCell(cell);

        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_frontLeft")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("average_front")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("biaojijuli_front")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("alter_front")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("afteralter_front")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_frontRight")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);

        celltable = new PdfPTable(2);
        cell = new PdfPCell(new Phrase("舯", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("左", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("右", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);
        cell = new PdfPCell(celltable);
        cell.setRowspan(2);
        cell.setBorderWidth(0);//设置表格的边框宽度为1
        cell.setPadding(0);//设置表格与上一个表格的填充为10
        table1.addCell(cell);

        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_midLeft")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("average_mid")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("biaojijuli_mid")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("alter_mid")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("afteralter_mid")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_midRight")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);

        celltable = new PdfPTable(2);
        cell = new PdfPCell(new Phrase("艉", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("左", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);
        cell = new PdfPCell(new Phrase("右", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        celltable.addCell(cell);
        cell = new PdfPCell(celltable);
        cell.setRowspan(2);
        cell.setBorderWidth(0);//设置表格的边框宽度为1
        cell.setPadding(0);//设置表格与上一个表格的填充为10
        table1.addCell(cell);


        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_backLeft")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("average_back")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("biaojijuli_back")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("alter_back")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("afteralter_back")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ceshishuichi_backRight")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        table1.addCell(cell);


        cell = new PdfPCell(new Phrase("校正前吃水差", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("chishuicha_before")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("船长LBP", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new DecimalFormat("0.00").format(Double.valueOf(map.get("ship_length")))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase("校正后吃水差", bold_chinese));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);
        cell = new PdfPCell(new Phrase(new Phrase(new DecimalFormat("0.000").format(Double.valueOf(map.get("chishuicha_after"))))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        table1.addCell(cell);

        cell = new PdfPCell(table1);
        cell.setColspan(6);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(cell);
        return table;
    }


    class CustomCell implements PdfPCellEvent {

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {

            // TODO Auto-generated method stub
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            //cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            //cb.setLineDash(0, 1, 1);
            cb.setLineWidth(0.5f);
//            cb.setLineDash(new float[]{5.0f, 5.0f}, -3);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();

        }
    }

}

