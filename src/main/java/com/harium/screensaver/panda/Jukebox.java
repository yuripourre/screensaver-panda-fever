package com.harium.screensaver.panda;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;
import java.io.File;
import java.io.IOException;


/**
 * com.harium.panda.Jukebox class to play .wav sound clips
 * Code from: https://stackoverflow.com/a/577926
 */
public class Jukebox {

    public static void play(String path) {
        final File file = new File(path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                playClip(file);
            }
        }).start();
    }

    private static void playClip(File clipFile) {
        try {
            AudioListener listener = new AudioListener();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
            try {
                Clip clip = AudioSystem.getClip();
                clip.addLineListener(listener);
                clip.open(audioInputStream);
                try {
                    clip.start();
                    listener.waitUntilDone();
                } finally {
                    clip.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } finally {
                audioInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    static class AudioListener implements LineListener {
        private boolean done = false;

        @Override
        public synchronized void update(LineEvent event) {
            Type eventType = event.getType();
            if (eventType == Type.STOP || eventType == Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }

        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) {
                wait();
            }
        }
    }

}