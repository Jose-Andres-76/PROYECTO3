package com.project.demo.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;




@Service
public class CloudinaryService {

    /**
     * Inyection for the cloudinary personal information
     */
    @Autowired
    private final Cloudinary cloudinary;
    @Autowired
    private UserRepository userRepository;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
    }



    @SuppressWarnings("unchecked")

    public User upload(Long id, MultipartFile file) {

        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");
        Optional<User> userFound= userRepository.findById(id);
        User user = userFound.get();

        String extensions = null;

        if( file.getOriginalFilename() != null ) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            extensions = splitName[splitName.length - 1];
        }


        if (!allowedExtensions.contains(extensions)) {
            System.out.println("Extensions not allowed");
            return null;
        }

        try {

            Map<String, Object> resultUpload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "ProfilePictures"));

            String imageUrl = resultUpload.get("secure_url").toString();

            user.setUrlImage(imageUrl);

            userRepository.save(user);

            return user;
        } catch (IOException e) {
            throw new RuntimeException("Update (overwrite) failed", e);
        }



    }
//    /**
//     *
//     * @param file
//     * This one retunr the file
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public Map upload(MultipartFile file) {
//        try {
//            return Map<String, ObjectUtils> resultload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("Folders", "ProfilePictures"));
//        } catch (IOException e) {
//            throw new RuntimeException("Upload failed", e);
//        }
//    }
//
//    /**
//     *
//     * @param file
//     * @param publicId
//     * This two update a specific image
//     * @return
//     */
//    public Map updateImage(MultipartFile file, String publicId) {
//        try {
//            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//                    "public_id", publicId,
//                    "overwrite", true
//            ));
//        } catch (IOException e) {
//            throw new RuntimeException("Update (overwrite) failed", e);
//        }
//    }
//
//
//    /**
//     *
//     * @param publicId
//     * Get the image from the server
//     */
//    public String getImageUrl(String publicId) {
//        return cloudinary.url().secure(true).generate(publicId);
//    }
//
//    /**
//     *
//     * @param publicId
//     * Delete the image from the server
//     * @return
//     */
//    public Map delete(String publicId) {
//        try {
//            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        } catch (IOException e) {
//            throw new RuntimeException("Delete failed", e);
//        }
//    }
}
