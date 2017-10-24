package com.springwiz.java.execrise.documentconverter.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

	public boolean converXlsxToCSV(MultipartFile file){
		try {
			doConvertXlsxToCsv(file);
		} catch (Exception e) {
			System.out.println("Error occurced while converting the document "+e.getMessage());
			return false;
		}
		return true;
	}
	
	private boolean doConvertXlsxToCsv(MultipartFile file) throws InvalidFormatException, IOException {
		File outputDir = new File("output");
		XSSFWorkbook workbook = null;
		if(!outputDir.exists())
			outputDir.mkdirs();		
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			int sheets = workbook.getNumberOfSheets();
			Cell cell;
			Row row;
			StringBuffer data;
			FileOutputStream fos;
			for (int i = 0; i < sheets; i++) {
				System.out.println("Sheet no > " + i + " Name > " + workbook.getSheetAt(i).getSheetName());
				data = new StringBuffer();
				Iterator<Row> rowIterator = workbook.getSheetAt(i).iterator();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
						switch (cell.getCellTypeEnum()) {
						case FORMULA:
							switch (cell.getCachedFormulaResultTypeEnum()) {
							case NUMERIC:
								data.append(getFormattedNumericValue(cell) + ",");
								break;
							default:
								break;
							}
							break;
						case BOOLEAN:
							data.append(cell.getBooleanCellValue() + ",");
							break;
						case NUMERIC:
							data.append(getFormattedNumericValue(cell) + ",");
							break;
						case STRING:
							data.append(cell.getStringCellValue() + ",");
							break;

						case BLANK:
							data.append(" " + ",");
							break;

						default:
							data.append(cell + ",");
						}
					}
					data.append("\n");					
				}
				fos = new FileOutputStream(new File(outputDir.getName()+File.separator+workbook.getSheetAt(i).getSheetName() + ".csv"));
				fos.write(data.toString().getBytes());
				fos.close();
			}
		} finally {
			if(workbook!=null)
				workbook.close();
		}
		return true;
	}

	private String getFormattedNumericValue(Cell cell) {
		String returnValue;
		if (DateUtil.isCellDateFormatted(cell)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			returnValue = dateFormat.format(cell.getDateCellValue());
		} else if (cell.getCellStyle().getDataFormatString().contains("%")) {
			Double value = cell.getNumericCellValue() * 100;
			returnValue = value.toString() + "%";
		} else {
			returnValue = "" + cell.getNumericCellValue();
		}
		return returnValue;
	}
}
