package sample;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.ImageGenerating.ImageChunking;
import sample.ImageGenerating.ImageDiff;
import sample.ImageGenerating.ScreenImageCreator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.IOException;
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
            }, 0, 10);
        }
        else
        {
            _timer.cancel();
        }
    }

    BufferedImage _lastImage;
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
            image = newImage;

            int[][] chunks = new ImageChunking(image).getChunks();
        }
        else
            _lastImage = image;
        ByteOutputStream outputStream = new ByteOutputStream();
        ImageIO.write(image, "png", outputStream);
        imageView.setImage(new Image(outputStream.newInputStream()));
    }
}
