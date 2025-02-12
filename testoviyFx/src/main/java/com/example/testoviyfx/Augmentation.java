package com.example.testoviyfx;// Java program to demonstrate colored
// to blue coloured image conversion


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Augmentation {
    public static void MakeAugmentation(String DIR)
            throws IOException {
        System.out.println("Augmentation started...");
        BufferedImage img;
        File f_WithNoise, f_compressed, f_changedHue, f_flipped;
        File[] Imgfiles = new File(DIR + "/images").listFiles();
        File[] Txtfiles = new File(DIR + "/labels").listFiles();
        Path source, destination;

        for (int i = 0; i < Imgfiles.length; i++) {
            File IMG_ORIGINAL = Imgfiles [i];
            File TXT_ORIGINAL = Txtfiles [i];
            if (!IMG_ORIGINAL.exists()) continue;
            if (!TXT_ORIGINAL.exists()) continue;

            String IMG_COMPRESSED = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_Compressed";
            String IMG_WITHNOISE  = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_WithNoise";
            String IMG_HUE  = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_ChangedHue";
            String IMG_FLIPPED = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_Flipped";


            source = Paths.get(String.valueOf(TXT_ORIGINAL));

            f_compressed = new File(IMG_COMPRESSED + ".jpg");
            f_WithNoise = new File(IMG_WITHNOISE + ".jpg");
            f_changedHue = new File(IMG_HUE + ".jpg");
            f_flipped = new File(IMG_FLIPPED + ".jpg");

            img = ImageIO.read(IMG_ORIGINAL);

            Noise.addNoise(img, 2);
            ImageIO.write(img, "jpg", f_WithNoise);

            HueSaturationBrightness.changeHue(img);
            ImageIO.write(img, "jpg", f_changedHue);

            Flipping.flipImage(img);
            ImageIO.write(img, "jpg", f_flipped);

            Compress.compress(IMG_ORIGINAL, f_compressed, 0.5f);
            

            destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_Compressed.txt");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_WithNoise.txt");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_ChangedHue.txt");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            Flipping.writeFlippedAnn(
                    Txtfiles[i],
                    DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_Flipped.txt"
            );

            System.out.println("Percent of augmentation: " + (int)(( (double) i / (double) Imgfiles.length) * 100) + "% ");
        }

        System.out.println("Augmentation finished!");

    }
}
