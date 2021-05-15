package pl.dkajewski.t9keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

import pl.dkajewski.t9keyboard.keyboard.Keyboard;

public class T9InputMethodService extends InputMethodService
{

    private Keyboard keyboardView;

    @Override
    public View onCreateInputView() {
        this.keyboardView = new Keyboard(this);
        return this.keyboardView;
    }

    public InputConnection getInputConnection()
    {
        return getCurrentInputConnection();
    }

}
