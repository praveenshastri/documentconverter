package com.springwiz.java.execrise.documentconverter.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springwiz.java.execrise.documentconverter.service.DocumentService;

@RestController
public class DocumentController {

	@Autowired
	DocumentService documentService;
	
	@RequestMapping("/")
	public String index(){
		return "Welcome to Document converter";
	}
	
	@RequestMapping("/upload")
	public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		System.out.println("file.getContentType() >"+file.getContentType());
		System.out.println("FileName > "+file.getOriginalFilename());	
		boolean status;
		String validationResult = validate(file); 
		if(validationResult.equals("success")){
			status = documentService.converXlsxToCSV(file);
			if(status == true)			
				return "File Uploaded Succesfully";
			else{
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);			
				return "Failed to Convert the file to csv. Invlaid file content";
			}
		}else{
			return validationResult;
		}
			
	}
	
	public String validate(MultipartFile uploadedFile) {
	       MultipartFile file = (MultipartFile) uploadedFile;
	       String result = "success";
	       if(file.isEmpty() || file.getSize()==0)
	    	   result = "Please select a file";
	       if(!(file.getContentType().toLowerCase().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	    		|| file.getContentType().toLowerCase().equals("application/octet-stream")   
	            || file.getContentType().toLowerCase().equals("application/vnd.ms-excel"))){
	    	   result = "Only xlsx file type is supported";
	       }
	       return result;
	   }
	
}
