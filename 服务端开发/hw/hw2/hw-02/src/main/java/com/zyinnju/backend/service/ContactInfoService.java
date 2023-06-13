package com.zyinnju.backend.service;

import com.zyinnju.backend.entity.dto.ContactInfo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyi
 */
@Service
@Data
public class ContactInfoService {

    private List<ContactInfo> contacts;

    public ContactInfoService() {
        contacts = new ArrayList<>();
    }

    public void addContact(ContactInfo contactInfo) {
        ContactInfo check = contacts.stream()
                .filter(contact -> contact.getPhone().equals(contactInfo.getPhone()))
                .findAny()
                .orElse(null);
        if (check != null) {
            throw new RuntimeException("重复的通讯录");
        }
        contacts.add(contactInfo);
    }
}
