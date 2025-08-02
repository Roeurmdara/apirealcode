package kh.edu.cstad.modilebankingaba.controller;

import kh.edu.cstad.modilebankingaba.dto.MediaResponse;
import kh.edu.cstad.modilebankingaba.serivce.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1") // A general API prefix
public class MediaController {

    private final MediaService mediaService;

    /**
     * Endpoint for uploading a single file.
     * The file is passed in the request body as "multipart/form-data".
     *
     * @param file The MultipartFile object representing the uploaded file.
     * @return A MediaResponse object containing metadata and the URI of the uploaded file.
     */
    @PostMapping(value = "/medias/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponse> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        MediaResponse response = mediaService.save(file);
        // Construct the full URI to the uploaded file for the response
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/media/") // Use the base URI from properties
                .path(response.getName() + "." + response.getExtension())
                .toUriString();
        response.setUri(fileDownloadUri);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint for uploading multiple files at once.
     *
     * @param files A list of MultipartFile objects.
     * @return A list of MediaResponse objects for each uploaded file.
     */
    @PostMapping(value = "/medias/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaResponse>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<MediaResponse> responses = mediaService.saveAll(files);
        // Construct the full URI for each uploaded file
        responses.forEach(response -> {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media/") // Use the base URI from properties
                    .path(response.getName() + "." + response.getExtension())
                    .toUriString();
            response.setUri(fileDownloadUri);
        });
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    /**
     * Endpoint for downloading a file by its filename.
     * The filename is expected to be a combination of name and extension (e.g., uuid.jpg).
     *
     * @param filename The full name of the file to download.
     * @return A ResponseEntity containing the file resource and appropriate headers.
     */
    @GetMapping("/media/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Resource resource = mediaService.load(filename);
        String contentType = "application/octet-stream"; // Default content type

        // Try to determine file's content type
        try {
            contentType = mediaService.getMediaType(filename);
        } catch (IOException e) {
            // Fall back to default
            System.err.println("Could not determine file type for " + filename);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Endpoint for deleting a file by its filename.
     *
     * @param filename The full name of the file to delete.
     * @return A ResponseEntity with a status indicating success.
     * @throws IOException If the file cannot be deleted.
     */
    @DeleteMapping("/medias/delete/{filename:.+}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) throws IOException {
        mediaService.delete(filename);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
