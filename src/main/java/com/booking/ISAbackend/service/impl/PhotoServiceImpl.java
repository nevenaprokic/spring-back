package com.booking.ISAbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.booking.ISAbackend.model.Photo;
import com.booking.ISAbackend.repository.PhotoRepository;
import com.booking.ISAbackend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public Photo getPhoto(int id) {
        return photoRepository.findById(id).get();
    }

    @Override
    public Stream<Photo> getAllPhotos() {
        return photoRepository.findAll().stream();
    }

    @Override
    public List<Photo> convertPhotosFromDTO(List<MultipartFile> photos, String email) throws IOException {
        List<Photo> adventurePhotos = new ArrayList<Photo>();
        if(photos == null){
            return adventurePhotos;
        }
        int counter = 0;
        for (MultipartFile photoData: photos
        ) {
            String photoName = savePhotoInFileSystem(photoData.getBytes(), email, counter);
            Photo p = new Photo(photoName);
            adventurePhotos.add(p);
            p.setDeleted(false);
            photoRepository.save(p);
            counter++;

        }
        return adventurePhotos;
    }
    @Override
    public String savePhotoInFileSystem(byte[] bytes, String ownerEmail, int counter) throws IOException {
        String folder = "./src/main/frontend/src/components/images/";
        LocalDateTime uniqueTime = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String photoName = ownerEmail + "_" + uniqueTime.format(formater) + counter + ".jpg";
        Path path = Paths.get(folder + photoName);
        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
            fos.write(bytes);
        }
        return photoName;
    }

    @Override
    public void removeOldPhotos(List<Photo> oldPhotos){
        Iterator<Photo> iterator = oldPhotos.iterator();
        while (iterator.hasNext()) {
            Photo photo = iterator.next();
            iterator.remove();
            photo.setDeleted(true);
            photoRepository.save(photo);
        }
    }
    @Override
    public List<Photo> ConvertBase64Photo(List<String> photos, String email) throws IOException {
        List<Photo> adventurePhotos = new ArrayList<Photo>();
        int counter = 0;
        for (String photoData: photos
        ) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(photoData);
            String photoName = savePhotoInFileSystem(bytes, email, counter);
            Photo p = new Photo(photoName);
            adventurePhotos.add(p);
            p.setDeleted(false);
            photoRepository.save(p);
            counter++;

        }
        return adventurePhotos;
    }


}
