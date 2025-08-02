package kh.edu.cstad.modilebankingaba.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaResponse {
    private String name;
    private String extension;
    private String mimeTypeFile;
    private String uri;
    private long size;
}
