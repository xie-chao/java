package com.calix.tools.app;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTool {

    public static void main(String[] args) throws IOException {
        String imageUrl = "/home/calix/Pictures/test1.jpg";
        String outputUrl = "/home/calix/Pictures/result.jpg";
//        toGray(imageUrl, outputUrl);
        toBinary(imageUrl, outputUrl);
    }

    public static void toGray(String imageUrl, String outputUrl) throws IOException {
        BufferedImage frame = ImageIO.read(new File(imageUrl));
        int height = frame.getHeight();
        int width = frame.getWidth();
        System.out.println("width = " + width + ", height : " + height);
        int[][] pixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = getGrey(frame.getRGB(x, y));
                pixels[y][x] = colorToRGB(255, gray, gray, gray);
            }
        }
        createImage(pixels, height, width, outputUrl);
    }

    public static void toBinary(String imageUrl, String outputUrl) throws IOException {
        BufferedImage frame = ImageIO.read(new File(imageUrl));
        int height = frame.getHeight();
        int width = frame.getWidth();
        int[][] pixels = new int[height][width];
        int sum = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grey = getGrey(frame.getRGB(x, y));
                pixels[y][x] = grey;
                sum += grey;
            }
        }
        int average = sum / (height * width);
        int[][] binaryPixels = convert_dithering(pixels, height, width, average);
        createImage(binaryPixels, height, width, outputUrl);
    }

    private static int getGrey(int rgb) {
        int r = rgb >> 16 & 0xff;
        int g = rgb >> 8 & 0xff;
        int b = rgb & 0xff;
        //		return (int)(0.299 * r + 0.587 * g + 0.114 * b);
        return (r * 19595 + g * 38469 + b * 7472) >> 16;// http://www.cnblogs.com/carekee/articles/3629964.html
    }

    /**
     * [		*   7/16
     * 3/16 5/16    1/16]
     */
    private static int[][] convert_dithering(int[][] pixels, int height, int width, int average) {
        int[][] newPixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int point = pixels[y][x];
                int error;
                if (point > average) {
                    error = point - 255;
                    newPixels[y][x] = 0xffffffff;
                } else {
                    newPixels[y][x] = 0;
                    error = point;
                }
                if (y + 1 < height) {
                    pixels[y + 1][x] = pixels[y + 1][x] + (error * 5 >> 4);
                    if (x > 0) {
                        pixels[y + 1][x - 1] = pixels[y + 1][x - 1] + (error * 3 >> 4);
                    }
                    if (x + 1 < width) {
                        pixels[y + 1][x + 1] = pixels[y + 1][x + 1] + (error >> 4);
                    }
                }
                if (x + 1 < width) {
                    pixels[y][x + 1] = pixels[y][x + 1] + (error * 7 >> 4);
                }
            }
        }
        return newPixels;
    }

    private static void createImage(int[][] pixels, int height, int width, String url) {
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bImage.setRGB(x, y, pixels[y][x]);
            }
        }
        try {
            ImageIO.write(bImage, "jpg", new File(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 颜色分量转换为RGB值
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {
        return ((alpha << 8 | red) << 8 | green) << 8 | blue;
    }

}
