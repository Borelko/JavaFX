package com.example.testoviyfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HueSaturationBrightness {
    public static void changeHue(BufferedImage img){

        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);

                float[] hsb = Color.RGBtoHSB((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, (pixel) & 0xFF, null);

                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];

                hue*=0.9;

                int rgb = Color.HSBtoRGB(hue, saturation, brightness);
                img.setRGB(x, y, rgb);
            }
        }
    }
    public static void decreaseBrightness(BufferedImage img){
        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);

                float[] hsb = Color.RGBtoHSB((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, (pixel) & 0xFF, null);

                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];

                brightness *= 0.3;

                int rgb = Color.HSBtoRGB(hue, saturation, brightness);
                img.setRGB(x, y, rgb);
            }
        }

    }
    public static void decreaseSaturation(BufferedImage img){
        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);

                float[] hsb = Color.RGBtoHSB((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, (pixel) & 0xFF, null);

                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];

                saturation*=0.5;

                int rgb = Color.HSBtoRGB(hue, saturation, brightness);
                img.setRGB(x, y, rgb);
            }
        }

    }
}
