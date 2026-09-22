package com.topropes.backend.catalog;

import com.topropes.backend.common.error.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CatalogImageController {

    private static final long MAX_IMAGE_BYTES = 2 * 1024 * 1024;
    private final Path uploadDirectory;

    public CatalogImageController(@Value("${app.upload-dir:./uploads/wrestlers}") String uploadDir) {
        this.uploadDirectory = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    @PostMapping(value = "/admin/catalog/wrestler-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || file.getSize() > MAX_IMAGE_BYTES) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Image must be between 1 byte and 2 MB.");
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!"image/png".equals(file.getContentType()) || !"png".equalsIgnoreCase(extension)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only PNG images are supported.");
        }

        String filename = UUID.randomUUID() + ".png";
        try {
            Files.createDirectories(uploadDirectory);
            Files.copy(file.getInputStream(), uploadDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store image.");
        }
        return Map.of("url", "/api/v1/catalog/wrestler-images/" + filename);
    }

    @GetMapping("/catalog/wrestler-images/{filename:.+}")
    public ResponseEntity<Resource> image(@PathVariable String filename) {
        try {
            Path image = uploadDirectory.resolve(filename).normalize();
            if (!image.startsWith(uploadDirectory) || !Files.isRegularFile(image)) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Image not found.");
            }
            Resource resource = new UrlResource(image.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(resource);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Image not found.");
        }
    }
}