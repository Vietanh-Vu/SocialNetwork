package com.example.socialnetwork.common.util;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HandleFile {

    public static String loadFileImage(PostRequest postRequest, StorageServicePort storageServicePort, int numberFile) {
        StringBuilder photoPaths = new StringBuilder();
        if (postRequest.getPhotoLists() != null) {
            if (postRequest.getPhotoLists().length <= numberFile) {
                for (MultipartFile photo : postRequest.getPhotoLists()) {
                    String filePath = storageServicePort.store(FileType.IMAGE, photo);
                    String photoUrl = storageServicePort.getUrl(filePath);
                    photoPaths.append(photoUrl).append(",");
                }
                // Remove trailing comma
                if (!photoPaths.isEmpty()) {
                    photoPaths.deleteCharAt(photoPaths.length() - 1);
                }
            } else {
                throw new ClientErrorException("Exceeded number of allowed files");
            }
        }
        if(photoPaths.isEmpty()){
            return null;
        }
        return photoPaths.toString();
    }

    public static String loadFileImage(MultipartFile[] images, StorageServicePort storageServicePort, int numberFile) {
        StringBuilder photoPaths = new StringBuilder();
        if (images != null) {
            if (images.length <= numberFile) {
                for (MultipartFile photo : images) {
                    if (photo.isEmpty()) {
                        return null;
                    }
                    String filePath = storageServicePort.store(FileType.IMAGE, photo);
                    String photoUrl = storageServicePort.getUrl(filePath);
                    photoPaths.append(photoUrl).append(",");
                }
                // Remove trailing comma
                if (!photoPaths.isEmpty()) {
                    photoPaths.deleteCharAt(photoPaths.length() - 1);
                }
            } else {
                throw new ClientErrorException("Exceeded number of allowed files");
            }
        }else{
            return "";
        }
        return photoPaths.toString();
    }

    public static String getFilePath(String photo){
        String regex = ".*(images/[^/]+\\.png)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(photo);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
}
