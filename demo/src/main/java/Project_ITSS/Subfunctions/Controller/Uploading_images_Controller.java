package Project_ITSS.Subfunctions.Controller;


import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Uploading_images_Controller {
    @PostMapping("/upload-image")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file) {
        Map<String, Object> json = new HashMap<>();
        try {
            // Kiểm tra file có tồn tại không
            if (file.isEmpty()) {
                json.put("status", 0);
                json.put("message", "Please select a file to upload");
                return json;
            }

            // Kiểm tra loại file (chỉ cho phép ảnh)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                json.put("status", 0);
                json.put("message", "Only image files are allowed");
                return json;
            }

            // Tạo thư mục lưu trữ nếu chưa tồn tại
            String uploadDir = "D:\\frontend-aims-main(5)\\frontend-aims-main(2)\\frontend-aims-main(2)\\frontend-aims-main\\public";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Tạo tên file unique để tránh trùng lặp
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), filePath);

            json.put("status", 1);
            json.put("message", "File uploaded successfully");
            json.put("filename", filename);
            json.put("filepath", filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            json.put("status", 0);
            json.put("message", "Failed to upload file: " + e.getMessage());
        }
        return json;
    }

    @PostMapping("/delete-image")
    public Map<String, Object> deleteImage(@RequestParam("image") String filename) {
        Map<String, Object> json = new HashMap<>();
        String uploadDir = "D:\\frontend-aims-main(5)\\frontend-aims-main(2)\\frontend-aims-main(2)\\frontend-aims-main\\public";
        Path filePath = Paths.get(uploadDir + filename);
        System.out.println(filePath);
        try {
            File file = filePath.toFile();
            if (!file.exists()) {
                json.put("status", 0);
                json.put("message", "File not found");
                return json;
            }
            if (file.delete()) {
                json.put("status", 1);
                json.put("message", "File deleted successfully");
            } else {
                json.put("status", 0);
                json.put("message", "Failed to delete file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status", 0);
            json.put("message", "Error deleting file: " + e.getMessage());
        }
        return json;
    }
}
