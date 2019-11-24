package io;

/**
 * used libraries commons.io , commons.math , poi
 */

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class Exporter {

   public static void createExcelOutput (String csv) {

        Workbook wb = new HSSFWorkbook();
        CreationHelper helper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        List<String> lines = null;
        try {
            lines = IOUtils.readLines(new StringReader(csv));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < lines.size(); i++) {
            String str[] = lines.get(i).split(",");
            Row row = sheet.createRow((short) i);
            for (int j = 0; j < str.length; j++) {
                row.createCell(j).setCellValue(helper.createRichTextString(str[j]));
            }
        }

        //Schedule.xls must be outputted to any Desktop
       File desktopDir = new File(System.getProperty("user.home"), "Desktop");

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(new File(desktopDir, "Schedule.xls"));
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}