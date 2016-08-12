package com.chengsi.weightcalc.utils.print;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-02-28.
 */
public class PrintPDF {


    public static Phrase spaceline = new Phrase(Chunk.NEWLINE);
    Map<String,String> map = new HashMap<>();

    public PrintPDF(Map<String,String> map){
        this.map = map;
    }

    public void print(){
    try {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("TAG", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);
//---------------------设置字体-------------------------
        BaseFont bfChinese = BaseFont.createFont("assets/fonts/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12, Font.BOLD);
        Font bold_underlined = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD | Font.UNDERLINE);
        Font bold_normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font bold_normal_P = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font small_black = new Font(Font.FontFamily.TIMES_ROMAN, 10);
        Font normal_chinese = new Font(bfChinese, 12);
        Font blue_chinese = new Font(bfChinese, 12, Font.NORMAL, new BaseColor(0, 0, 255));
        Font blue_chinese_big = new Font(bfChinese, 12, Font.NORMAL, new BaseColor(0, 0, 255));
        Font blue_english = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, new BaseColor(0, 0, 255));
        Rectangle rect = new Rectangle(PageSize.A4);
        Document doc = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(doc, output);
        float[] widths = {0.3f, 0.3f, 0.1f, 0.3f};
        float[] width1 = {0.3f, 0.1f, 0.3f, 0.3f};
        doc.open();


//---------------------设置标题-------------------------
        Paragraph title = new Paragraph("SHENGSI ENTRY-EXIT INSPECTION AND QUARANTINE BUREAU", bold);// 设置标题
        title.setAlignment(Element.ALIGN_CENTER);// 设置对齐方式
        title.setLeading(1f);// 设置行间距
        doc.add(title);
        doc.add(spaceline);
        title = new Paragraph("INSPECTION RECORD ON COMPUTATION OF", bold);// 设置标题
        title.setAlignment(Element.ALIGN_CENTER);// 设置对齐方式
        title.setLeading(1f);// 设置行间距
        doc.add(title);
        doc.add(spaceline);
        title = new Paragraph("WEIGHT BY CHECKING OF SHIP’S DRAFT", bold);// 设置标题
        title.setAlignment(Element.ALIGN_CENTER);// 设置对齐方式
        title.setLeading(1f);// 设置行间距
        doc.add(title);
        doc.add(spaceline);
        CustomCell border = new CustomCell();

        for (int row = 1; row < 27; row++) {
            switch (row) {
                case 1:
                    Paragraph shipname = new Paragraph("船名", blue_chinese);
                    doc.add(shipname);
                    break;
                case 2:
                    PdfPTable table = new PdfPTable(8);
                    table.setWidthPercentage(100);
                    PdfPCell cell1 = new PdfPCell();
                    PdfPCell cell2 = new PdfPCell();
                    PdfPCell cell3 = new PdfPCell();
                    PdfPCell cell4 = new PdfPCell();
                    for (int i = 1; i <= 8; i++) {
                        switch (i) {
                            case 1:
                                Paragraph pmv = new Paragraph("M.V:.", bold_normal_P);
                                pmv.setAlignment(Element.ALIGN_CENTER);
                                cell1.addElement(pmv);
                                cell1.setBorder(Rectangle.NO_BORDER);
                                table.addCell(cell1);
                                break;
                            case 2:
                                Paragraph pname = new Paragraph(map.get("ship_name"), bold_normal_P);
                                pname.setAlignment(Element.ALIGN_CENTER);
                                cell2.addElement(pname);
                                cell2.setBorder(Rectangle.NO_BORDER);
                                cell2.setCellEvent(border);
                                cell2.setColspan(3);
                                table.addCell(cell2);
                                break;
                            case 5:
                                Paragraph pd = new Paragraph("Date: ", bold_normal_P);
                                pd.setAlignment(Element.ALIGN_CENTER);
                                cell3.addElement(pd);
                                cell3.setBorder(Rectangle.NO_BORDER);
                                table.addCell(cell3);
                                break;
                            case 6:
                                Paragraph ptime =new Paragraph(map.get("check_time_f")+" - "+map.get("check_time_b"), bold_normal_P);
                                ptime.setAlignment(Element.ALIGN_CENTER);
                                cell4.addElement(ptime);
                                cell4.setBorder(Rectangle.NO_BORDER);
                                cell4.setCellEvent(border);
                                cell4.setColspan(3);
                                table.addCell(cell4);
                                break;
                        }
                    }
                    doc.add(table);
                    break;
                case 3:
                    PdfPTable header = new PdfPTable(widths);
                    header.setWidthPercentage(100);
                    header.setSpacingBefore(10f);
                    PdfPCell cell5 = new PdfPCell();
                    PdfPCell cell6 = new PdfPCell();
                    PdfPCell cell7 = new PdfPCell();
                    PdfPCell cell8 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Paragraph paragraph1 = new Paragraph("1.SHIP’S DRAFT", bold_normal);
                                cell5.addElement(paragraph1);
                                cell5.setBorder(Rectangle.NO_BORDER);
                                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                header.addCell(cell5);
                                break;
                            case 2:
                                cell6.addElement(new Paragraph("BEF.LOAD/DISCHARGE", bold_normal));
                                cell6.setBorder(Rectangle.NO_BORDER);
                                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                header.addCell(cell6);
                                break;
                            case 3:
                                cell7.addElement(new Paragraph("MID", bold_normal));
                                cell7.setBorder(Rectangle.NO_BORDER);
                                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                header.addCell(cell7);
                                break;
                            case 4:
                                cell8.addElement(new Paragraph("AFT.LOAD/ DISCHARGE", bold_normal));
                                cell8.setBorder(Rectangle.NO_BORDER);
                                cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                header.addCell(cell8);
                                break;
                        }
                    }
                    doc.add(header);
                    break;
                case 4:
                    PdfPTable table4 = new PdfPTable(widths);
                    table4.setWidthPercentage(100);
                    PdfPCell cell41 = new PdfPCell();
                    PdfPCell cell42 = new PdfPCell();
                    PdfPCell cell43 = new PdfPCell();
                    PdfPCell cell44 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row41 = new Chunk("艏校正后水尺",blue_chinese);
                                Chunk chunk_row42 = new Chunk("Fore.Corrected",normal);
                                Paragraph phrase_row41 = new Paragraph();
                                phrase_row41.add(chunk_row41);
                                phrase_row41.add(chunk_row42);
                                cell41.addElement(phrase_row41);
                                cell41.setBorder(Rectangle.NO_BORDER);
                                table4.addCell(cell41);
                                break;
                            case 2:
                                Chunk chunk_row43 = new Chunk(map.get("alter_f_qian") == null ? "000.000" : map.get("alter_f_qian"),normal);
                                Chunk chunk_row44 = new Chunk("M",normal);
                                Paragraph phrase_row42 = new Paragraph();
                                phrase_row42.add(chunk_row43);
                                phrase_row42.add(chunk_row44);
                                phrase_row42.setAlignment(Element.ALIGN_CENTER);
                                cell42.addElement(phrase_row42);
                                cell42.setBorder(Rectangle.NO_BORDER);
                                cell42.setCellEvent(border);
                                table4.addCell(cell42);
                                break;
                            case 3:
                                cell43.addElement(new Paragraph("        ", bold_normal));
                                cell43.setBorder(Rectangle.NO_BORDER);
                                cell43.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell43.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table4.addCell(cell43);
                                break;
                            case 4:
                                Chunk chunk_row45 = new Chunk(map.get("alter_f_hou") == null ? "000.000" : map.get("alter_f_hou"),normal);
                                Chunk chunk_row46 = new Chunk("M",normal);
                                Paragraph phrase_row43 = new Paragraph();
                                phrase_row43.add(chunk_row45);
                                phrase_row43.add(chunk_row46);
                                phrase_row43.setAlignment(Element.ALIGN_CENTER);
                                cell44.addElement(phrase_row43);
                                cell44.setBorder(Rectangle.NO_BORDER);
                                cell44.setCellEvent(border);
                                table4.addCell(cell44);
                                break;
                        }
                    }
                    table4.setSpacingBefore(10f);
                    doc.add(table4);
                    break;
                case 5:
                    PdfPTable table5 = new PdfPTable(widths);
                    table5.setWidthPercentage(100);
                    PdfPCell cell51 = new PdfPCell();
                    PdfPCell cell52 = new PdfPCell();
                    PdfPCell cell53 = new PdfPCell();
                    PdfPCell cell54 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row51 = new Chunk("艉校正后水尺",blue_chinese);
                                Chunk chunk_row52 = new Chunk("Aft.Corrected",normal);
                                Paragraph phrase_row51 = new Paragraph();
                                phrase_row51.add(chunk_row51);
                                phrase_row51.add(chunk_row52);
                                cell51.addElement(phrase_row51);
                                cell51.setBorder(Rectangle.NO_BORDER);
                                table5.addCell(cell51);
                                break;
                            case 2:
                                Chunk chunk_row53 = new Chunk(map.get("alter_h_qian") == null ? "000.000" : map.get("alter_h_qian"),normal);
                                Chunk chunk_row54 = new Chunk("M",normal);
                                Paragraph phrase_row52 = new Paragraph();
                                phrase_row52.add(chunk_row53);
                                phrase_row52.add(chunk_row54);
                                phrase_row52.setAlignment(Element.ALIGN_CENTER);
                                cell52.addElement(phrase_row52);
                                cell52.setBorder(Rectangle.NO_BORDER);
                                cell52.setCellEvent(border);
                                table5.addCell(cell52);
                                break;
                            case 3:
                                cell53.addElement(new Paragraph("        ", bold_normal));
                                cell53.setBorder(Rectangle.NO_BORDER);
                                cell53.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell53.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table5.addCell(cell53);
                                break;
                            case 4:
                                Chunk chunk_row55 = new Chunk(map.get("alter_h_hou") == null ? "000.000" : map.get("alter_h_hou"),normal);
                                Chunk chunk_row56 = new Chunk("M",normal);
                                Paragraph phrase_row53 = new Paragraph();
                                phrase_row53.add(chunk_row55);
                                phrase_row53.add(chunk_row56);
                                phrase_row53.setAlignment(Element.ALIGN_CENTER);
                                cell54.addElement(phrase_row53);
                                cell54.setBorder(Rectangle.NO_BORDER);
                                cell54.setCellEvent(border);
                                table5.addCell(cell54);
                                break;
                        }
                    }

                    table5.setSpacingBefore(10f);
                    doc.add(table5);
                    break;
                case 6:
                    PdfPTable table6 = new PdfPTable(widths);
                    table6.setWidthPercentage(100);
                    PdfPCell cell61 = new PdfPCell();
                    PdfPCell cell62 = new PdfPCell();
                    PdfPCell cell63 = new PdfPCell();
                    PdfPCell cell64 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row61 = new Chunk("舯校正后水尺",blue_chinese);
                                Chunk chunk_row62 = new Chunk("Mid. Averaged",normal);
                                Paragraph phrase_row61 = new Paragraph();
                                phrase_row61.add(chunk_row61);
                                phrase_row61.add(chunk_row62);
                                cell61.addElement(phrase_row61);
                                cell61.setBorder(Rectangle.NO_BORDER);
                                table6.addCell(cell61);
                                break;
                            case 2:
                                Chunk chunk_row63 = new Chunk(map.get("alter_m_qian") == null ? "000.000" : map.get("alter_m_qian"),normal);
                                Chunk chunk_row64 = new Chunk("M",normal);
                                Paragraph phrase_row62 = new Paragraph();
                                phrase_row62.add(chunk_row63);
                                phrase_row62.add(chunk_row64);
                                phrase_row62.setAlignment(Element.ALIGN_CENTER);
                                cell62.addElement(phrase_row62);
                                cell62.setBorder(Rectangle.NO_BORDER);
                                cell62.setCellEvent(border);
                                table6.addCell(cell62);
                                break;
                            case 3:
                                cell63.addElement(new Paragraph("        ", bold_normal));
                                cell63.setBorder(Rectangle.NO_BORDER);
                                cell63.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell63.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table6.addCell(cell63);
                                break;
                            case 4:
                                Chunk chunk_row65 = new Chunk(map.get("alter_m_hou") == null ? "000.000" : map.get("alter_m_hou"),normal);
                                Chunk chunk_row66 = new Chunk("M",normal);
                                Paragraph phrase_row63 = new Paragraph();
                                phrase_row63.add(chunk_row65);
                                phrase_row63.add(chunk_row66);
                                phrase_row63.setAlignment(Element.ALIGN_CENTER);
                                cell64.addElement(phrase_row63);
                                cell64.setBorder(Rectangle.NO_BORDER);
                                cell64.setCellEvent(border);
                                table6.addCell(cell64);
                                break;
                        }
                    }
                    table6.setSpacingBefore(10f);
                    doc.add(table6);
                    break;
                case 7:
                    Paragraph Draft = new Paragraph("Draft corrected For deformation", normal);
                    Draft.setPaddingTop(20);
                    Draft.setSpacingBefore(10f);
                    doc.add(Draft);
                    break;
