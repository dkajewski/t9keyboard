package pl.dkajewski.t9keyboard.input;

import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

import pl.dkajewski.t9keyboard.R;
import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.clickdetails.ClickDetails;
import pl.dkajewski.t9keyboard.keyboard.Keyboard;

public class Multitap extends InputCommon
{


    public Multitap(T9InputMethodService context, InputConnection inputConnection, SparseArray<String> keysToButtons, Keyboard keyboard) {
        super(context, inputConnection, keysToButtons, keyboard);
    }

    @Override
    public void handleInput(View button)
    {
        this.button = button;
        this.handleTimeIntervals(button);
        this.multiTapInputMethodHandler(this.button);
    }

    private void multiTapInputMethodHandler(View view)
    {
        inputConnection = this.context.getInputConnection();
        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.button_delete:
                this.deleteButtonHandler();
                break;
            case R.id.custom_1: case R.id.custom_2: case R.id.custom_3: case R.id.custom_4: case R.id.custom_5: case R.id.custom_6:
                this.finishComposingText();
                String buttonText = ((Button) view).getText().toString();
                inputConnection.commitText(buttonText, 1);
                break;
            case R.id.button_space:
                int currentCursorPos = 0;
                if (this.clickDetails.size() > 0) {
                    currentCursorPos = this.clickDetails.get(this.clickDetails.size()-1).cursorPosition;
                } else {
                    currentCursorPos = this.getCursorPosition();
                }

                this.finishComposingText();
                this.addSpace(currentCursorPos);
                this.finishComposingText();
                break;
            case R.id.button_shift :
                this.setShiftMode();
                break;
            default:
                this.handleMultiTapLettersInput(buttonId);
                break;
        }
    }

    /**
     * Method detects if clicked button was clicked for the nth time (then sets text in composition) or was clicked new button
     * (then commits previous character and sets composition for new one). Manages cursor position to be always after entered character
     * @param buttonId id of clicked button
     */
    private void handleMultiTapLettersInput(int buttonId)
    {
        int cursorPosition = this.getCursorPosition();
        if (this.clickDetails.size() == 0 || this.clickDetails.get(this.clickDetails.size()-1).clickedButton != buttonId) {
            if (this.clickDetails.size() > 0) {
                this.finishComposingText();
                this.setShiftModeAfterTextCommit();
            }

            this.clickDetails.add(new ClickDetails(buttonId, cursorPosition+1));
        } else {
            ClickDetails lastClickedBtn = this.clickDetails.get(this.clickDetails.size()-1);
            this.clickDetails.clear();
            this.clickDetails.add(lastClickedBtn);
            this.clickDetails.get(0).clicked(buttonId);
        }

        ClickDetails clicked = this.clickDetails.get(this.clickDetails.size()-1);
        String text = this.getTextToCommit(this.keysToButtons.get(clicked.clickedButton));
        this.inputConnection.setComposingText(text, cursorPosition);
        this.inputConnection.setSelection(clicked.cursorPosition, clicked.cursorPosition);
        this.delayedTextCommit();
    }
}
