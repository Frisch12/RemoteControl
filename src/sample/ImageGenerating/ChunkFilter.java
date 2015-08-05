package sample.ImageGenerating;

import com.sun.glass.ui.Size;

import java.util.ArrayList;
import java.util.List;

public class ChunkFilter {
    private Chunk[] chunks;
    private Size imageSize;

    public ChunkFilter(Chunk[] chunks, Size imageSize) {
        this.chunks = chunks;
        this.imageSize = imageSize;
    }

    public Chunk[] getFiltered(int[] diff1) {
        List<Chunk> result = new ArrayList<>();
        for(Chunk chk : chunks){
            boolean changed = false;
            for (int y = 0; y < chk.getSize(); y++){
                for(int x = 0; x < chk.getSize(); x++){
                    if(diff1[chk.getX() + x + chk.getY() * imageSize.width + y * imageSize.width] > 0)
                        changed = true;
                }
            }
            if(changed)
                result.add(chk);
        }
        return result.toArray(new Chunk[result.size()]);
    }
}
