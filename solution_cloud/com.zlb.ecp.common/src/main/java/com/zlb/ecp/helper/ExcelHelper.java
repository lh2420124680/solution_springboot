package com.zlb.ecp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper<T> {
	public final int EXCEL_MAX_LINE = 65530;

	private void createFile(String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				throw new FileNotFoundException("directory " + file.getParent() + " not found");
			}
			file.createNewFile();
		}
	}

	public void writeDataToExcel(String fileName, String sheetName, String[] titleNames, String[] columnNames,
			List<Map<String, Object>> data) throws Exception {
		Workbook workbook = null;
		createFile(fileName);

		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(suffix)) {
			workbook = new HSSFWorkbook();
		} else if (".xlsx".equals(suffix)) {
			workbook = new XSSFWorkbook();
		} else {
			throw new IllegalArgumentException("unsupported filetype " + suffix);
		}
		OutputStream os = new FileOutputStream(fileName);

		int dataSize = data.size();
		int sheetSize = 1;
		Sheet sheet = null;
		if (dataSize > 65530) {
			if (dataSize % 65530 == 0) {
				sheetSize = dataSize / 65530;
			} else {
				sheetSize = dataSize / 65530 + 1;
			}
			List<Map<String, Object>> tempData = null;
			int lastIndex = 0;
			for (int i = 0; i < sheetSize; i++) {
				String tempSheetName = sheetName + (i + 1);
				sheet = workbook.createSheet(tempSheetName);

				lastIndex = (i + 1) * 65530;
				if (i == sheetSize - 1) {
					lastIndex = data.size();
				}
				tempData = data.subList(i * 65530, lastIndex);
				createSheet(workbook, sheet, titleNames, columnNames, tempData);
			}
		} else {
			sheet = workbook.createSheet(sheetName);
			createSheet(workbook, sheet, titleNames, columnNames, data);
		}
		workbook.write(os);
		os.close();
	}

	public void writeEntityToExcel(String fileName, String sheetName, String[] titleNames, String[] columnNames,
			List<T> data) throws Exception {
		Workbook workbook = null;
		createFile(fileName);

		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(suffix)) {
			workbook = new HSSFWorkbook();
		} else if (".xlsx".equals(suffix)) {
			workbook = new XSSFWorkbook();
		} else {
			throw new IllegalArgumentException("unsupported filetype " + suffix);
		}
		OutputStream os = new FileOutputStream(fileName);

		int dataSize = data.size();
		int sheetSize = 1;
		Sheet sheet = null;
		if (dataSize > 65530) {
			if (dataSize % 65530 == 0) {
				sheetSize = dataSize / 65530;
			} else {
				sheetSize = dataSize / 65530 + 1;
			}
			List<T> tempData = null;
			int lastIndex = 0;
			for (int i = 0; i < sheetSize; i++) {
				String tempSheetName = sheetName + (i + 1);
				sheet = workbook.createSheet(tempSheetName);

				lastIndex = (i + 1) * 65530;
				if (i == sheetSize - 1) {
					lastIndex = data.size();
				}
				tempData = data.subList(i * 65530, lastIndex);
				createEntitySheet(workbook, sheet, titleNames, columnNames, tempData);
			}
		} else {
			sheet = workbook.createSheet(sheetName);
			createEntitySheet(workbook, sheet, titleNames, columnNames, data);
		}
		workbook.write(os);
		os.close();
	}

	private void createSheet(Workbook wb, Sheet sheet, String[] titleNames, String[] columnNames,
			List<Map<String, Object>> data) {
		int rowIndex = 0;

		Row titleRow = sheet.createRow(rowIndex++);

		CellStyle titleStyle = wb.createCellStyle();
		Font titleFont = wb.createFont();
		titleFont.setFontName("宋体");
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBoldweight((short) 700);
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment((short) 6);
		titleStyle.setVerticalAlignment((short) 1);
		titleStyle.setWrapText(true);
		titleStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		for (int i = 0; i < titleNames.length; i++) {
			Cell titleCell = titleRow.createCell(i);
			titleCell.setCellValue(titleNames[i]);
			titleCell.setCellStyle(titleStyle);
		}
		CellStyle dataStyle = wb.createCellStyle();
		Font dataFont = wb.createFont();
		dataFont.setFontName("宋体");
		dataFont.setFontHeightInPoints((short) 10);
		dataFont.setBoldweight((short) 400);
		dataStyle.setFont(dataFont);
		dataStyle.setAlignment((short) 1);
		dataStyle.setVerticalAlignment((short) 1);
		dataStyle.setWrapText(true);
		for (Map<String, Object> tempData : data) {
			Row dataRow = sheet.createRow(rowIndex++);
			for (int i = 0; i < columnNames.length; i++) {
				Cell dataCell = dataRow.createCell(i);

				Object obj = tempData.get(columnNames[i]);
				if ((obj instanceof String)) {
					dataCell.setCellValue(obj + "");
				} else if ((obj instanceof Date)) {
					dataCell.setCellValue((Date) obj);
				} else if ((obj instanceof Calendar)) {
					dataCell.setCellValue((Calendar) obj);
				} else if ((obj instanceof Boolean)) {
					dataCell.setCellValue(((Boolean) obj).booleanValue());
				} else if ((obj instanceof Double)) {
					dataCell.setCellValue(((Double) obj).doubleValue());
				} else if ((obj instanceof Integer)) {
					dataCell.setCellValue(((Integer) obj).doubleValue());
				} else if ((obj instanceof Float)) {
					dataCell.setCellValue(((Float) obj).floatValue());
				} else if ((obj instanceof Long)) {
					dataCell.setCellValue(((Long) obj).longValue());
				} else if ((obj instanceof Byte)) {
					dataCell.setCellErrorValue(((Byte) obj).byteValue());
				} else if (obj == null) {
					dataCell.setCellValue("");
				} else {
					dataCell.setCellValue("未知数据类型！");
				}
				dataCell.setCellStyle(dataStyle);
			}
		}
	}

	private void createEntitySheet(Workbook wb, Sheet sheet, String[] titleNames, String[] columnNames, List<T> data) {
		int rowIndex = 0;

		Row titleRow = sheet.createRow(rowIndex++);

		CellStyle titleStyle = wb.createCellStyle();
		Font titleFont = wb.createFont();
		titleFont.setFontName("宋体");
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBoldweight((short) 700);
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment((short) 6);
		titleStyle.setVerticalAlignment((short) 1);
		titleStyle.setWrapText(true);
		titleStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		for (int i = 0; i < titleNames.length; i++) {
			Cell titleCell = titleRow.createCell(i);
			titleCell.setCellValue(titleNames[i]);
			titleCell.setCellStyle(titleStyle);
		}
		CellStyle dataStyle = wb.createCellStyle();
		Font dataFont = wb.createFont();
		dataFont.setFontName("宋体");
		dataFont.setFontHeightInPoints((short) 10);
		dataFont.setBoldweight((short) 400);
		dataStyle.setFont(dataFont);
		dataStyle.setAlignment((short) 1);
		dataStyle.setVerticalAlignment((short) 1);
		dataStyle.setWrapText(true);
		for (T tempData : data) {
			Row dataRow = sheet.createRow(rowIndex++);
			for (int i = 0; i < columnNames.length; i++) {
				Cell dataCell = dataRow.createCell(i);

				Object obj = getPropertyVal(tempData, columnNames[i]);
				if ((obj instanceof String)) {
					dataCell.setCellValue(obj + "");
				} else if ((obj instanceof Date)) {
					dataCell.setCellValue((Date) obj);
				} else if ((obj instanceof Calendar)) {
					dataCell.setCellValue((Calendar) obj);
				} else if ((obj instanceof Boolean)) {
					dataCell.setCellValue(((Boolean) obj).booleanValue());
				} else if ((obj instanceof Double)) {
					dataCell.setCellValue(((Double) obj).doubleValue());
				} else if ((obj instanceof Integer)) {
					dataCell.setCellValue(((Integer) obj).doubleValue());
				} else if ((obj instanceof Float)) {
					dataCell.setCellValue(((Float) obj).floatValue());
				} else if ((obj instanceof Long)) {
					dataCell.setCellValue(((Long) obj).longValue());
				} else if ((obj instanceof Byte)) {
					dataCell.setCellErrorValue(((Byte) obj).byteValue());
				} else if (obj == null) {
					dataCell.setCellValue("");
				} else {
					dataCell.setCellValue("未知数据类型！");
				}
				dataCell.setCellStyle(dataStyle);
			}
		}
	}

	private Object readCellValue(Cell c) {
		if (c == null) {
			return "";
		}
		switch (c.getCellType()) {
		case 3:
			return "";
		case 4:
			return Boolean.valueOf(c.getBooleanCellValue());
		case 5:
			return Byte.valueOf(c.getErrorCellValue());
		case 2:
			return c.getCellFormula();
		case 0:
			if (DateUtil.isCellDateFormatted(c)) {
				return c.getDateCellValue();
			}
			return Double.valueOf(c.getNumericCellValue());
		case 1:
			return c.getStringCellValue();
		}
		return "Unknown Cell Type:" + c.getCellType();
	}

	public List<Map<String, Object>> readDataFromExcel(String fileName, String[] columnNames, int startIndex)
			throws Exception {
		List<Map<String, Object>> data = new ArrayList();

		FileInputStream fis = new FileInputStream(fileName);

		Workbook workbook = WorkbookFactory.create(fis);

		Sheet sheet = null;
		int sheetNum = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			sheet = workbook.getSheetAt(i);
			data.addAll(readSheet(sheet, columnNames, startIndex));
		}
		return data;
	}

	private List<Map<String, Object>> readSheet(Sheet sheet, String[] columnNames, int startIndex) {
		List<Map<String, Object>> mapList = new ArrayList();

		Row row = null;
		int rowNum = sheet.getPhysicalNumberOfRows();
		for (int i = startIndex; i < rowNum; i++) {
			Map<String, Object> map = new HashMap();
			row = sheet.getRow(i);
			for (int j = 0; j < columnNames.length; j++) {
				map.put(columnNames[j], readCellValue(row.getCell(j)));
			}
			mapList.add(map);
		}
		return mapList;
	}

	public List<T> readEntityFromExcel(String fileName, String[] props, int startIndex, T t) throws Exception {
		List<T> data = new ArrayList();

		FileInputStream fis = new FileInputStream(fileName);

		Workbook workbook = WorkbookFactory.create(fis);

		Sheet sheet = null;
		int sheetNum = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			sheet = workbook.getSheetAt(i);

			data.addAll(readEntitySheet(sheet, props, startIndex, t));
		}
		return data;
	}

	private List<T> readEntitySheet(Sheet sheet, String[] props, int startIndex, T t) throws Exception {
		List<T> mapList = new ArrayList();

		Row row = null;
		int rowNum = sheet.getPhysicalNumberOfRows();
		for (int i = startIndex; i < rowNum; i++) {
			T obj = (T) t.getClass().newInstance();
			row = sheet.getRow(i);
			for (int j = 0; j < props.length; j++) {
				setPropertyVal(obj, props[j], readCellValue(row.getCell(j)));
			}
			mapList.add(obj);
		}
		return mapList;
	}

	private void setPropertyVal(Object obj, String fieldName, Object value) {
		Field field = getProperty(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Field getProperty(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (Exception localException) {
			}
		}
		return null;
	}

	public Object getPropertyVal(Object obj, String fieldName) {
		Field field = getProperty(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}
}
