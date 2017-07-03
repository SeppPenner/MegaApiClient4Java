package megaapi.megaapiclient4java.Enumerations;

public enum NodeType {

    File(0),
    Directory(1),
    Root(2),
    Inbox(3),
    Trash(4);

    private final int value;

    NodeType(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
