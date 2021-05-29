package pl.dkajewski.t9keyboard.keyboard;

import android.util.Log;

import pl.dkajewski.t9keyboard.input.InputCommon;

public class DelayedFinishComposingText implements Runnable
{
    private InputCommon keyboard;

    public DelayedFinishComposingText(InputCommon keyboard)
    {
        this.keyboard = keyboard;
    }

    @Override
    public void run()
    {
        if (System.currentTimeMillis() - InputCommon.currentClickTime > InputCommon.MAX_TIME_BETWEEN_CLICKS) {
            this.keyboard.finishComposingText();
        }
    }
}
