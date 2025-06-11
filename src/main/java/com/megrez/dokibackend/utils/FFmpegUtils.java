package com.megrez.dokibackend.utils;

import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.common.LocalFilesPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FFmpegUtils {
    public static String createThumbnail(String videoPath) {
        // 输入视频路径
        String inputVideo = LocalFilesPath.videoFilesPathLocal + videoPath + "/video.mp4";

        // 截图输出路径
        String outputImage = LocalFilesPath.videoFilesPathLocal + videoPath + "/thumbnail.jpg";

        // 截图时间点
        String timestamp = "00:00:05";

        // FFmpeg 命令
        String[] command = new String[]{
                "ffmpeg",
                "-ss", timestamp,
                "-i", inputVideo,
                "-frames:v", "1",
                "-q:v", "2",
                outputImage
        };
        try {
            // 使用 ProcessBuilder 执行命令
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true); // 合并标准输出和错误输出

            Process process = builder.start();

            // 打印输出日志，日志必须被消费，否则会导致进程阻塞
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
            }
            // 等待命令执行完成
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return FileServerURL.videoFilesPath + videoPath + "/thumbnail.jpg";
    }

    /**
     * 获取视频时长
     *
     * @param videoPath 视频路径
     * @return 视频时长（秒）
     */
    public static int getVideoDuration(String videoPath) {
        videoPath = LocalFilesPath.videoFilesPathLocal + videoPath + "/video.mp4";
        String[] command = new String[]{
                "ffprobe",
                "-i", videoPath,
                "-show_entries", "format=duration",
                "-v", "quiet",
                "-of", "csv=p=0"
        };
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            String output = new String(process.getInputStream().readAllBytes());
            return (int) Math.round(Double.parseDouble(output));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 进行视频转码，统一转码为mp4格式
     *
     * @param videoPath 视频路径
     */
    public static void transcodeVideo(String videoPath) {
        videoPath = LocalFilesPath.videoFilesPathLocal + videoPath + "/video.mp4";
        String[] command = new String[]{
                "ffmpeg",
                "-i", videoPath,
                "-c:v", "libx264",
                "-c:a", "aac",
                "-strict", "experimental",
                "-f", "mp4",
                "-movflags", "frag_keyframe+empty_moov",
                "-bsf:a", "aac_adtstoasc",
                "-movflags", "faststart",
        };
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
