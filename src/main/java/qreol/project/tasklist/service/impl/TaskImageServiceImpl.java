package qreol.project.tasklist.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qreol.project.tasklist.domain.exception.ImageUploadException;
import qreol.project.tasklist.domain.task.TaskImage;
import qreol.project.tasklist.service.ImageService;
import qreol.project.tasklist.service.props.MinioProperties;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed " + e.getMessage());
        }

        MultipartFile file = image.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name.");
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed " + e.getMessage());
        }
        saveImage(inputStream, fileName);

        return fileName;
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1)
                .build()
        );
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());

        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }

    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);

        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }


}
