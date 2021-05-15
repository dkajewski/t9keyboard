package pl.dkajewski.t9keyboard.keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import pl.dkajewski.t9keyboard.R;
import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.clickdetails.ClickDetails;

public class Keyboard extends RelativeLayout implements View.OnClickListener {

    private Button button1, button2, button3, button4,
            button5, button6, button7, button8,
            button9, button0, buttonDelete, buttonEnter,
            customBtn1, customBtn2, customBtn3, customBtn4, customBtn5, customBtn6,
            buttonSpace;
    private T9InputMethodService context;

    private InputConnection inputConnection;
    private ArrayList<ClickDetails> clickDetails;
    private Keys keyValues;
    private SparseArray<String> keysToButtons = new SparseArray<>();

    private static long lastClickTime = 0;
    private static long currentClickTime = 0;
    public static final long MAX_TIME_BETWEEN_CLICKS = 400;

    public Keyboard(Context context) {
        this(context, null, 0);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = (T9InputMethodService) context;
        this.init(context, attrs);
        this.clickDetails = new ArrayList<>();
        this.keyValues = new Keys("PL");
        this.setKeysToButtons();
        this.inputConnection = this.context.getInputConnection();
    }

    private void init(Context context, AttributeSet attrs)
    {
        this.initT9Buttons(context);
    }

    @Override
    public void onClick(View view)
    {
        this.inputConnection = this.context.getInputConnection();
        if (currentClickTime != 0) {
            lastClickTime = currentClickTime;
        }

        currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime > MAX_TIME_BETWEEN_CLICKS) {
            this.finishComposingText();
        }

        this.multiTapInputMethodHandler(view);
    }

    private void multiTapInputMethodHandler(View view)
    {
        inputConnection = this.context.getInputConnection();
        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.button_delete:
                this.finishComposingText();
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
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
            default:
                this.handleMultiTapLettersInput(buttonId);
                break;
        }
    }

    private void initT9Buttons(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.keyboard_view, this, true);
        button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(this);
        button4 = findViewById(R.id.button_4);
        button4.setOnClickListener(this);
        button5 = findViewById(R.id.button_5);
        button5.setOnClickListener(this);
        button6 = findViewById(R.id.button_6);
        button6.setOnClickListener(this);
        button7 = findViewById(R.id.button_7);
        button7.setOnClickListener(this);
        button8 = findViewById(R.id.button_8);
        button8.setOnClickListener(this);
        button9 = findViewById(R.id.button_9);
        button9.setOnClickListener(this);
//        button0 = findViewById(R.id.button_0);
//        button0.setOnClickListener(this);
        buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(this);
//        buttonEnter = findViewById(R.id.button_enter);
//        buttonEnter.setOnClickListener(this);
        buttonSpace = findViewById(R.id.button_space);
        buttonSpace.setOnClickListener(this);
        customBtn1 = findViewById(R.id.custom_1);
        customBtn1.setOnClickListener(this);
        customBtn2 = findViewById(R.id.custom_2);
        customBtn2.setOnClickListener(this);
        customBtn3 = findViewById(R.id.custom_3);
        customBtn3.setOnClickListener(this);
        customBtn4 = findViewById(R.id.custom_4);
        customBtn4.setOnClickListener(this);
        customBtn5 = findViewById(R.id.custom_5);
        customBtn5.setOnClickListener(this);
        customBtn6 = findViewById(R.id.custom_6);
        customBtn6.setOnClickListener(this);

    }

    /**
     * Method returns character to be entered
     * @param key keyboard key from which character will be retrieved
     * @return text/character assigned to the key given in the parameter
     */
    private String getTextToCommit(String key)
    {
        ArrayList<String> chars = this.keyValues.getKeyChars(key);
        return this.clickDetails.get(0).getClickedString(chars);
    }

    /**
     * Method sets values of keysToButtonsField field (button id to corresponding key)
     */
    private void setKeysToButtons()
    {
        this.keysToButtons.put(R.id.button_1, "1");
        this.keysToButtons.put(R.id.button_2, "2");
        this.keysToButtons.put(R.id.button_3, "3");
        this.keysToButtons.put(R.id.button_4, "4");
        this.keysToButtons.put(R.id.button_5, "5");
        this.keysToButtons.put(R.id.button_6, "6");
        this.keysToButtons.put(R.id.button_7, "7");
        this.keysToButtons.put(R.id.button_8, "8");
        this.keysToButtons.put(R.id.button_9, "9");
    }

    /**
     * Method calculates current cursor position based on full text input length and text before cursor
     * @return current cursor position
     */
    private int getCursorPosition()
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
    private void addSpace(int cursorPosition)
    {
        this.inputConnection.commitText(" ", cursorPosition);
        this.inputConnection.setSelection(cursorPosition+1, cursorPosition+1);
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
    }

    /**
     * Finish composing text. Clear clickDetails field
     */
    private void finishComposingText()
    {
        this.inputConnection.finishComposingText();
        this.clickDetails.clear();
    }

}