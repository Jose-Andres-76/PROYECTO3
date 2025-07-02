package com.project.demo.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    /**
     * Inyection for the cloudinary personal information
     */
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     *
     * @param file
     * This one retunr the file
     * @return
     */
    public Map upload(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    /**
     *
     * @param file
     * @param publicId
     * This two update a specific image
     * @return
     */
    public Map updateImage(MultipartFile file, String publicId) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true
            ));
        } catch (IOException e) {
            throw new RuntimeException("Update (overwrite) failed", e);
        }
    }


    /**
     *
     * @param publicId
     * Get the image from the server
     */
    public String getImageUrl(String publicId) {
        return cloudinary.url().secure(true).generate(publicId);
    }

    /**
     *
     * @param publicId
     * Delete the image from the server
     * @return
     */
    public Map delete(String publicId) {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}
