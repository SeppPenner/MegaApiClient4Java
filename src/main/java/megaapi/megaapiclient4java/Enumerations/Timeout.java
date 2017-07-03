package megaapi.megaapiclient4java.Enumerations;

public enum Timeout {
    
    Infinite(-1);

    private final int value;

    Timeout(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
