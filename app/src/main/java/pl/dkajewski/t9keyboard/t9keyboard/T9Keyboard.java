package pl.dkajewski.t9keyboard.t9keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import pl.dkajewski.t9keyboard.R;
import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.clickdetails.ClickDetails;

public class T9Keyboard extends LinearLayout implements View.OnClickListener {

    private Button button1, button2, button3, button4,
            button5, button6, button7, button8,
            button9, button0, buttonDelete, buttonEnter;
    private T9InputMethodService context;

    private InputConnection inputConnection;
    private ClickDetails clickDetails;
    private T9Keys keyValues;
    private SparseArray<String> keysToButtons = new SparseArray<>();


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
        this.clickDetails = new ClickDetails();
        this.keyValues = new T9Keys("PL");
        this.setKeysToButtons();
    }

    private void init(Context context, AttributeSet attrs)
    {
        this.initT9Buttons(context);
    }

    @Override
    public void onClick(View view) {
        inputConnection = this.context.getInputConnection();
        this.clickDetails.clicked(view.getId());

        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.button_delete:
                CharSequence selectedText = inputConnection.getSelectedText(0);

                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;
            case R.id.button_emoticon:
                break;
            default:
                String letter = this.getTextToCommit(this.keysToButtons.get(buttonId));
                inputConnection.commitText(letter, 1);
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
    }

    private String getTextToCommit(String key)
    {
        ArrayList<String> chars = this.keyValues.getKeyChars(key);
        return chars.get(0);
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
}