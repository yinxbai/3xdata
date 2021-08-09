package com.txdata.common.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 用于导出XLSX文件的视图
 *
 * @author lixg
 */
public class XLSXView implements View {

    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private Workbook workbook;
    private String filename;

    public XLSXView(Workbook workbook, String filename) throws UnsupportedEncodingException {
        this.workbook = workbook;
        this.filename = URLEncoder.encode(filename, "UTF-8");
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        workbook.write(response.getOutputStream());
    }
}
