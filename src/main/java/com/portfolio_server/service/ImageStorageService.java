package com.portfolio_server.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** Stores uploaded images on the local disk and returns their public URL path. */
@Service
public class ImageStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("png", "jpg", "jpeg", "gif", "webp", "svg"); //허용할 확장자 목록

    private final Path uploadDir;
    private final String publicPathPrefix;

    public ImageStorageService(
            @Value("${app.upload.dir:uploads}") String uploadDir,
            @Value("${app.upload.public-path:/uploads}") String publicPathPrefix) {
        //Paths.get("uploads")는 프로젝트 실행 위치 안의 uploads폴더를 나타내고,.toAbsolutePath()로 절대 경로로 나타냄,.normalize()로 표준 경로로 만듦(중복 슬래시 정리)
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.publicPathPrefix = publicPathPrefix;
    }

    @PostConstruct
    //객체 생성후
    void init() {
        try {
            //Files.createDirectory()는 이미 있어도 예외, 부모 폴더 없어도 예외 하지만 아래처럼 복수로 하면 이미 있으면 그냥 넘어가고 부모 폴더 없으면 부모 폴더까지 만듦
            Files.createDirectories(uploadDir); //해당경로의 폴더가 없을때만 해당경로에 폴더를 만듦
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + uploadDir, e);
        }
    }

    /**
     * Saves the file and returns the public URL path (e.g. {@code /uploads/uuid.png}).
     * The caller turns this into an absolute URL for the client.
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }
        String extension = extractExtension(file.getOriginalFilename());    //파일 이름에서 확장자를 뽑아냄
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported image type: " + extension);
        }
        String filename = UUID.randomUUID() + "." + extension;  //파일이름을 UUID뒤에 확장자 붙여서 만듦(중복이 나올수 없도록 폴더에 저장해야 해서 중복 나오면 안됨)
        Path target = uploadDir.resolve(filename).normalize();  //파일 저장 경로에 확장자 붙인 파일이름을 붙이고 표준 경로로 만듦
        // Guard against path traversal.
        if (!target.startsWith(uploadDir)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
        }
        try {
            file.transferTo(target);    //요청들어온 파일을 target위치의 파일에 넣음
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }
        return publicPathPrefix + "/" + filename;   /* /uploads/파일이름으로 반환 */
    }

    private String extractExtension(String originalFilename) {
        String ext = StringUtils.getFilenameExtension(originalFilename);    //확장자 읽음
        return ext == null ? "" : ext.toLowerCase();
    }
}