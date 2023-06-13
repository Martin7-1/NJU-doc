package com.zyinnju.backend.web.controller;

import com.zyinnju.backend.entity.dto.ContactInfo;
import com.zyinnju.backend.service.ContactInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

/**
 * 通讯录接口
 *
 * @author zhengyi
 */
@Controller
@Slf4j
@RequestMapping("/contact")
@SessionAttributes("contactInfo")
public class ContactController {

    private final ContactInfoService contactInfoService;

    @Autowired
    public ContactController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @ModelAttribute(name = "contactInfo")
    public ContactInfo contactInfo() {
        return new ContactInfo();
    }

    @GetMapping
    public String getContactInfoView() {
        return "contact";
    }

    @PostMapping
    public String createContact(@Valid ContactInfo contactToCreate, Errors errors,
                                SessionStatus sessionStatus, Model model) {
        model.addAttribute("contactList", contactInfoService.getContacts());
        if (errors.hasErrors()) {
            return "contact";
        }
        log.info("Submit contact, contact=" + contactToCreate.toString());
        try {
            contactInfoService.addContact(contactToCreate);
        } catch (RuntimeException e) {
            log.warn("重复的通讯录");
            errors.rejectValue("phone", "1", "不能出现重复通讯录");
            return "contact";
        }
        sessionStatus.setComplete();
        return "contact";
    }
}
