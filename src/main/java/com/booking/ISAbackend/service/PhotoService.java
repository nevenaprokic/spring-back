package com.booking.ISAbackend.service;

import com.booking.ISAbackend.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


public interface PhotoService {

    Photo getPhoto(int id);
    Stream<Photo> getAllPhotos();
    List<Photo> convertPhotosFromDTO(List<MultipartFile> photos, String email) throws IOException;
    String savePhotoInFileSystem(byte[] bytes, String ownerEmail, int counter) throws IOException;
    void removeOldPhotos(List<Photo> oldPhotos);
    List<Photo> ConvertBase64Photo(List<String> photos, String email) throws IOException;

}
