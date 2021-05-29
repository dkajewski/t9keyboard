package pl.dkajewski.t9keyboard.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.RelativeLayout;
import pl.dkajewski.t9keyboard.R;
import pl.dkajewski.t9keyboard.T9InputMethodService;
import pl.dkajewski.t9keyboard.input.InputCommon;
import pl.dkajewski.t9keyboard.input.Multitap;

public class Keyboard extends RelativeLayout implements View.OnClickListener {

    private Button button1, button2, button3, button4,
            button5, button6, button7, button8,
            button9, button0, buttonDelete, buttonEnter,
            customBtn1, customBtn2, customBtn3, customBtn4, customBtn5, customBtn6,
            buttonSpace, buttonShift;
    private T9InputMethodService context;

    private InputConnection inputConnection;


    private SparseArray<String> keysToButtons = new SparseArray<>();

    private InputCommon inputMethod;


    public Keyboard(Context context) {
        this(context, null, 0);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = (T9InputMethodService) context;
        this.initT9Buttons(context);


        this.setKeysToButtons();
        this.inputConnection = this.context.getInputConnection();
        this.inputMethod = new Multitap(this.context, this.inputConnection, this.keysToButtons, this);
    }

    @Override
    public void onClick(View view)
    {
        this.inputMethod.handleInput(view);
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
        buttonShift = findViewById(R.id.button_shift);
        buttonShift.setOnClickListener(this);
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

}