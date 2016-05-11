package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class FileUploadController {
    @Autowired
    private SessionBean sessionBean;
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/{id}/fileUpload", method = POST)
    public ResponseEntity<User> uploadFile(@RequestParam("file") MultipartFile uploadfile,
                                           @PathVariable("id") Long id) {
        Long idBd = sessionBean.getSessionId();

        if (Optional.ofNullable(idBd).isPresent() && id.equals(idBd)) {
            Optional<User> upload = uploadService.upload(uploadfile, id);
            if (upload.isPresent()){
                return new ResponseEntity<>(upload.get(), OK);
            }
        }

        return new ResponseEntity<>(BAD_REQUEST);
    }
}
