package com.flatrental.domain.file;

import com.flatrental.api.file.FileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileMapper {

    public FileDTO mapToFileDTO(File file) {
        return new FileDTO(file.getFilename());
    }

    public List<File> mapToFiles(Collection<FileDTO> fileDTOs) {
        return Optional.ofNullable(fileDTOs)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::mapToFiles)
                .collect(Collectors.toList());
    }

    public File mapToFiles(FileDTO fileDTO) {
        return new File(fileDTO.getFilename());
    }

    public URI mapToDownloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileController.MAIN_RESOURCE)
                .path(FileController.DOWNLOAD_FILE_RESOURCE)
                .build(filename);
    }

}
