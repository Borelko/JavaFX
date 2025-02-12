package com.example.testoviyfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Flipping {
    public static void flipImage(BufferedImage img){

        for (int x = 0; x < img.getWidth()/2; x++) {
            for(int y = 0; y < img.getHeight(); y++) {

                int tempRgb = img.getRGB(x, y);

                img.setRGB(x,y,img.getRGB((img.getWidth()-1-x), y));
                img.setRGB((img.getWidth()-1-x),y,tempRgb);

            }
        }

    }

    public static void writeFlippedAnn(File txt, String To_Path_label) throws FileNotFoundException {

        try (FileWriter writer = new FileWriter(To_Path_label)) {
            Scanner sc = new Scanner(txt);
            System.out.println("Scan" + txt);
            while (sc.hasNext()) {

                    System.out.println("In cycle");
                    String line = sc.next();
                    double x = Double.parseDouble(sc.next());
                    line += (" " + (Double) (1 - x));
                    line += " " + sc.next();
                    line += " " + sc.next();
                    line += " " + sc.next();

                    writer.write(line);
                    writer.write(System.lineSeparator());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
