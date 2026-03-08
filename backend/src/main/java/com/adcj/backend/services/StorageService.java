package com.adcj.backend.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service //TODO: delete this
public class StorageService {
    StorageService() {
    }

    public String store(MultipartFile file) {
        return UUID.randomUUID().toString();
    }

    public Resource loadAsResource(String imageId) {
        return null;
    }

    public void delete(String imageId) {

    }
}
