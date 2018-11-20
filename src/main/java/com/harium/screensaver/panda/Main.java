package com.harium.screensaver.panda;

import com.harium.etyl.Etyl;
import com.harium.etyl.commons.context.Application;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.util.PathHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Etyl {

    public Main(int width, int height) {
        super(width, height);
    }

    public static void main(String[] args) {
        // Take picture
        takePicture();

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());

        Main app = new Main(screenRect.width, screenRect.height);
        app.setUndecorated(true);
        app.setTitle("Panda Fever");
        app.init();
        app.enableKioskMode();
    }

    @Override
    public Application startApplication() {
        return new HelloWorld(w, h);
    }

    private static void takePicture() {
        String fileName = getFilename();
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
                    .getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, "png", new File(fileName));

        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFilename() {
        File folder = new File(PathHelper.currentDirectory() + "/assets/images/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath() + "/screenshot.png";
    }

    public class HelloWorld extends ScreenSaverWithPassword {
        private ImageLayer background;

        private ScreenAnimation animation;

        public HelloWorld(int w, int h) {
            super(w, h);
        }

        @Override
        public void load() {
            animation = new ScreenAnimation(w, h);
            animation.load();

            generateExitSequence();

            String filename = getFilename();
            background = new ImageLayer(filename, true);
        }

        /**
         * Sequence to exit is "xxx"
         */
        protected void generateExitSequence() {
            sequence.add(KeyEvent.VK_X);
            sequence.add(KeyEvent.VK_X);
            sequence.add(KeyEvent.VK_X);
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.GREEN_ETYL);
            g.fillRect(0, 0, w, h);

            background.draw(g);
            animation.draw(g);
        }

        @Override
        public void updateMouse(PointerEvent event) {
            super.updateMouse(event);

            animation.updateMouse(event);
        }

    }
}