//            case 8:
//                Paragraph deformation = new Paragraph("", normal);
//                deformation.setPaddingTop(20);
//                doc.add(deformation);
//                break;
                case 9:
                    PdfPTable table9 = new PdfPTable(widths);
                    table9.setWidthPercentage(100);
                    PdfPCell cell91 = new PdfPCell();
                    PdfPCell cell92 = new PdfPCell();
                    PdfPCell cell93 = new PdfPCell();
                    PdfPCell cell94 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row91 = new Chunk("拱陷校正后平均吃水",blue_chinese);
                                Chunk chunk_row92 = new Chunk("D/M",blue_english);
                                Paragraph phrase_row91 = new Paragraph();
                                phrase_row91.add(chunk_row91);
                                phrase_row91.add(chunk_row92);
                                cell91.addElement(phrase_row91);
                                cell91.setBorder(Rectangle.NO_BORDER);
                                table9.addCell(cell91);
                                break;
                            case 2:
                                Chunk chunk_row93 = new Chunk(map.get("jiaozhenghou_average_qian") == null ? "000.000" : map.get("jiaozhenghou_average_qian"),normal);
                                Chunk chunk_row94 = new Chunk("M",normal);
                                Paragraph phrase_row92 = new Paragraph();
                                phrase_row92.add(chunk_row93);
                                phrase_row92.add(chunk_row94);
                                phrase_row92.setAlignment(Element.ALIGN_CENTER);
                                cell92.addElement(phrase_row92);
                                cell92.setBorder(Rectangle.NO_BORDER);
                                cell92.setCellEvent(border);
                                table9.addCell(cell92);
                                break;
                            case 3:
                                cell93.addElement(new Paragraph("        ", bold_normal));
                                cell93.setBorder(Rectangle.NO_BORDER);
                                cell93.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell93.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table9.addCell(cell93);
                                break;
                            case 4:
                                Chunk chunk_row95 = new Chunk(map.get("jiaozhenghou_average_hou") == null ? "000.000" : map.get("jiaozhenghou_average_hou"),normal);
                                Chunk chunk_row96 = new Chunk("M",normal);
                                Paragraph phrase_row93 = new Paragraph();
                                phrase_row93.add(chunk_row95);
                                phrase_row93.add(chunk_row96);
                                phrase_row93.setAlignment(Element.ALIGN_CENTER);
                                cell94.addElement(phrase_row93);
                                cell94.setBorder(Rectangle.NO_BORDER);
                                cell94.setCellEvent(border);
                                table9.addCell(cell94);
                                break;
                        }
                    }
                    doc.add(table9);
                    break;
