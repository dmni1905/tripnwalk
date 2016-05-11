package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.OK;

@Service
public class UploadService {
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    public Optional<User> upload(MultipartFile uploadfile, Long id){
        final String path = env.getProperty("paths.uploadedFiles");
        File dir = new File(path, id.toString());
        User user = null;

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
                    userService.setImgSrc(id, "/static/img/users/" + id + '/' + newImg.getName());

                    user = userService.getById(id).get();
                    user.getFriends().clear();
                    user.getRoutes().clear();
                }
            }
        }
        return Optional.ofNullable(user);
    }

    private boolean uploadedFiles(MultipartFile uploadfile, File img) {
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
            byte[] bytes = uploadfile.getBytes();

            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return false;
        }

        return true;
    }
}
