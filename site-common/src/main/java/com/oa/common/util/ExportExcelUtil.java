package com.oa.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcelUtil {
	private final static Logger logger = LogManager.getLogger(ExportExcelUtil.class);

	public static String exportExcel(HttpServletRequest request, HttpServletResponse response, String templateFilePath,
			String outputName, Map beans) {
		InputStream is = null;
		OutputStream os = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			outputName = new String(outputName.getBytes("gbk"), "iso8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=" + outputName);
			XLSTransformer transformer = new XLSTransformer();
			is = new BufferedInputStream(new FileInputStream(request.getSession().getServletContext()
					.getRealPath(templateFilePath)));
			HSSFWorkbook workbook = (HSSFWorkbook) transformer.transformXLS(is, beans);
			os = response.getOutputStream();
			workbook.write(os);
		} catch (IOException e) {
			logger.error("exportExcel 创建io流 error", e);
			throw new RuntimeException("exportExcel 创建io流 error", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("exportExcel 关闭inputStream error", e);
					throw new RuntimeException("exportExcel 关闭inputStream error", e);
				}
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					logger.error("exportExcel 关闭outputStream error", e);
					throw new RuntimeException("exportExcel 关闭outputStream error", e);
				}
		}
		return null;
	}
}
