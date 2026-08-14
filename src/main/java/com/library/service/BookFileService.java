package com.library.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BookFileService {

    String upload(Long bookId, MultipartFile file);

    Resource download(Long bookId);
}
