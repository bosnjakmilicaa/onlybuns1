package com.project.onlybuns.service;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class ImageCompressionService {
    private final String IMAGE_DIRECTORY = "src/main/resources/static.images";

    public ImageCompressionService() {
       // printImagesInDirectory();
    }

   /* public void printImagesInDirectory() {
        File dir = new File(IMAGE_DIRECTORY);
        File[] files = dir.listFiles();

        if (files != null) {
            System.out.println("Sadržaj direktorijuma:");
            for (File file : files) {
                System.out.println("Pronađena slika: " + file.getName());
            }
        } else {
            System.out.println("Direktorijum je prazan ili nije pronađen.");
        }
    } */

    @Scheduled(cron = "0 0 0 * * ?")
    public void compressOldImages() {
        File dir = new File(IMAGE_DIRECTORY);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isOlderThanOneMonth(file) && !isCompressed(file)) {
                    try {
                        compressImage(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*private boolean isOlderThanOneMonth(File file) {
        long currentTime = new Date().getTime();
        long fileTime = file.lastModified();
        long oneMonthInMillis = 30L * 24 * 60 * 60 * 1000; // 30 dana u milisekundama
        return (currentTime - fileTime) > oneMonthInMillis;
    }*/

    private boolean isOlderThanOneMonth(File file) {
        return true;  // kompresujeće se sve slike
    }

     @PostConstruct
    public void init() {
        compressOldImages();
    }

    private boolean isCompressed(File file) {
        return file.getName().contains("_compressed");
    }

    private void compressImage(File file) throws IOException {
        String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf('.'));
        String compressedImagePath = file.getParent() + File.separator + fileNameWithoutExtension + "_compressed" + getFileExtension(file);

        //System.out.println("Pokušavam da kompresujem sliku: " + file.getName());

        Thumbnails.of(file)
                .size(800, 800)
                .outputFormat("jpeg")
                .toFile(compressedImagePath);

        System.out.println("Slika kompresovana: " + compressedImagePath);
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot != -1) {
            return fileName.substring(lastIndexOfDot);
        }
        return "";
    }
}
