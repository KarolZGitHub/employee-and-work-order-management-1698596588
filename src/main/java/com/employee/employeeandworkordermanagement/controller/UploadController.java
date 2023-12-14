package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UploadPhotoService;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private final UploadPhotoService uploadPhotoService;
    private final UserService userService;

    @GetMapping("/upload-image")
    public String displayUploadForm(Model model, Authentication authentication) {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        return "upload/uploadImage";
    }

    @PostMapping("/upload-image")
    public String uploadImage(Authentication authentication, Model model, @RequestParam("image") MultipartFile file) throws IOException {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        if (file.isEmpty()) {
            model.addAttribute("msg", "You didn't choose your file.");
            return "upload/uploadImageInformation";
        }
        uploadPhotoService.uploadImage(file, authentication, model);
        return "upload/uploadImageInformation";
    }
}
