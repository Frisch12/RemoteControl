package sample.ImageGenerating;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageChunking
{
    public static int ChunkSize = 512;

    private int[] data;
    private int height = 0;
    private int width = 0;

    public ImageChunking(BufferedImage image){
        this.data = ((DataBufferInt)image.getData().getDataBuffer()).getData();
        height = image.getHeight();
        width = image.getWidth();
        ChunkSize = euclid(image.getHeight(), image.getWidth());
    }

    public Chunk[] getChunks(){
        int chunks = (int) Math.ceil(data.length / (double) (ChunkSize*ChunkSize));
        Chunk[] result = new Chunk[chunks];

        int chunksHorizontal = width / ChunkSize;
        for(int i = 0; i < chunks; i++){
            int[][] chunk = new int[ChunkSize][ChunkSize];
            for(int y = 0; y < ChunkSize; y++){
                chunk[y] = new int[ChunkSize];
                for(int x = 0; x < ChunkSize; x++){
                    chunk[y][x] = data[((i % chunksHorizontal) * ChunkSize) + x + ((int)Math.floor(i / chunksHorizontal) * width * ChunkSize) + y * width];
                }
            }
            Chunk chkObj = new Chunk();
            chkObj.setSize(ChunkSize);
            chkObj.setPixels(chunk);
            chkObj.setNumber(i);
            chkObj.setX((i % chunksHorizontal) * ChunkSize);
            chkObj.setY((int)Math.floor(i / chunksHorizontal) * ChunkSize);
            result[i] = chkObj;
        }

        return result;
    }

    private int euclid(int a, int b)
    {
        if(b == 0)
            return a;
        return euclid(b, a % b);
    }
}
