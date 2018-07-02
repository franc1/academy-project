package com.logate.academy.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Document;
import com.logate.academy.repository.ArticleRepository;
import com.logate.academy.repository.DocumentRepository;
import com.logate.academy.web.dto.FileDTO;

@Service
public class FileService {
	
	@Value("${articles.file-path}")
	private String filePath;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	
	public Document uploadDoc(FileDTO fileData, MultipartFile file, Integer articleId) 
			throws IOException
	{
		Optional<Article> articleOpt = articleRepository.findById(articleId);
		if (!articleOpt.isPresent()) {
			return null;
		}
		
		// get file name 
		String fileName = file.getOriginalFilename();
		
		// save file to FS
		Path path = Paths.get(filePath + fileName);
		Files.write(path, file.getBytes());
		
		Article article = articleOpt.get();
		
		Document document = new Document();
		document.setTitle(fileData.getTitle());
		document.setFilePath(filePath + fileName);
		document.setDescription(fileData.getDescription());
		
		documentRepository.save(document);
		
		article.setDocument(document);
		articleRepository.save(article);
		
		return document;
	}
	
	@Transactional
	public Document uploadEncoded(Integer articleId, FileDTO fileDTO) 
			throws IOException
	{
		Optional<Article> artOpt = articleRepository.findById(articleId);
		if (!artOpt.isPresent()) {
			return null;
		}
		
		byte[] fileByteArray = Base64.decodeBase64(fileDTO.getFileEncoded());
		
		Path path = Paths.get(filePath + fileDTO.getTitle());
		Files.write(path, fileByteArray);
		
		Document document = new Document();
		//document.setDescription("Testni opis");
		document.setDescription(fileDTO.getDescription());
		document.setFilePath(filePath + fileDTO.getTitle());
		document.setTitle(fileDTO.getTitle());
		
		documentRepository.save(document);
		
		Article article = artOpt.get();
		article.setDocument(document);
		
		articleRepository.save(article);
		
		return document;
	}
	
	
	public ByteArrayResource download(Integer id) throws IOException
	{
		Optional<Document> document = documentRepository.findById(id);
		
		String fileName = document.get().getFilePath();
		Path path = Paths.get(fileName);
		
		byte[] fileBytes = Files.readAllBytes(path);
		return new ByteArrayResource(fileBytes);
	}

}
