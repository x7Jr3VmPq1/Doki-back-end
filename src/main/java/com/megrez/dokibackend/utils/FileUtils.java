package com.megrez.dokibackend.utils;

import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.common.LocalFilesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.UUID;

// 文件操作工具类
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    // 根据URL截取文件名
    public static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    // 根据文件流保存视频，返回视频URL
    public static String saveVideo(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // 获取原始文件名（含扩展名）
        String originalFilename = file.getOriginalFilename();
        // 获取文件扩展名
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 文件夹的唯一名称
        String uniqueFileName = UUID.randomUUID().toString();
        // 创建文件夹，用来保存视频文件和发布时的封面图
        Path folderPath = Paths.get(LocalFilesPath.videoFilesPathLocal + uniqueFileName);
        Files.createDirectories(folderPath);
        Path targetFilePath = folderPath.resolve("video" + fileExtension);

        // 将上传的文件保存到目标路径
        file.transferTo(targetFilePath.toFile());

        // 返回唯一文件夹名
        return uniqueFileName;
    }

    // 保存封面图
    public static String saveCover(String coverBase64, String folderName) throws IOException {
        if (coverBase64 == null || coverBase64.isEmpty()) {
            return null;
        }
        try {
            // 解析 Base64 字符串，去掉头部的 `data:image/jpeg;base64,` 部分
            String base64Data = coverBase64.split(",")[1];

            // 将 Base64 字符串解码为字节数组
            byte[] imgBytes = Base64Utils.decodeFromString(base64Data);

            // 生成文件名
            String fileName = "thumbnail.jpg";

            // 确保目录存在
            Path directory = Paths.get(LocalFilesPath.videoFilesPathLocal + folderName);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            // 保存图片到本地文件系统
            Path filePath = directory.resolve(fileName);
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(imgBytes);
            }
            // 返回封面图路径
            return FileServerURL.videoFilesPath + folderName + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }

    // 发布视频时，在videos文件夹中创建一个文件夹，存储视频和封面图，返回被移动后的视频和封面图
//    public static String[] createVideoFolder(String videoURL, String coverBase64) throws IOException {
//        // 先根据视频URL获取视频名称
//        String fileNameFromURL = getFileNameFromURL(videoURL);
//        // 去除拓展名
//        String fileNameWithoutExtension = fileNameFromURL.substring(0, fileNameFromURL.lastIndexOf("."));
//        // 判断pending_videos文件夹下是否存在同名文件
//        File file = new File(FileServerURL.videoUploadPath, fileNameFromURL);
//        if (file.exists()) {
//            // 在videos文件夹中创建一个同名的文件夹
//            Path path = Paths.get(FileServerURL.videoFilesPathLocal + fileNameWithoutExtension);
//            Path directories = Files.createDirectories(path);
//            Path targetFile = directories.resolve(file.getName());
//            Files.move(file.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
//            // 创建一个字符串数组，存储新的HTTP路径并返回
//            String[] httpArr = new String[2];
//            return httpArr;
//        }
//        return null;
//    }

    // 删除视频
    public static void deleteVideo(String videoURL) throws IOException {
        Files.deleteIfExists(Paths.get(LocalFilesPath.videoFilesPathLocal, videoURL.substring(videoURL.lastIndexOf("/") + 1)));
    }

    // 删除文件或文件夹（递归）
    public static void delete(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) return;

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    // 复制文件或目录（递归）
    public static void copy(String sourceStr, String targetStr) throws IOException {
        Path source = Paths.get(sourceStr);
        Path target = Paths.get(targetStr);

        Files.walk(source).forEach(sourcePath -> {
            try {
                Path relative = source.relativize(sourcePath);
                Path targetPath = target.resolve(relative);
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy: " + sourcePath, e);
            }
        });
    }

    // 移动文件或目录（相当于剪切）
    public static void move(String sourceStr, String targetStr) throws IOException {
        Path source = Paths.get(sourceStr);
        Path target = Paths.get(targetStr);
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    // 存储私聊发送的图片
    public static String savePrivateChatImage(String imageBASE64) throws IOException {
        String imgUrl;
        try {
            // 解析 Base64 字符串，去掉头部的 `data:image/jpeg;base64,` 部分
            String base64Data = imageBASE64.split(",")[1];

            // 将 Base64 字符串解码为字节数组
            byte[] imgBytes = Base64Utils.decodeFromString(base64Data);

            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";

            // 确保目录存在
            Path directory = Paths.get(LocalFilesPath.privateChatImageUploadPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            // 保存图片到本地文件系统
            Path filePath = directory.resolve(fileName);
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(imgBytes);
            }
            // 设置评论图片的 URL
            imgUrl = FileServerURL.privateChatImageFilesPath + fileName;


        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
        return imgUrl;
    }
}