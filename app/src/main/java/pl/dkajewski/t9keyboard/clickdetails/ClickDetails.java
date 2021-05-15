package pl.dkajewski.t9keyboard.clickdetails;

import java.util.ArrayList;

public class ClickDetails
{
    public long previousClickedTimeMs = 0;
    public long clickedTimeMs = 0;
    public int clickedButton = 0;
    public int timesClicked = 0;
    public int cursorPosition;

    public ClickDetails(int button, int cursorPosition)
    {
        this.cursorPosition = cursorPosition;
        this.clicked(button);
    }

    public void clicked(int button)
    {
        this.timesClicked++;
        this.previousClickedTimeMs = this.clickedTimeMs;
        this.clickedButton = button;
        this.clickedTimeMs = System.currentTimeMillis();
    }

    public long getTimeBetweenClicks() {
        if (this.previousClickedTimeMs == 0) {
            return 0;
        }

        return this.clickedTimeMs - this.previousClickedTimeMs;
    }

    public String getClickedString(ArrayList<String> possibleChars)
    {
        int size = possibleChars.size();
        int characterIndex = 0;
        if (this.timesClicked%size == 0) {
            characterIndex = size - 1;
        } else {
            characterIndex = this.timesClicked%size - 1;
        }

        return possibleChars.get(characterIndex);
    }
}
