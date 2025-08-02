package kh.edu.cstad.modilebankingaba.serivce.impl;

import kh.edu.cstad.modilebankingaba.domain.Media;
import kh.edu.cstad.modilebankingaba.dto.MediaResponse;
import kh.edu.cstad.modilebankingaba.repository.MediaRepository;
import kh.edu.cstad.modilebankingaba.serivce.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final Path root;
    private final String baseUri;

    // Use constructor injection for both the repository and the configured path
    public MediaServiceImpl(@Value("${media.server-path}") String serverPath,
                            @Value("${media.base-uri}") String baseUri,
                            MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
        this.root = Paths.get(serverPath);
        this.baseUri = baseUri;
        init();
    }

    private void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }

    @Override
    public MediaResponse save(MultipartFile file) {
        // 1. Generate a unique name for the file
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String name = UUID.randomUUID().toString();
        int lastIndex = originalFilename.lastIndexOf(".");
        String extension = lastIndex > 0 ? originalFilename.substring(lastIndex + 1) : "";
        String filename = String.format("%s.%s", name, extension);

        // 2. Save file to server path
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (IOException e) {
            log.error("File upload failed for {}: {}", originalFilename, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        // 3. Save metadata to the database
        Media media = new Media();
        media.setName(name);
        media.setExtension(extension);
        media.setMimeTypeFile(file.getContentType());
        media.setIsDeleted(false);
        media = mediaRepository.save(media);

        // 4. Return a response DTO with the media details and URI
        return MediaResponse.builder()
                .name(media.getName())
                .extension(media.getExtension())
                .mimeTypeFile(media.getMimeTypeFile())
                .uri(baseUri + filename)
                .size(file.getSize())
                .build();
    }

    @Override
    public List<MediaResponse> saveAll(List<MultipartFile> files) {
        return files.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        Path file = root.resolve(filename);
        if (Files.deleteIfExists(file)) {
            // Get the name and extension to delete from the database
            int lastIndex = filename.lastIndexOf(".");
            String name = lastIndex > 0 ? filename.substring(0, lastIndex) : filename;
            String extension = lastIndex > 0 ? filename.substring(lastIndex + 1) : "";

            mediaRepository.deleteByNameAndExtension(filename);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found to delete: " + filename);
        }
    }

    @Override
    public String getMediaType(String filename) throws IOException {
        Path file = root.resolve(filename);
        return Files.probeContentType(file);
    }
}
