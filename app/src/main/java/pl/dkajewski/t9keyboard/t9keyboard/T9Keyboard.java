package pl.dkajewski.t9keyboard.t9keyboard;

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

public class T9Keyboard extends RelativeLayout implements View.OnClickListener {

    private Button button1, button2, button3, button4,
            button5, button6, button7, button8,
            button9, button0, buttonDelete, buttonEnter,
            customBtn1, customBtn2, customBtn3, customBtn4, customBtn5, customBtn6,
            buttonSpace;
    private T9InputMethodService context;

    private InputConnection inputConnection;
    private ArrayList<ClickDetails> clickDetails;
    private T9Keys keyValues;
    private SparseArray<String> keysToButtons = new SparseArray<>();
    private int previousClickedButtonsCount = 0;

    public T9Keyboard(Context context) {
        this(context, null, 0);
    }

    public T9Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public T9Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = (T9InputMethodService) context;
        this.init(context, attrs);
        this.clickDetails = new ArrayList<>();
        this.keyValues = new T9Keys("PL");
        this.setKeysToButtons();
    }

    private void init(Context context, AttributeSet attrs)
    {
        this.initT9Buttons(context);
    }

    @Override
    public void onClick(View view)
    {
        this.multiTapInputMethodHandler(view);
    }

    private void multiTapInputMethodHandler(View view)
    {
        inputConnection = this.context.getInputConnection();
        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.button_delete:
                inputConnection.finishComposingText();
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (this.clickDetails.size() > 0) {
                    this.clickDetails.clear();
                }
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;
            case R.id.custom_1: case R.id.custom_2: case R.id.custom_3: case R.id.custom_4: case R.id.custom_5: case R.id.custom_6:
                this.inputConnection.finishComposingText();
                String buttonText = ((Button) view).getText().toString();
                inputConnection.commitText(buttonText, 1);
                break;
            case R.id.button_space:
                this.inputConnection.finishComposingText();
                this.addSpace();
                this.clickDetails.clear();
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

    private String getTextToCommit(String key, int currentCharacter)
    {
        ArrayList<String> chars = this.keyValues.getKeyChars(key);
        return this.clickDetails.get(currentCharacter).getClickedString(chars);
    }

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

    private int getCursorPosition()
    {
        //get the total text first
        String fullText = this.inputConnection.getExtractedText(new ExtractedTextRequest(),0).text.toString();
        //get whole text before the cursor
        String beforeText = this.inputConnection.getTextBeforeCursor(fullText.length(),0).toString();

        return beforeText.length();
    }

    private void addSpace()
    {
        this.inputConnection.commitText(" ", this.getCursorPosition());
    }

    private void handleMultiTapLettersInput(int buttonId)
    {
        //todo: fix cursor bug
        this.previousClickedButtonsCount = this.clickDetails.size();
        if (this.previousClickedButtonsCount == 0 || buttonId != this.clickDetails.get(this.previousClickedButtonsCount - 1).clickedButton) {
            this.clickDetails.add(new ClickDetails(buttonId));
        } else {
            this.clickDetails.get(this.previousClickedButtonsCount - 1).clicked(buttonId);
        }

        if (this.previousClickedButtonsCount < this.clickDetails.size()) {
            if (this.previousClickedButtonsCount != 0) {
                String letter = this.getTextToCommit(this.keysToButtons.get(this.clickDetails.get(this.previousClickedButtonsCount - 1).clickedButton), this.previousClickedButtonsCount - 1);
                inputConnection.commitText(letter, this.getCursorPosition());
            }

            String letter = this.getTextToCommit(this.keysToButtons.get(buttonId), this.clickDetails.size()-1);
            inputConnection.setComposingText(letter, this.getCursorPosition());
        } else {
            ClickDetails currentKey = this.clickDetails.get(this.clickDetails.size() - 1);
            if (currentKey.getTimeBetweenClicks() > ClickDetails.MAX_TIME_BETWEEN_CLICKS) {
                if (currentKey.timesClicked > 1) {
                    currentKey.timesClicked--;
                }

                String letter = this.getTextToCommit(this.keysToButtons.get(currentKey.clickedButton), this.clickDetails.size()-1);
                inputConnection.commitText(letter, this.getCursorPosition());
                currentKey.timesClicked = 1;
            }

            String letter = this.getTextToCommit(this.keysToButtons.get(buttonId), this.clickDetails.size()-1);
            inputConnection.setComposingText(letter, this.getCursorPosition());
        }
    }
}