package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    private final UserRepository userRepository;

    @GetMapping("/upload-image")
    public String displayUploadForm() {
        return "upload/uploadImage";
    }

    @PostMapping("/upload-image")
    public String uploadImage(Authentication authentication, Model model, @RequestParam("image") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            model.addAttribute("msg", "You didn't choose your file.");
            return "upload/uploadImageInformation";
        }
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
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
        return "upload/uploadImageInformation";
    }
}
