package pl.dkajewski.t9keyboard.input;

import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import pl.dkajewski.t9keyboard.R;
import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.clickdetails.ClickDetails;
import pl.dkajewski.t9keyboard.keyboard.DelayedFinishComposingText;
import pl.dkajewski.t9keyboard.keyboard.Keyboard;
import pl.dkajewski.t9keyboard.keyboard.Keys;

public abstract class InputCommon
{

    protected View button;
    protected T9InputMethodService context;
    protected ArrayList<ClickDetails> clickDetails;
    protected InputConnection inputConnection;
    protected byte shiftMode = 0;
    public static long lastClickTime = 0;
    public static long currentClickTime = 0;
    public static final long MAX_TIME_BETWEEN_CLICKS = 400;
    protected Keys keyValues;
    protected SparseArray<String> keysToButtons;
    protected Keyboard keyboard;


    public InputCommon(T9InputMethodService context, InputConnection inputConnection, SparseArray<String> keysToButtons, Keyboard keyboard)
    {

        this.context = context;
        this.clickDetails = new ArrayList<>();
        this.inputConnection = inputConnection;
        this.keyValues = new Keys("PL");
        this.keysToButtons = keysToButtons;
        this.keyboard = keyboard;
    }

    public abstract void handleInput(View button);

    /**
     * Finish composing text. Clear clickDetails field
     */
    public void finishComposingText()
    {
        this.inputConnection.finishComposingText();
        if (this.shiftMode == 1 && this.clickDetails.size() > 0) {
            this.setShiftModeAfterTextCommit();
        }

        this.clickDetails.clear();
    }

    /**
     * Method responsible for calling finishComposingText, when time between clicks is greater than MAX_TIME_BETWEEN_CLICKS
     * @param view in that case it's treated like a clicked button
     */
    protected void handleTimeIntervals(View view)
    {
        this.inputConnection = this.context.getInputConnection();
        int[] skipButtons = {R.id.button_shift, R.id.button_delete};
        boolean skipFinish = Arrays.binarySearch(skipButtons, view.getId()) >= 0;
        if (skipFinish) {
            return;
        }

        if (currentClickTime != 0) {
            lastClickTime = currentClickTime;
        }

        currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime > MAX_TIME_BETWEEN_CLICKS && lastClickTime > 0) {
            this.finishComposingText();
        }
    }

    /**
     * Sets state of the shift button and then calls shiftButtonHandler method
     */
    protected void setShiftMode()
    {
        if (this.shiftMode == 2) {
            this.shiftMode = 0;
        } else {
            this.shiftMode++;
        }

        this.shiftButtonHandler();
    }

    /**
     * Sets letter case in the keyboard depending on shiftMode field
     */
    protected void shiftButtonHandler()
    {
        if (this.shiftMode == 0 || this.shiftMode == 1) {
            // we skip index 0, because it's not abc like button
            for (int i = 1; i < this.keysToButtons.size(); i++) {
                Button button = this.keyboard.findViewById(this.keysToButtons.keyAt(i));
                String text = button.getText().toString();
                if (this.shiftMode == 0) {
                    button.setText(text.toLowerCase());
                } else {
                    button.setText(text.toUpperCase());
                }
            }
        }
    }

    /**
     * Deletes one character from input
     */
    protected void deleteButtonHandler()
    {
        this.finishComposingText();
        CharSequence selectedText = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(selectedText)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 1);
        }
    }

    /**
     * Method calculates current cursor position based on full text input length and text before cursor
     * @return current cursor position
     */
    protected int getCursorPosition()
    {
        //get the total text first
        String fullText = this.inputConnection.getExtractedText(new ExtractedTextRequest(),0).text.toString();
        //get whole text before the cursor
        String beforeText = this.inputConnection.getTextBeforeCursor(fullText.length(),0).toString();

        return beforeText.length();
    }

    /**
     * Method adds space to current input selection and sets cursor position to be after entered space
     * @param cursorPosition current cursor position
     */
    protected void addSpace(int cursorPosition)
    {
        this.inputConnection.commitText(" ", cursorPosition);
        this.inputConnection.setSelection(cursorPosition+1, cursorPosition+1);
    }

    /**
     * Method returns character to be entered
     * @param key keyboard key from which character will be retrieved
     * @return text/character assigned to the key given in the parameter
     */
    protected String getTextToCommit(String key)
    {
        ArrayList<String> chars = this.keyValues.getKeyChars(key);
        String text = this.clickDetails.get(0).getClickedString(chars);
        if (this.shiftMode == 1 || this.shiftMode == 2) {
            text = text.toUpperCase();
        } else {
            text = text.toLowerCase();
        }

        return text;
    }

    /**
     * Sets shift button mode when text is committed
     */
    protected void setShiftModeAfterTextCommit()
    {
        if (this.shiftMode == 1) {
            this.shiftMode = 0;
            this.shiftButtonHandler();
        }
    }

    /**
     * Launches a handler to finish composing text if there's no action
     */
    protected void delayedTextCommit()
    {
        Handler handler = new Handler();
        handler.postDelayed(new DelayedFinishComposingText(this), MAX_TIME_BETWEEN_CLICKS + 50);
    }
}
