package qreol.project.tasklist.service.impl;

import io.minio.MinioClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import qreol.project.tasklist.config.TestConfig;
import qreol.project.tasklist.domain.exception.ImageUploadException;
import qreol.project.tasklist.domain.task.TaskImage;
import qreol.project.tasklist.service.props.MinioProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@SpringBootTest
@ActiveProfiles("junit")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskImageServiceImplTest {

    @MockBean
    public MinioClient minioClient;

    @MockBean
    public MinioProperties minioProperties;

    @Autowired
    public TaskImageServiceImpl imageService;

    @Test
    void upload() throws Exception {
        String bucketName = "images";
        String fileExtension = "jpg";
        String imageName = "image";
        String originalFileName = "image.jpg";
        TaskImage image = new TaskImage();

        MultipartFile file = new MockMultipartFile(
                imageName,
                originalFileName,
                fileExtension,
                new ByteArrayInputStream("text".getBytes())
        );
        image.setFile(file);

        Mockito.when(minioProperties.getBucket()).thenReturn(bucketName);
        Mockito.when(minioClient.bucketExists(null)).thenReturn(true);
        Mockito.when(minioClient.putObject(null)).thenReturn(null);

        String fileName = imageService.upload(image);
        String testExtension = fileName
                .substring(fileName.lastIndexOf('.') + 1);

        Assertions.assertNotNull(fileName);
        Assertions.assertEquals(fileExtension, testExtension);
    }

    @Test
    void uploadWithCreateBucketFail() throws Exception {
        String exceptionMessage = "Image upload failed bucket name must not be null.";
        TaskImage image = new TaskImage();
        Mockito.when(minioClient.bucketExists(null)).thenReturn(false);

        ImageUploadException e = Assertions.assertThrows(
                ImageUploadException.class,
                () -> imageService.upload(image)
        );

        Assertions.assertEquals(e.getMessage(), exceptionMessage);
    }

    @Test
    void uploadWithNoNameAndEmptyInputStream() throws Exception {
        String exceptionMessage = "Image must have name.";
        String imageName = "image";
        String bucketName = "images";
        TaskImage image = new TaskImage();

        MultipartFile file = new MockMultipartFile(
                imageName,
                InputStream.nullInputStream()
        );
        image.setFile(file);

        Mockito.when(minioProperties.getBucket()).thenReturn(bucketName);
        Mockito.when(minioClient.bucketExists(null)).thenReturn(true);
        Mockito.when(minioClient.putObject(null)).thenReturn(null);

        ImageUploadException e = Assertions.assertThrows(
                ImageUploadException.class,
                () -> imageService.upload(image)
        );

        Assertions.assertEquals(e.getMessage(), exceptionMessage);

    }

}
