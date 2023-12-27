package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UploadPhotoService;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private final UploadPhotoService uploadPhotoService;
    private final UserService userService;
    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }
    @GetMapping("/upload-image")
    public String displayUploadForm() {
        return "upload/uploadImage";
    }

    @PostMapping("/upload-image")
    public String uploadImage(Authentication authentication, Model model, @RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("msg", "You didn't choose your file.");
            return "upload/uploadImageInformation";
        }
        uploadPhotoService.uploadImage(file, authentication, model);
        return "upload/uploadImageInformation";
    }
}
