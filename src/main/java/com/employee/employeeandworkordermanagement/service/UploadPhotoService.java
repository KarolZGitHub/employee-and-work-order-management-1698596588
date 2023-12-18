package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.repository.UserRepository;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UploadPhotoService {

    private final UserRepository userRepository;

    private static byte[] resizeImage(byte[] originalImageBytes, int targetWidth, int targetHeight) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
        BufferedImage originalImage = ImageIO.read(bis);

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        resizedImage.createGraphics().drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", bos);
        return bos.toByteArray();
    }

    public void uploadImage(MultipartFile file, Authentication authentication, Model model) throws IOException {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/image";
        String uploadDirectory = createUploadDirectory();
        System.out.println("Upload directory: " + uploadDirectory);

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, resizeImage(file.getBytes(), 60, 60)); // Resize image to 60x60 pixels

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        if (user != null) {
            String relativePath = convertToRelativePath(basePath, fileNameAndPath.toString());
            user.setProfilePicturePath("/" + relativePath);
            userRepository.save(user);
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString() + " for user: " + user.getFirstName());
        } else {
            model.addAttribute("msg", "User not found!");
        }
    }

    private String createUploadDirectory() {
        // Using FileSystems to get separator depending on operating system.
        String separator = FileSystems.getDefault().getSeparator();
        return System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources" + separator + "static";
    }

    private String convertToRelativePath(String basePath, String fullPath) {
        String relativePath = fullPath.substring(basePath.length());
        return relativePath.startsWith(File.separator) ? relativePath.substring(1) : relativePath;
    }
}
