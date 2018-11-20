package com.harium.screensaver.panda;

import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.event.PointerState;
import com.harium.etyl.core.animation.Animation;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.layer.ImageLayer;
import com.harium.etyl.layer.StaticLayer;
import com.harium.etyl.util.PathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScreenAnimation {

    private final int w, h;
    private static final int MAX_LAYERS = 300;
    private static final int ADD_DELAY = 80;

    private String soundPath;

    private long when = 0;
    private long lastAdded = 0;

    private boolean startAnimation = false;

    private StaticLayer panda;
    private StaticLayer evilPanda;

    private int eventCount = 0;

    private List<ImageLayer> layers = new ArrayList<>();

    Random random = new Random();

    public ScreenAnimation(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void load() {
        lastAdded = 0;
        panda = new StaticLayer("panda.png");
        evilPanda = new StaticLayer("evil_panda.jpg");

        soundPath = PathHelper.currentDirectory() + "/assets/sounds/pop.wav";
    }

    public void updateMouse(PointerEvent event) {
        if (PointerState.MOVE == event.getState()) {
            eventCount++;

            if (eventCount > 3) {
                if (!startAnimation) {
                    startAnimation = true;
                    when = System.currentTimeMillis();
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (!startAnimation) {
            return;
        }

        if (layers.size() < MAX_LAYERS) {
            long now = System.currentTimeMillis();
            if (lastAdded + ADD_DELAY < now) {
                layers.add(generateLayer());
                playPop();
                lastAdded = now;
            }
        }

        for (ImageLayer layer : layers) {
            layer.draw(g);
        }

    }

    private ImageLayer generateLayer() {
        int pw = panda.getW();
        int ph = panda.getH();
        int hw = pw / 2;
        int hh = ph / 2;

        int x = random.nextInt(w + pw) - hw;
        int y = random.nextInt(h + ph) - hh;

        float initialScale = 0.1f;
        ImageLayer layer = new ImageLayer(x, y);
        layer.setPath(panda.getPath());
        layer.setScale(initialScale);

        int animationTime = 200;
        int maxAngle = 30;
        float angle = random.nextInt(maxAngle * 2);
        angle -= maxAngle;

        Animation.animate(layer).rotate(animationTime).to(angle).and()
                .scale(animationTime).from(initialScale).to(1.2f).then()
                .scale(50).from(1.2f).to(1f)
                .start();

        // And rotate
        return layer;
    }

    private void playPop() {
        Jukebox.play(soundPath);
    }
}
