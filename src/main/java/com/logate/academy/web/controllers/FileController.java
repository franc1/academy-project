package com.logate.academy.web.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.Document;
import com.logate.academy.services.FileService;
import com.logate.academy.web.dto.FileDTO;

@RestController
@RequestMapping("/api")
public class FileController {
	
	@Autowired
	private FileService fileService;

	
	@PostMapping(value = "/file/upload/article/{id}")
	public ResponseEntity<Document> uploadFileMultipart(
			@PathVariable("id") Integer articleId,
			@RequestParam("data") String fileData,
			@RequestParam("file") MultipartFile file) throws IOException
	{
		// convert JSON string to FileDTO
		FileDTO fileDTO = new ObjectMapper().readValue(fileData, FileDTO.class);
		
		Document doc = fileService.uploadDoc(fileDTO, file, articleId);
		return new ResponseEntity<>(doc, HttpStatus.OK);
	}
	
	@PutMapping(value = "/file/upload/encoded/{id}")
	public ResponseEntity<Document> uploadByEncodedString(
			@PathVariable("id") Integer id,
			@RequestBody FileDTO fileDTO) throws Exception
	{
		Document document = fileService.uploadEncoded(id, fileDTO);
		return new ResponseEntity<>(document, HttpStatus.OK);	
	}
	
	@GetMapping(value = "/file/download/{id}")
	public ResponseEntity<ByteArrayResource> downloadFile(
			@PathVariable("id") Integer id) throws IOException
	{
		ByteArrayResource fileBytes = fileService.download(id);
		return new ResponseEntity<>(fileBytes, HttpStatus.OK);
	}
}
