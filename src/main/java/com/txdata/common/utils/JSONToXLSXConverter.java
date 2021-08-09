package com.txdata.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.util.Map;

/**
 * JSON转XLSX工具类
 *
 * @author lixg
 */
public class JSONToXLSXConverter {

    private static final Color DEFAULT_HEADER_ROW_BACKGROUND_COLOR = new Color(242, 242, 242);

    private static final String DEFAULT_FONT_NAME = "宋体";

    private static final short DEFAULT_FONT_SIZE = 9;

    private static final float DEFAULT_ROW_HEIGHT = 20;

    public static Workbook convert(Object json) {
        return convert(json, true);
    }

    public static Workbook convert(Object json, boolean prettyFormat) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        fill(workbook.createSheet(), json, prettyFormat);
        return workbook;
    }

    public static void fill(XSSFSheet sheet, Object json) {
        fill(sheet, json, true);
    }

    public static void fill(XSSFSheet sheet, Object json, boolean prettyFormat) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        fillHeader(sheet, 0, 0, json);
        fillBody(sheet, sheet.getLastRowNum() + 1, 0, json);
        if (prettyFormat && sheet.getLastRowNum() > 0) {
            int headerRowspan = getHeaderRowspan(json);
            for (int i = 0; i < headerRowspan; i++) {
                Row row = sheet.getRow(i);
                row.setHeightInPoints(DEFAULT_ROW_HEIGHT);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellStyle(createDefaultHeaderCellStyle(workbook));
                    }
                }
            }
            for (int i = headerRowspan; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                row.setHeightInPoints(DEFAULT_ROW_HEIGHT);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellStyle(createDefaultBodyCellStyle(workbook));
                    }
                }
            }
            for (int i = 0; i < getColspan(json); i++) {
                sheet.autoSizeColumn(i);
            }
            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress region = sheet.getMergedRegion(i);
                RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, workbook);
                RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, workbook);
                RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, workbook);
                RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, workbook);
            }
        }
    }

    private static XSSFCellStyle createDefaultHeaderCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorder(cellStyle);
        cellStyle.setFillForegroundColor(new XSSFColor(DEFAULT_HEADER_ROW_BACKGROUND_COLOR));
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFont(createDefaultHeaderFont(workbook));
        return cellStyle;
    }

    private static XSSFCellStyle createDefaultBodyCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorder(cellStyle);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFont(createDefaultBodyFont(workbook));
        return cellStyle;
    }

    private static void setBorder(XSSFCellStyle cellStyle) {
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    }

    private static XSSFFont createDefaultHeaderFont(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(DEFAULT_FONT_NAME);
        font.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        return font;
    }

    private static XSSFFont createDefaultBodyFont(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(DEFAULT_FONT_NAME);
        font.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        return font;
    }

    /**
     * 使用JSON填充表头
     *
     * @param sheet 表格
     * @param rownum 起始行
     * @param colnum 起始列
     * @param json JSON
     */
    public static void fillHeader(XSSFSheet sheet, int rownum, int colnum, Object json) {
        if (json instanceof JSONObject) {
            fillHeader(sheet, rownum, colnum, (JSONObject) json);
        } else if (json instanceof JSONArray) {
            fillHeader(sheet, rownum, colnum, (JSONArray) json);
        }
    }

    /**
     * 用JSON对象填充表头
     *
     * @param sheet 表格
     * @param rownum 起始行
     * @param colnum 起始列
     * @param jsonObject JSON对象
     */
    private static void fillHeader(XSSFSheet sheet, int rownum, int colnum, JSONObject jsonObject) {
        int currentCol = colnum;
        int rowspan = getHeaderRowspan(jsonObject);
        for (Map.Entry<String, Object> kvp : jsonObject.entrySet()) {
            fillHeader(sheet, rownum, currentCol, kvp);
            if (rowspan > 1) {
                if (getHeaderRowspan(kvp) <= 1) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum + rowspan - 1, currentCol, currentCol));
                }
            }
            currentCol += getColspan(kvp.getValue());
        }
    }

    /**
     * 用键值对填充表头
     *
     * @param sheet 表格
     * @param rownum 起始行
     * @param colnum 起始列
     * @param kvp 键值对
     */
    private static void fillHeader(XSSFSheet sheet, int rownum, int colnum, Map.Entry<String, Object> kvp) {
        String key = kvp.getKey();
        Object val = kvp.getValue();
        if (!key.startsWith("#")) {
            // 如果该键值对的键不以#号开头，那么将该键填充到表格，从当前行和当前列开始
            fillCell(sheet, rownum, colnum, key);
        }
        if (val instanceof JSONObject || val instanceof JSONArray) {
            if (key.startsWith("#")) {
                // 如果该键值对的键以#号开头，那么将该键值对的值填充到表格，从当前行和当前列开始
                fillHeader(sheet, rownum, colnum, val);
            } else {
                int colspan = getColspan(val);
                if (colspan > 1) {
                    // 跨列合并
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, colnum, colnum + colspan - 1));
                }
                // 如果该键值对的键不以#号开头，那么将该键值对的值填充到表格，从下一行和当前列开始
                fillHeader(sheet, rownum + 1, colnum, val);
            }
        }
    }

    /**
     * 用JSON数组填充表头
     *
     * @param sheet 表格
     * @param rownum 起始行
     * @param colnum 起始列
     * @param jsonArray JSON数组
     */
    private static void fillHeader(XSSFSheet sheet, int rownum, int colnum, JSONArray jsonArray) {
        Object max = null;
        int maxRowspan = 0;
        for (Object json : jsonArray) {
            int rowspan = getHeaderRowspan(json);
            if (rowspan > maxRowspan) {
                max = json;
                maxRowspan = rowspan;
            }
        }
        if (max != null) {
            fillHeader(sheet, rownum, colnum, max);
        }
    }

    /**
     * 用JSON填充表格主体，从指定的行号和列号开始
     *
     * @param sheet 表格
     * @param rownum 行号
     * @param colnum 列号
     * @param json JSON
     */
    public static void fillBody(XSSFSheet sheet, int rownum, int colnum, Object json) {
        if (json instanceof JSONObject) {
            fillBody(sheet, rownum, colnum, (JSONObject) json);
        } else if (json instanceof JSONArray) {
            fillBody(sheet, rownum, colnum, (JSONArray) json);
        } else {
            fillCell(sheet, rownum, colnum, json);
        }
    }

    /**
     * 用JSON对象填充表格主体，从指定的行号和列号开始
     *
     * @param sheet 表格
     * @param rownum 行号
     * @param colnum 列号
     * @param jsonObject JSON对象
     */
    private static void fillBody(XSSFSheet sheet, int rownum, int colnum, JSONObject jsonObject) {
        int currentCol = colnum;
        int rowspan = getBodyRowspan(jsonObject);
        for (Object json : jsonObject.values()) {
            fillBody(sheet, rownum, currentCol, json);
            if (rowspan > 1) {
                if (getBodyRowspan(json) == 1) {
                    sheet.addMergedRegion(new CellRangeAddress(rownum, rownum + rowspan - 1, currentCol, currentCol));
                }
            }
            currentCol += getColspan(json);
        }
    }

    /**
     * 用JSON数组填充表格主体，从指定的行号和列号开始
     *
     * @param sheet 表格
     * @param rownum 行号
     * @param colnum 列号
     * @param jsonArray JSON数组
     */
    private static void fillBody(XSSFSheet sheet, int rownum, int colnum, JSONArray jsonArray) {
        int currentRow = rownum;
        int colspan = getColspan(jsonArray);
        for (Object json : jsonArray) {
            fillBody(sheet, currentRow, colnum, json);
            if (colspan > 1) {
                if (getColspan(json) == 1) {
                    sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, colnum, colnum + colspan - 1));
                }
            }
            currentRow += getBodyRowspan(json);
        }
    }

    private static void fillCell(XSSFSheet sheet, int rownum, int colnum, Object val) {
        Row row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
        }
        if (val != null) {
            cell.setCellValue(val.toString());
        }
    }

    /**
     * 获取内容在表头中的rowspan
     *
     * @param json 内容
     *
     * @return 内容在表头中的rowspan
     */
    public static int getHeaderRowspan(Object json) {
        if (json instanceof JSONObject) {
            return getHeaderRowspan((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return getHeaderRowspan((JSONArray) json);
        } else {
            return 0;
        }
    }

    /**
     * JSON对象在表头中的rowspan是其各键值对在表头中的rowspan中的最大值
     *
     * @param jsonObject JSON对象
     *
     * @return JSON对象在表头中的rowspan
     */
    private static int getHeaderRowspan(JSONObject jsonObject) {
        int maxRowspan = 0;
        for (Map.Entry<String, Object> kvp : jsonObject.entrySet()) {
            maxRowspan = Math.max(maxRowspan, getHeaderRowspan(kvp));
        }
        return maxRowspan;
    }

    /**
     * 键值对在表头中的rowspan是其值在表头中的rowspan
     * 如果该键值对的键不以#开头，rowspan + 1
     *
     * @param kvp 键值对
     *
     * @return 键值对在表头中的rowspan
     */
    private static int getHeaderRowspan(Map.Entry<String, Object> kvp) {
        int rowspan = 0;
        Object val = kvp.getValue();
        if (val instanceof JSONObject || val instanceof JSONArray) {
            rowspan += getHeaderRowspan(val);
        }
        if (!kvp.getKey().startsWith("#")) {
            rowspan++;
        }
        return rowspan;
    }

    /**
     * JSON数组在表头中的rowspan是其各元素在表头中的rowspan中的最大值
     *
     * @param jsonArray JSON数组
     *
     * @return JSON数组在表头中的rowspan
     */
    private static int getHeaderRowspan(JSONArray jsonArray) {
        int rowspan = 0;
        for (Object json : jsonArray) {
            rowspan = Math.max(rowspan, getHeaderRowspan(json));
        }
        return rowspan;
    }

    /**
     * 获取内容在表格主体中的rowspan
     *
     * @param json 内容
     *
     * @return 内容在表格主体中的rowspan
     */
    public static int getBodyRowspan(Object json) {
        if (json instanceof JSONObject) {
            return getBodyRowspan((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return getBodyRowspan((JSONArray) json);
        } else {
            return 1;
        }
    }

    /**
     * JSON对象在表格主体中的rowspan为其各属性值在表格主体中的rowspan的最大值
     *
     * @param jsonObject JSON对象
     *
     * @return JSON对象在表格主体中的rowspan
     */
    private static int getBodyRowspan(JSONObject jsonObject) {
        int rowspan = 0;
        for (Object json : jsonObject.values()) {
            rowspan = Math.max(rowspan, getBodyRowspan(json));
        }
        return rowspan;
    }

    /**
     * JSON数组在表格主体中的rowspan为其各元素在表格主体中的rowspan之和
     *
     * @param jsonArray JSON数组
     *
     * @return JSON数组在表格主体中的rowspan
     */
    private static int getBodyRowspan(JSONArray jsonArray) {
        int rowspan = 0;
        for (Object json : jsonArray) {
            rowspan += getBodyRowspan(json);
        }
        return rowspan;
    }

    /**
     * 获取内容的colspan
     *
     * @param json 内容
     *
     * @return 内容的colspan
     */
    public static int getColspan(Object json) {
        if (json instanceof JSONObject) {
            return getColspan((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return getColspan((JSONArray) json);
        } else {
            return 1;
        }
    }

    /**
     * JSON对象的colspan为其各属性值的colspan之和
     *
     * @param jsonObject JSON对象
     *
     * @return JSON对象的colspan
     */
    private static int getColspan(JSONObject jsonObject) {
        int colspan = 0;
        for (Object json : jsonObject.values()) {
            colspan += getColspan(json);
        }
        return colspan;
    }

    /**
     * JSON数组的colspan为其各元素的colspan的最大值
     *
     * @param jsonArray JSON数组
     *
     * @return JSON数组的colspan
     */
    private static int getColspan(JSONArray jsonArray) {
        int colspan = 0;
        for (Object json : jsonArray) {
            colspan = Math.max(colspan, getColspan(json));
        }
        return colspan;
    }
}
