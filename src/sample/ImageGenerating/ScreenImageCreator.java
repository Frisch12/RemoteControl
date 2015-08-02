package sample.ImageGenerating;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenImageCreator
{
    private Robot robot;
    public ScreenImageCreator(){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getScreenshot(){
        return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }
}
