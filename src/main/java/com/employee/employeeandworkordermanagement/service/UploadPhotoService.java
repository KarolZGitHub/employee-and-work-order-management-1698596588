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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UploadPhotoService {

    private final UserRepository userRepository;

    private static String createUploadDirectory() {
        String userHome = System.getProperty("user.home");
        Path uploadPath = Paths.get(userHome, "uploads");

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create the upload directory: " + uploadPath, e);
            }
        }
        return uploadPath.toString();
    }

    public void uploadImage(MultipartFile file, Authentication authentication, Model model) throws IOException {
        System.out.println("Upload directory: " + createUploadDirectory());

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(createUploadDirectory(), file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        if (user != null) {
            user.setProfilePicturePath(fileNameAndPath.toString());
            userRepository.save(user);
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString() + " for user: " + user.getFirstName());
        } else {
            model.addAttribute("msg", "User not found!");
        }
    }
}
