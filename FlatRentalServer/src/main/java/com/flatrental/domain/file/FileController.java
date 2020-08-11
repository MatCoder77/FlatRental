package com.flatrental.domain.file;

import com.flatrental.api.FileUploadDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.flatrental.domain.file.FileController.MAIN_RESOURCE;

@Api(tags = "Files")
@RestController
@RequestMapping(MAIN_RESOURCE)
@RequiredArgsConstructor
public class FileController {

    public static final String MAIN_RESOURCE = "/api/file";
    public static final String UPLOAD_RESOURCE = "/upload";
    public static final String UPLOAD_MULTIPLE_RESOURCE = "/upload-multiple";
    public static final String FILE_QUERY_PARAM = "file";
    public static final String FILES_QUERY_PARAM = "files";
    public static final String FILE_NAME_PATH_PARAM = "fileName";
    public static final String DOWNLOAD_FILE_RESOURCE = "/download/{" + FILE_NAME_PATH_PARAM + "}";

    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping(UPLOAD_RESOURCE)
    @HasAnyRole
    public FileUploadDTO uploadFile(@RequestParam(FILE_QUERY_PARAM) MultipartFile file) {
        String fileName = fileService.storeFile(file);
        URI fileDownloadUri = fileMapper.mapToDownloadUri(fileName);
        return new FileUploadDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping(UPLOAD_MULTIPLE_RESOURCE)
    @HasAnyRole
    public List<FileUploadDTO> uploadMultipleFiles(@RequestParam(FILES_QUERY_PARAM) MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping(DOWNLOAD_FILE_RESOURCE)
    public ResponseEntity<Resource> downloadFile(@PathVariable(FILE_NAME_PATH_PARAM) String fileName, HttpServletRequest request) {
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
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return contentType;
    }

}
