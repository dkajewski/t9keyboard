package pl.dkajewski.t9keyboard.keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Keys
{

    private HashMap<String, ArrayList<String>> keyToValues;

    public Keys(String language)
    {
        this.initKeyToValues(language);
    }

    public ArrayList<String> getKeyChars(String key)
    {
        return this.keyToValues.get(key);
    }

    public void initKeyToValues(String language)
    {
        switch (language) {
            case "PL": this.initKeysPL(); break;
            case "EN":
            default: this.initKeysEN(); break;
        }
    }

    public void initKeysPL()
    {
        this.keyToValues = new HashMap<>();
        this.keyToValues = this.getCommonKeys();
        this.addPolishLetters();
        this.addNumbers();
    }

    public void initKeysEN()
    {
        this.keyToValues = new HashMap<>();
        this.keyToValues = this.getCommonKeys();
        this.addNumbers();
    }

    private HashMap<String, ArrayList<String>> getCommonKeys()
    {
        HashMap<String, ArrayList<String>> commonKeys = new HashMap<>();
        ArrayList<String> _1 = new ArrayList<>();
        _1.add("@");
        _1.add("'");
        ArrayList<String> _2 = new ArrayList<>();
        _2.add("a");
        _2.add("b");
        _2.add("c");
        ArrayList<String> _3 = new ArrayList<>();
        _3.add("d");
        _3.add("e");
        _3.add("f");
        ArrayList<String> _4 = new ArrayList<>();
        _4.add("g");
        _4.add("h");
        _4.add("i");
        ArrayList<String> _5 = new ArrayList<>();
        _5.add("j");
        _5.add("k");
        _5.add("l");
        ArrayList<String> _6 = new ArrayList<>();
        _6.add("m");
        _6.add("n");
        _6.add("o");
        ArrayList<String> _7 = new ArrayList<>();
        _7.add("p");
        _7.add("q");
        _7.add("r");
        _7.add("s");
        ArrayList<String> _8 = new ArrayList<>();
        _8.add("t");
        _8.add("u");
        _8.add("v");
        ArrayList<String> _9 = new ArrayList<>();
        _9.add("w");
        _9.add("x");
        _9.add("y");
        _9.add("z");
        commonKeys.put("1", _1);
        commonKeys.put("2", _2);
        commonKeys.put("3", _3);
        commonKeys.put("4", _4);
        commonKeys.put("5", _5);
        commonKeys.put("6", _6);
        commonKeys.put("7", _7);
        commonKeys.put("8", _8);
        commonKeys.put("9", _9);

        return commonKeys;
    }

    private void addNumbers()
    {
        for (Map.Entry<String, ArrayList<String>> item: this.keyToValues.entrySet()) {
            item.getValue().add(item.getKey());
        }
    }

    private void addPolishLetters()
    {
        for (Map.Entry<String, ArrayList<String>> item: this.keyToValues.entrySet()) {
            switch (item.getKey()) {
                case "2":
                    item.getValue().add("ą");
                    item.getValue().add("ć");
                    break;
                case "3":
                    item.getValue().add("ę");
                    break;
                case "5":
                    item.getValue().add("ł");
                    break;
                case "6":
                    item.getValue().add("ń");
                    item.getValue().add("ó");
                    break;
                case "7":
                    item.getValue().add("ś");
                    break;
                case "9":
                    item.getValue().add("ź");
                    item.getValue().add("ż");
                    break;
                default: break;

            }
        }
    }

}
