package com.flatrental.domain.images;

import com.flatrental.api.FileUploadDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @HasAnyRole
    public FileUploadDTO uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api")
                .path("/file")
                .path("/download/")
                .path(fileName)
                .toUriString();

        return new FileUploadDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/upload-multiple")
    public List<FileUploadDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(fileName);
        String contentType = getContentType(resource, request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private String getContentType(Resource resource, HttpServletRequest request) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    @GetMapping("/download-multiple")
    public ResponseEntity<List<Resource>> downloadMultipleFiles(@RequestParam List<String> filenameList, HttpServletRequest request) {
        List<Resource> resources = filenameList.stream()
                .map(fileService::loadFileAsResource)
                .collect(Collectors.toList());
        String contentType = getCommonContentTypeWhenAllTheSameOrThrow(resources, request);
        return ResponseEntity.ok()
                //.contentType(MediaType.parseMediaType(contentType))
                .body(resources);
    }

    private String getCommonContentTypeWhenAllTheSameOrThrow(List<Resource> resources, HttpServletRequest request) {
        List<String> distinctContentTypes = resources.stream()
                .map(resource -> getContentType(resource, request))
                .distinct()
                .collect(Collectors.toList());

        if (distinctContentTypes.size() != 1) {
            throw new IllegalArgumentException("Tried to download files with distinct media types.");
        }

        return distinctContentTypes.get(0);
    }

}
