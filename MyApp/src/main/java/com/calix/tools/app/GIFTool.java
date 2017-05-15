package com.calix.tools.app;


import com.calix.tools.util.GifDecoder;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class GIFTool {

    private static final int STEP_X = 10;
    private static final int STEP_Y = 5;

    public void pixelToNum(String imageUrl, String outputUrl) throws IOException {
        GifDecoder decoder = new GifDecoder();
        InputStream is = new FileInputStream(imageUrl);
        if (decoder.read(is) != 0) {
            System.out.println("读取有错误");
            return;
        }
        is.close();
        int frameCount = decoder.getFrameCount();
        System.out.println("帧是数量: " + frameCount);
        //		int[][] inPixels = new int[width][height];
        for (int i = 0; i < frameCount; i++) {
            BufferedImage frame = decoder.getFrame(i);
            int delay = decoder.getDelay(i);
            int height = frame.getHeight();
            int width = frame.getWidth();
            for (int x = 0; x < width; x += STEP_X) {
                for (int y = 0; y < height; y += STEP_Y) {
                }
            }

            //			frame.getRGB(i, j);

        }

    }

    public void grey() {

    }

    public static void main(String[] args) throws IOException {
        new GIFTool().pixelToNum("/home/calix/Pictures/tools.gif", null);
    }
}
