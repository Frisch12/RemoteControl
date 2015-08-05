package sample.ImageGenerating;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageDiff
{
    private int[] pixels;
    private int[] oldPixels;

    public ImageDiff(BufferedImage image1, BufferedImage image2){
        pixels = ((DataBufferInt)image1.getData().getDataBuffer()).getData();
        oldPixels = ((DataBufferInt)image2.getData().getDataBuffer()).getData();
    }

    public int[] getDiff(){
        int[] result = new int[pixels.length];
        for (int x = 0; x < result.length; x++) {
            int i = pixels[x] - oldPixels[x];
            if(i < 0)
                i = -i;
            result[x] = i;
        }
        return result;
    }
}
