package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Message;
import com.employee.employeeandworkordermanagement.service.MessageService;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @GetMapping("/all-messages")
    public String showAllMessagesForUser(Model model, Authentication authentication) {
        List<Message> messageList = messageService.getMessagesForUser(
                userService.findOptionalUserByEmail(authentication.getName()).get());
        model.addAttribute("messages", messageList);
        return "message/allMessages";
    }

    @PostMapping("/delete")
    public String deleteMessage(@RequestParam(name = "id") Long id) {
        Message message = messageService.findById(id);
        messageService.deleteMessage(message);
        return "redirect:/message/all-messages";
    }

    @GetMapping("view")
    public String showSingleMessage(@RequestParam(name = "id") Long id, Model model) {
        Message message = messageService.findById(id);
        message.setRead(true);
        messageService.saveMessage(message);
        model.addAttribute("message", message);
        return "message/singleMessage";
    }
}
//TODO: single message view, delete message feature, change all messages view