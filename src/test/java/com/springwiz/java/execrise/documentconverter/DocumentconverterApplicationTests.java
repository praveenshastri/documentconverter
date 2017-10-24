package com.springwiz.java.execrise.documentconverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class DocumentconverterApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private WebApplicationContext webContext;
	
	@Test
	public void TestIndex(){
		String body = this.restTemplate.getForObject("/", String.class);
		assertThat(body).isEqualTo("Welcome to Document converter");		
	}
	
	
	@Test
	public void TestValidFileUload() throws Exception{
		MockMultipartFile xlsxFile = new MockMultipartFile("file", "sample.xlsx","application/vnd.ms-excel",getClass().getClassLoader().getResourceAsStream("seeded_excel_for_java_test.xlsx"));
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(xlsxFile)).andExpect(status().is(200));		
	}

	@Test
	public void TestInvalidFileUload() throws Exception{
		MockMultipartFile xlsxFile = new MockMultipartFile("file","sample.txt","text/plain","dummy Content".getBytes());
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(xlsxFile)).andExpect(status().is(200)).andExpect(content().string("Only xlsx file type is supported"));		
	}

	@Test
	public void TestEmptyFileUload() throws Exception{
		MockMultipartFile xlsxFile = new MockMultipartFile("file", "sample.xlsx","application/vnd.ms-excel"," ".getBytes());
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(xlsxFile)).andExpect(status().is(500)).andExpect(content().string("Failed to Convert the file to csv. Invlaid file content"));		
	}
	
	
}
