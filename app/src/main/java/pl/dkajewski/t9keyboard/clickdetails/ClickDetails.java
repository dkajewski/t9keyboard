package pl.dkajewski.t9keyboard.clickdetails;

public class ClickDetails
{
    public long previousClickedTimeMs = 0;
    public long clickedTimeMs = 0;

    public int previousClickedButton;
    public int clickedButton;

    public ClickDetails()
    {

    }

    public void clicked(int button)
    {
        this.clickedTimeMs = System.currentTimeMillis();
        this.clickedButton = button;
    }

    public long getTimeBetweenClicks()
    {
        if (this.previousClickedTimeMs == 0) {
            return 0;
        }

        return this.clickedTimeMs - this.previousClickedTimeMs;
    }
}