//            case 10:
//                Paragraph DENSITY = new Paragraph("2   DENSITY OF HARBOUR", bold_underlined);
//                DENSITY.setPaddingTop(20);
//                doc.add(DENSITY);
//                break;
                case 11:
                    PdfPTable table11 = new PdfPTable(width1);
                    table11.setWidthPercentage(100);
                    PdfPCell cell111 = new PdfPCell();
                    PdfPCell cell112 = new PdfPCell();
                    PdfPCell cell113 = new PdfPCell();
                    PdfPCell cell114 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row111 = new Chunk("2   DENSITY OF HARBOUR WATER AT BERT",bold_underlined);
                                Paragraph phrase_row111 = new Paragraph();
                                phrase_row111.add(chunk_row111);
                                cell111.addElement(phrase_row111);
                                cell111.setBorder(Rectangle.NO_BORDER);
                                table11.addCell(cell111);
                                break;
                            case 2:
                                Chunk space = new Chunk("   ",bold_underlined);
                                Phrase phrase_space = new Phrase();
                                phrase_space.add(space);
                                cell112.addElement(phrase_space);
                                cell112.setBorder(Rectangle.NO_BORDER);
                                cell112.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell112.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table11.addCell(cell112);
                                break;
                            case 3:
                                Chunk chunk_row112 = new Chunk(map.get("scmd_qian") == null ? "000.000" : map.get("scmd_qian"),normal);
                                Paragraph phrase_row112 = new Paragraph();
                                phrase_row112.add(chunk_row112);
                                phrase_row112.setAlignment(Element.ALIGN_CENTER);
                                cell113.addElement(phrase_row112);
                                cell113.setBorder(Rectangle.NO_BORDER);
                                table11.addCell(cell113);
                                break;
                            case 4:
                                Chunk chunk_row113 = new Chunk(map.get("scmd_hou") == null ? "000.000" : map.get("scmd_hou"),normal);
                                Paragraph phrase_row113 = new Paragraph();
                                phrase_row113.add(chunk_row113);
                                phrase_row113.setAlignment(Element.ALIGN_CENTER);
                                cell114.addElement(phrase_row113);
                                cell114.setBorder(Rectangle.NO_BORDER);
                                table11.addCell(cell114);
                                break;
                        }
                    }
                    table11.setSpacingBefore(10f);
                    doc.add(table11);
                    break;
                case 12:
                    Paragraph DISPLACEMENT = new Paragraph("3   DISPLACEMENT", bold_underlined);
                    DISPLACEMENT.setSpacingBefore(10f);
                    doc.add(DISPLACEMENT);
                    break;
                case 13:
                    Paragraph Corresponding = new Paragraph("Corresponding displacement", small_black);
                    Corresponding.setSpacingBefore(10f);
                    doc.add(Corresponding);
                    break;
                case 14:
                    PdfPTable table14 = new PdfPTable(widths);
                    table14.setWidthPercentage(100);
                    PdfPCell cell141 = new PdfPCell();
                    PdfPCell cell142 = new PdfPCell();
                    PdfPCell cell143 = new PdfPCell();
                    PdfPCell cell144 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row141 = new Chunk("实际水尺数据对应的排水量",blue_chinese);
                                Paragraph phrase_row141 = new Paragraph();
                                phrase_row141.add(chunk_row141);
                                cell141.addElement(phrase_row141);
                                cell141.setBorder(Rectangle.NO_BORDER);
                                table14.addCell(cell141);
                                break;
                            case 2:
                                Chunk chunk_row142 = new Chunk(map.get("shijipaishuizaizhong_qian") == null ? "000.000" : map.get("shijipaishuizaizhong_qian"),normal);
                                Chunk chunk_row143 = new Chunk("M/T",normal);
                                Paragraph phrase_row142 = new Paragraph();
                                phrase_row142.add(chunk_row142);
                                phrase_row142.add(chunk_row143);
                                phrase_row142.setAlignment(Element.ALIGN_CENTER);
                                cell142.addElement(phrase_row142);
                                cell142.setBorder(Rectangle.NO_BORDER);
                                cell142.setCellEvent(border);
                                table14.addCell(cell142);
                                break;
                            case 3:
                                cell143.addElement(new Paragraph("        ", bold_normal));
                                cell143.setBorder(Rectangle.NO_BORDER);
                                cell143.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell143.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table14.addCell(cell143);
                                break;
                            case 4:
                                Chunk chunk_row144 = new Chunk(map.get("shijipaishuizaizhong_hou") == null ? "000.000" : map.get("shijipaishuizaizhong_hou"),normal);
                                Chunk chunk_row145 = new Chunk("M/T",normal);
                                Paragraph phrase_row143 = new Paragraph();
                                phrase_row143.add(chunk_row144);
                                phrase_row143.add(chunk_row145);
                                phrase_row143.setAlignment(Element.ALIGN_CENTER);
                                cell144.addElement(phrase_row143);
                                cell144.setBorder(Rectangle.NO_BORDER);
                                cell144.setCellEvent(border);
                                table14.addCell(cell144);
                                break;
                        }
                    }
                    doc.add(table14);
                    break;
                case 15:
                    Paragraph trim = new Paragraph("After correction for the trim", small_black);
                    trim.setSpacingBefore(10f);
                    doc.add(trim);
                    break;
                case 16:
                    PdfPTable table16 = new PdfPTable(widths);
                    table16.setWidthPercentage(100);
                    PdfPCell cell161 = new PdfPCell();
                    PdfPCell cell162 = new PdfPCell();
                    PdfPCell cell163 = new PdfPCell();
                    PdfPCell cell164 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row161 = new Chunk("纵倾校正后排水量",blue_chinese);
                                Paragraph phrase_row161 = new Paragraph();
                                phrase_row161.add(chunk_row161);
                                cell161.addElement(phrase_row161);
                                cell161.setBorder(Rectangle.NO_BORDER);
                                table16.addCell(cell161);
                                break;
                            case 2:
                                Chunk chunk_row162 = new Chunk(map.get("alterpaishui_qian") == null ? "000.000" : map.get("alterpaishui_qian"),normal);
                                Chunk chunk_row163 = new Chunk("M/T",normal);
                                Paragraph phrase_row162 = new Paragraph();
                                phrase_row162.add(chunk_row162);
                                phrase_row162.add(chunk_row163);
                                phrase_row162.setAlignment(Element.ALIGN_CENTER);
                                cell162.addElement(phrase_row162);
                                cell162.setBorder(Rectangle.NO_BORDER);
                                cell162.setCellEvent(border);
                                table16.addCell(cell162);
                                break;
                            case 3:
                                cell163.addElement(new Paragraph("        ", bold_normal));
                                cell163.setBorder(Rectangle.NO_BORDER);
                                cell163.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell163.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table16.addCell(cell163);
                                break;
                            case 4:
                                Chunk chunk_row164 = new Chunk(map.get("alterpaishui_hou") == null ? "000.000" : map.get("alterpaishui_hou"),normal);
                                Chunk chunk_row165 = new Chunk("M/T",normal);
                                Paragraph phrase_row163 = new Paragraph();
                                phrase_row163.add(chunk_row164);
                                phrase_row163.add(chunk_row165);
                                phrase_row163.setAlignment(Element.ALIGN_CENTER);
                                cell164.addElement(phrase_row163);
                                cell164.setBorder(Rectangle.NO_BORDER);
                                cell164.setCellEvent(border);
                                table16.addCell(cell164);
                                break;
                        }
                    }
                    doc.add(table16);
                    break;
                case 17:
                    Paragraph correction = new Paragraph("After correction for density Of harbour water", small_black);
                    correction.setSpacingBefore(10f);
                    doc.add(correction);
                    break;
                case 18:
                    PdfPTable table18 = new PdfPTable(widths);
                    table18.setWidthPercentage(100);
                    PdfPCell cell181 = new PdfPCell();
                    PdfPCell cell182 = new PdfPCell();
                    PdfPCell cell183 = new PdfPCell();
                    PdfPCell cell184 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row181 = new Chunk("密度校正后排水量",blue_chinese);
                                Paragraph phrase_row181 = new Paragraph();
                                phrase_row181.add(chunk_row181);
                                cell181.addElement(phrase_row181);
                                cell181.setBorder(Rectangle.NO_BORDER);
                                table18.addCell(cell181);
                                break;
                            case 2:
                                Chunk chunk_row182 = new Chunk(map.get("weight_after_qian") == null ? "000.000" : map.get("weight_after_qian"),normal);
                                Chunk chunk_row183 = new Chunk("M/T",normal);
                                Paragraph phrase_row182 = new Paragraph();
                                phrase_row182.add(chunk_row182);
                                phrase_row182.add(chunk_row183);
                                phrase_row182.setAlignment(Element.ALIGN_CENTER);
                                cell182.addElement(phrase_row182);
                                cell182.setBorder(Rectangle.NO_BORDER);
                                cell182.setCellEvent(border);
                                table18.addCell(cell182);
                                break;
                            case 3:
                                cell183.addElement(new Paragraph("        ", bold_normal));
                                cell183.setBorder(Rectangle.NO_BORDER);
                                cell183.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell183.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table18.addCell(cell183);
                                break;
                            case 4:
                                Chunk chunk_row184 = new Chunk(map.get("weight_after_hou") == null ? "000.000" : map.get("weight_after_hou"),normal);
                                Chunk chunk_row185 = new Chunk("M/T",normal);
                                Paragraph phrase_row183 = new Paragraph();
                                phrase_row183.add(chunk_row184);
                                phrase_row183.add(chunk_row185);
                                phrase_row183.setAlignment(Element.ALIGN_CENTER);
                                cell184.addElement(phrase_row183);
                                cell184.setBorder(Rectangle.NO_BORDER);
                                cell184.setCellEvent(border);
                                table18.addCell(cell184);
                                break;
                        }
                    }
                    doc.add(table18);
                    break;
                case 19:
                    Paragraph QUANTITY = new Paragraph("4   QUANTITY OF SHIP’S ELEMENTS", bold_underlined);
                    QUANTITY.setSpacingBefore(10f);
                    doc.add(QUANTITY);
                    break;
                case 20:
                    PdfPTable table20 = new PdfPTable(widths);
                    table20.setWidthPercentage(100);
                    PdfPCell cell201 = new PdfPCell();
                    PdfPCell cell202 = new PdfPCell();
                    PdfPCell cell203 = new PdfPCell();
                    PdfPCell cell204 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row201 = new Chunk("Bunkers",normal);
                                Chunk chunk_row202 = new Chunk("油料",blue_chinese);
                                Paragraph phrase_row201 = new Paragraph();
                                phrase_row201.add(chunk_row201);
                                phrase_row201.add(chunk_row202);
                                cell201.addElement(phrase_row201);
                                cell201.setBorder(Rectangle.NO_BORDER);
                                table20.addCell(cell201);
                                break;
                            case 2:
                                Chunk chunk_row203 = new Chunk(map.get("oil_qian") == null ? "000.000" : map.get("oil_qian"),normal);
                                Chunk chunk_row204 = new Chunk("M",normal);
                                Paragraph phrase_row202 = new Paragraph();
                                phrase_row202.add(chunk_row203);
                                phrase_row202.add(chunk_row204);
                                phrase_row202.setAlignment(Element.ALIGN_CENTER);
                                cell202.addElement(phrase_row202);
                                cell202.setBorder(Rectangle.NO_BORDER);
                                cell202.setCellEvent(border);
                                table20.addCell(cell202);
                                break;
                            case 3:
                                cell203.addElement(new Paragraph("        ", bold_normal));
                                cell203.setBorder(Rectangle.NO_BORDER);
                                cell203.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell203.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table20.addCell(cell203);
                                break;
                            case 4:
                                Chunk chunk_row205 = new Chunk(map.get("oil_hou") == null ? "000.000" : map.get("oil_hou"),normal);
                                Chunk chunk_row206 = new Chunk("M",normal);
                                Paragraph phrase_row203 = new Paragraph();
                                phrase_row203.add(chunk_row205);
                                phrase_row203.add(chunk_row206);
                                phrase_row203.setAlignment(Element.ALIGN_CENTER);
                                cell204.addElement(phrase_row203);
                                cell204.setBorder(Rectangle.NO_BORDER);
                                cell204.setCellEvent(border);
                                table20.addCell(cell204);
                                break;
                        }
                    }
                    table20.setSpacingBefore(10f);
                    doc.add(table20);
                    break;
                case 21:
                    PdfPTable table21 = new PdfPTable(widths);
                    table21.setWidthPercentage(100);
                    PdfPCell cell211 = new PdfPCell();
                    PdfPCell cell212 = new PdfPCell();
                    PdfPCell cell213 = new PdfPCell();
                    PdfPCell cell214 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row211 = new Chunk("Fresh water",normal);
                                Chunk chunk_row212 = new Chunk("淡水",blue_chinese);
                                Paragraph phrase_row211 = new Paragraph();
                                phrase_row211.add(chunk_row211);
                                phrase_row211.add(chunk_row212);
                                cell211.addElement(phrase_row211);
                                cell211.setBorder(Rectangle.NO_BORDER);
                                table21.addCell(cell211);
                                break;
                            case 2:
                                Chunk chunk_row213 = new Chunk(map.get("ds_qian") == null ? "000.000" : map.get("ds_qian"),normal);
                                Chunk chunk_row214 = new Chunk("M",normal);
                                Paragraph phrase_row212 = new Paragraph();
                                phrase_row212.add(chunk_row213);
                                phrase_row212.add(chunk_row214);
                                phrase_row212.setAlignment(Element.ALIGN_CENTER);
                                cell212.addElement(phrase_row212);
                                cell212.setBorder(Rectangle.NO_BORDER);
                                cell212.setCellEvent(border);
                                table21.addCell(cell212);
                                break;
                            case 3:
                                cell213.addElement(new Paragraph("        ", bold_normal));
                                cell213.setBorder(Rectangle.NO_BORDER);
                                cell213.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell213.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table21.addCell(cell213);
                                break;
                            case 4:
                                Chunk chunk_row215 = new Chunk(map.get("ds_hou") == null ? "000.000" : map.get("ds_hou"),normal);
                                Chunk chunk_row216 = new Chunk("M",normal);
                                Paragraph phrase_row213 = new Paragraph();
                                phrase_row213.add(chunk_row215);
                                phrase_row213.add(chunk_row216);
                                phrase_row213.setAlignment(Element.ALIGN_CENTER);
                                cell214.addElement(phrase_row213);
                                cell214.setBorder(Rectangle.NO_BORDER);
                                cell214.setCellEvent(border);
                                table21.addCell(cell214);
                                break;
                        }
                    }
                    table21.setSpacingBefore(10f);
                    doc.add(table21);
                    break;
                case 22:
                    PdfPTable table22 = new PdfPTable(widths);
                    table22.setWidthPercentage(100);
                    PdfPCell cell221 = new PdfPCell();
                    PdfPCell cell222 = new PdfPCell();
                    PdfPCell cell223 = new PdfPCell();
                    PdfPCell cell224 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row221 = new Chunk("Ballast water",normal);
                                Chunk chunk_row222 = new Chunk("压舱水",blue_chinese);
                                Paragraph phrase_row221 = new Paragraph();
                                phrase_row221.add(chunk_row221);
                                phrase_row221.add(chunk_row222);
                                cell221.addElement(phrase_row221);
                                cell221.setBorder(Rectangle.NO_BORDER);
                                table22.addCell(cell221);
                                break;
                            case 2:
                                Chunk chunk_row223 = new Chunk(map.get("ycs_qian") == null ? "000.000" : map.get("ycs_qian"),normal);
                                Chunk chunk_row224 = new Chunk("M",normal);
                                Paragraph phrase_row222 = new Paragraph();
                                phrase_row222.add(chunk_row223);
                                phrase_row222.add(chunk_row224);
                                phrase_row222.setAlignment(Element.ALIGN_CENTER);
                                cell222.addElement(phrase_row222);
                                cell222.setBorder(Rectangle.NO_BORDER);
                                cell222.setCellEvent(border);
                                table22.addCell(cell222);
                                break;
                            case 3:
                                cell223.addElement(new Paragraph("        ", bold_normal));
                                cell223.setBorder(Rectangle.NO_BORDER);
                                cell223.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell223.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table22.addCell(cell223);
                                break;
                            case 4:
                                Chunk chunk_row225 = new Chunk(map.get("ycs_hou") == null ? "000.000" : map.get("ycs_hou"),normal);
                                Chunk chunk_row226 = new Chunk("M",normal);
                                Paragraph phrase_row223 = new Paragraph();
                                phrase_row223.add(chunk_row225);
                                phrase_row223.add(chunk_row226);
                                phrase_row223.setAlignment(Element.ALIGN_CENTER);
                                cell224.addElement(phrase_row223);
                                cell224.setBorder(Rectangle.NO_BORDER);
                                cell224.setCellEvent(border);
                                table22.addCell(cell224);
                                break;
                        }
                    }
                    table22.setSpacingBefore(10f);
                    doc.add(table22);
                    break;
                case 23:
                    PdfPTable table23 = new PdfPTable(widths);
                    table23.setWidthPercentage(100);
                    PdfPCell cell231 = new PdfPCell();
                    PdfPCell cell232 = new PdfPCell();
                    PdfPCell cell233 = new PdfPCell();
                    PdfPCell cell234 = new PdfPCell();
                    for (int i = 1; i <= 4; i++) {
                        switch (i) {
                            case 1:
                                Chunk chunk_row231 = new Chunk("other cargo",normal);
                                Chunk chunk_row232 = new Chunk("其它货物",blue_chinese);
                                Paragraph phrase_row231 = new Paragraph();
                                phrase_row231.add(chunk_row231);
                                phrase_row231.add(chunk_row232);
                                cell231.addElement(phrase_row231);
                                cell231.setBorder(Rectangle.NO_BORDER);
                                table23.addCell(cell231);
                                break;
                            case 2:
                                Chunk chunk_row233 = new Chunk(map.get("other_qian"),normal);
                                Chunk chunk_row234 = new Chunk("M",normal);
                                Paragraph phrase_row232 = new Paragraph();
                                phrase_row232.add(chunk_row233);
                                phrase_row232.add(chunk_row234);
                                phrase_row232.setAlignment(Element.ALIGN_CENTER);
                                cell232.addElement(phrase_row232);
                                cell232.setBorder(Rectangle.NO_BORDER);
                                cell232.setCellEvent(border);
                                table23.addCell(cell232);
                                break;
                            case 3:
                                cell233.addElement(new Paragraph("        ", bold_normal));
                                cell233.setBorder(Rectangle.NO_BORDER);
                                cell233.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell233.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table23.addCell(cell233);
                                break;
                            case 4:
                                Chunk chunk_row235 = new Chunk(map.get("other_qian"),normal);
                                Chunk chunk_row236 = new Chunk("M",normal);
                                Paragraph phrase_row233 = new Paragraph();
                                phrase_row233.add(chunk_row235);
                                phrase_row233.add(chunk_row236);
                                phrase_row233.setAlignment(Element.ALIGN_CENTER);
                                cell234.addElement(phrase_row233);
                                cell234.setBorder(Rectangle.NO_BORDER);
                                cell234.setCellEvent(border);
                                cell234.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell234.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table23.addCell(cell234);
                                break;
                        }
                    }
                    table23.setSpacingBefore(10f);
                    doc.add(table23);
                    break;
                case 24:
                    PdfPTable table24 = new PdfPTable(new float[]{0.7f,0.3f});
                    table24.setWidthPercentage(100);
                    PdfPCell cell241 = new PdfPCell();
                    PdfPCell cell242 = new PdfPCell();
                    for(int i=1;i<3;i++){
                        switch (i){
                            case 1:
                                Chunk chunk_row241 = new Chunk("5   WEIGHT OF CARGO LOAD/ DISCHARGED",bold_underlined);
                                Chunk chunk_row242 = new Chunk("货物卸载量",blue_chinese);
                                Phrase phrase_row241 = new Phrase();
                                phrase_row241.add(chunk_row241);
                                phrase_row241.add(chunk_row242);
                                cell241.addElement(phrase_row241);
                                cell241.setBorder(Rectangle.NO_BORDER);
                                table24.addCell(cell241);
                                break;
                            case 2:
                                Chunk chunk_row243 = new Chunk("17.06",normal);
                                Chunk chunk_row244 = new Chunk("M/T",normal);
                                Paragraph phrase_row242 = new Paragraph();
                                phrase_row242.add(chunk_row243);
                                phrase_row242.add(chunk_row244);
                                phrase_row242.setAlignment(Element.ALIGN_CENTER);
                                cell242.addElement(phrase_row242);
                                cell242.setBorder(Rectangle.NO_BORDER);
                                cell242.setCellEvent(border);
                                table24.addCell(cell242);
                                break;
                        }
                    }
                    table24.setSpacingBefore(10f);
                    doc.add(table24);
                    break;
                case 25:
                    PdfPTable table25 = new PdfPTable(new float[]{0.15f,0.85f});
                    table25.setWidthPercentage(100);
                    PdfPCell cell251 = new PdfPCell();
                    PdfPCell cell252 = new PdfPCell();
                    for(int i=1;i<3;i++){
                        switch (i){
                            case 1:
                                Chunk chunk_row251 = new Chunk("REMARK",normal);
                                Paragraph phrase_row251 = new Paragraph();
                                phrase_row251.add(chunk_row251);
                                phrase_row251.setAlignment(Element.ALIGN_CENTER);
                                cell251.setBorder(Rectangle.NO_BORDER);
                                cell251.addElement(phrase_row251);
                                table25.addCell(cell251);
                                break;
                            case 2:
                                Chunk chunk_row253 = new Chunk("*****",normal);
                                Phrase phrase_row252 = new Phrase();
                                phrase_row252.add(chunk_row253);
                                cell252.addElement(phrase_row252);
                                cell252.setBorder(Rectangle.NO_BORDER);
                                cell252.setCellEvent(border);
                                cell252.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell252.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table25.addCell(cell252);
                                break;
                        }
                    }
                    table25.setSpacingBefore(40f);
                    doc.add(table25);
                    break;
                case 26:
                    PdfPTable table26 = new PdfPTable(new float[]{0.15f,0.3f,0.25f,0.3f});
                    table26.setWidthPercentage(100);
                    PdfPCell cell261 = new PdfPCell();
                    PdfPCell cell262 = new PdfPCell();
                    PdfPCell cell263 = new PdfPCell();
                    PdfPCell cell264 = new PdfPCell();
                    for(int i=1;i<5;i++){
                        switch (i){
                            case 1:
                                Chunk chunk_row261 = new Chunk("Surveyor:",normal);
                                Paragraph phrase_row261 = new Paragraph();
                                phrase_row261.add(chunk_row261);
                                phrase_row261.setAlignment(Element.ALIGN_CENTER);
                                cell261.addElement(phrase_row261);
                                cell261.setBorder(Rectangle.NO_BORDER);
                                table26.addCell(cell261);
                                break;
                            case 2:
                                Chunk chunk_row262 = new Chunk(" ",normal);
                                Phrase phrase_row262 = new Phrase();
                                phrase_row262.add(chunk_row262);
                                cell262.addElement(phrase_row262);
                                cell262.setBorder(Rectangle.NO_BORDER);
                                cell262.setCellEvent(border);
                                table26.addCell(cell262);
                                break;
                            case 3:
                                Chunk chunk_row263 = new Chunk("Surveyor on duty:",normal);
                                Paragraph phrase_row263 = new Paragraph();
                                phrase_row263.add(chunk_row263);
                                phrase_row263.setAlignment(Element.ALIGN_CENTER);
                                cell263.addElement(phrase_row263);
                                cell263.setBorder(Rectangle.NO_BORDER);
                                table26.addCell(cell263);
                                break;
                            case 4:
                                Chunk chunk_row264 = new Chunk("",normal);
                                Phrase phrase_row264 = new Phrase();
                                phrase_row264.add(chunk_row264);
                                cell264.addElement(phrase_row264);
                                cell264.setBorder(Rectangle.NO_BORDER);
                                cell264.setCellEvent(border);
                                cell264.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell264.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table26.addCell(cell264);
                                break;
                        }
                    }
                    table26.setSpacingBefore(10f);
                    doc.add(table26);
                    break;
            }
        }


        doc.close();
    } catch (Exception de) {
        de.printStackTrace();
    }
    }
    class CustomCell implements PdfPCellEvent {
        /*public void cellLayout(PdfPCell cell, Rectangle rect,
        PdfContentByte[] canvas) {
        PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
        cb.setLineDash(new float[] { 3.0f, 3.0f }, 0);
        cb.stroke();
        }*/
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {

            // TODO Auto-generated method stub
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            //cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            //cb.setLineDash(0, 1, 1);
            cb.setLineWidth(0.5f);
            cb.setLineDash(new float[]{5.0f, 5.0f}, -3);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();

        }
    }



//    public void createpdf() throws FileNotFoundException, DocumentException {
//        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES), "pdfdemo");
//        if (!pdfFolder.exists()) {
//            pdfFolder.mkdir();
//            Log.i("TAG", "Pdf Directory created");
//        }
//
////Create time stamp
//        Date date = new Date();
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//
//        File myFile = new File(pdfFolder + timeStamp + ".pdf");
//
//        OutputStream output = new FileOutputStream(myFile);
//
//        //Step 1
//        Document document = new Document();
//        Font bold_underlined = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD | Font.UNDERLINE);
//        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);
//        Phrase phrase = new Phrase();
//        Chunk chunk1 = new Chunk("Hello!", bold_underlined);
//        Chunk chunk2 = new Chunk("How are you?", normal);
//        phrase.add(chunk1);
//        phrase.add(Chunk.NEWLINE);
//        phrase.add(chunk2);
//
//        document.add(phrase);
////
////        //Step 2
////        PdfWriter.getInstance(document, output);
////
////        //Step 3
////        document.open();
////
////        //Step 4 Add content
////        document.add(new Paragraph("hello world"));
////
////        //Step 5: Close the document
//        document.close();
//
//    }

}
