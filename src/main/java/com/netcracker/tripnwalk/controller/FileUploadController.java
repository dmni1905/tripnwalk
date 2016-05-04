package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class FileUploadController {
    @Autowired
    private SessionBean sessionBean;
    @Autowired
    private Environment env;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/{id}/fileUpload", method = POST)
    public ResponseEntity<User> uploadFile(@RequestParam("file") MultipartFile uploadfile, @PathVariable("id") Long id) {
        Long idBd = sessionBean.getSessionId();
        if (Optional.ofNullable(idBd).isPresent() && id.equals(idBd)) {
            final String path = env.getProperty("paths.uploadedFiles");
            File dir = new File(path, id.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            } else {
                Stream.of(dir.listFiles()).forEach(File::delete);
            }
            String name = uploadfile.getOriginalFilename();
            String exp = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
            if (exp.equals("png") || exp.equals("jpg") || exp.equals("jepg")) {

                if (!uploadfile.isEmpty()) {
                    File newImg = new File(dir, System.currentTimeMillis() + "." + exp);
                    if (uploadedFiles(uploadfile, newImg)) {
                        userService.setImgSrc(id, "/static/img/users/"+id+"/"+newImg.getName());
                        User user = userService.getById(id).get();
                        user.getFriends().clear();
                        user.getRoutes().clear();
                        return new ResponseEntity<>(user, OK);
                    }
                }
            }

        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    private boolean uploadedFiles(MultipartFile uploadfile, File img) {
        try {
            byte[] bytes = uploadfile.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(img));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
