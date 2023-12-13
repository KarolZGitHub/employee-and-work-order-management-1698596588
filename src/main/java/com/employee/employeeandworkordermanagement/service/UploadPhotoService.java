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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UploadPhotoService {

    private final UserRepository userRepository;

    private static String createUploadDirectory() {
        return System.getProperty("user.dir") + "/src/main/resources/static/image";
    }

    private static String convertToRelativePath(String basePath, String fullPath) {
        Path basePathObject = Paths.get(basePath);
        Path fullPathObject = Paths.get(fullPath);
        Path relativePath = basePathObject.relativize(fullPathObject);
        return relativePath.toString();
    }

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
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static";
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
}
