package sample.ImageGenerating;

import java.awt.image.BufferedImage;

public class Chunk
{
    private int[][] pixels;
    private int x;
    private int y;
    private int number;
    private int size;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void writeToImage(BufferedImage image){
        for(int y = 0; y < pixels.length; y++){
            for(int x = 0; x < pixels[y].length; x++){
                image.setRGB(this.x + x, this.y + y, pixels[y][x]);
            }
        }
    }
}
