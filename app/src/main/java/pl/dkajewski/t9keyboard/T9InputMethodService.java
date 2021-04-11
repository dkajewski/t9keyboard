package pl.dkajewski.t9keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

import pl.dkajewski.t9keyboard.t9keyboard.T9Keyboard;

public class T9InputMethodService extends InputMethodService
{

    private T9Keyboard keyboardView;

    @Override
    public View onCreateInputView() {
        this.keyboardView = new T9Keyboard(this);
        return this.keyboardView;
    }

    public InputConnection getInputConnection()
    {
        return getCurrentInputConnection();
    }

}
