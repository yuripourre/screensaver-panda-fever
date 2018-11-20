package com.harium.screensaver.panda;

import com.harium.etyl.commons.context.Application;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.KeyState;

import java.util.ArrayList;
import java.util.List;

public abstract class ScreenSaverWithPassword extends Application {

    private int exitSequence = 0;
    protected List<Integer> sequence = new ArrayList<>();

    public ScreenSaverWithPassword(int w, int h) {
        super(w, h);
    }

    @Override
    public void updateKeyboard(KeyEvent event) {
        super.updateKeyboard(event);

        if (checkSequence(event)) {
            System.exit(0);
        }
    }

    private boolean checkSequence(KeyEvent event) {
        // Ignore
        if (event.getState() != KeyState.RELEASED) {
            return false;
        }

        if (exitSequence < sequence.size()) {
            int next = sequence.get(exitSequence);
            if (event.getKey() == next) {
                exitSequence++;
            } else {
                exitSequence = 0;
            }
        }

        return exitSequence >= sequence.size();
    }

}
