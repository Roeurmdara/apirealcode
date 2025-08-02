package kh.edu.cstad.modilebankingaba.serivce;


import kh.edu.cstad.modilebankingaba.dto.MediaResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface MediaService {
    MediaResponse save(MultipartFile file);
    List<MediaResponse> saveAll(List<MultipartFile> files);
    Resource load(String filename);
    void delete(String filename) throws IOException;
    String getMediaType(String filename) throws IOException;

}
