package sample;

import com.sun.glass.ui.Size;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.ImageGenerating.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends Controller {
    @FXML
    public ImageView imageView;

    private Timer _timer = new Timer();

    private Boolean _started = false;
    private Robot robot;
    private ScreenImageCreator creator = new ScreenImageCreator();

    @Override
    public void onShutdown() {
        super.onShutdown();
        _timer.cancel();
    }

    @FXML
    public void doSomething() {
        _started = !_started;
        if(_started) {
            _timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        takeAndSetScreenshot();
                    } catch (AWTException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 50);
        }
        else
        {
            _timer.cancel();
        }
    }

    BufferedImage _lastImage;
    BufferedImage _toChange;
    long lastTime = 0;
    private void takeAndSetScreenshot() throws AWTException, IOException {
        if(robot == null)
            robot = new Robot();
        BufferedImage image = creator.getScreenshot();

        if(_lastImage != null){
            ImageDiff diff = new ImageDiff(image, _lastImage);
            int[] diff1 = diff.getDiff();

            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

            newImage.setData(Raster.createRaster(newImage.getData().getSampleModel(), new DataBufferInt(diff1, diff1.length), new Point(0, 0)));
            _lastImage = image;

            Chunk[] chunks = new ImageChunking(image).getChunks();

            Chunk[] filtered = new ChunkFilter(chunks, new Size(image.getWidth(), image.getHeight())).getFiltered(diff1);

            for (Chunk chunk : filtered) {
                chunk.writeToImage(_toChange);
            }
        }
        else{
            _lastImage = image;
            _toChange = image;
        }

        double scale = 0.25;
        int w = (int) (_toChange.getWidth() * scale);
        int h = (int) (_toChange.getHeight() * scale);
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        after = scaleOp.filter(_toChange, after);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(after, "png", outputStream);

        imageView.setImage(new Image(new ByteArrayInputStream(outputStream.toByteArray())));
        lastTime = System.nanoTime();
        System.gc();
    }
}
