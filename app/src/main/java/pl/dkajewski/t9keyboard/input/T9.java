package pl.dkajewski.t9keyboard.input;

import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputConnection;

import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.keyboard.Keyboard;

public class T9 extends InputCommon
{

    public T9(T9InputMethodService context, InputConnection inputConnection, SparseArray<String> keysToButtons, Keyboard keyboard) {
        super(context, inputConnection, keysToButtons, keyboard);
    }

    @Override
    public void handleInput(View view) {

    }
}
