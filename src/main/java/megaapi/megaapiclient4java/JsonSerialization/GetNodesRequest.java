package megaapi.megaapiclient4java.JsonSerialization;

public class GetNodesRequest extends RequestBase {

    public GetNodesRequest(String shareId){
        super("f");
        this.c = 1;
        if (shareId != null) {
            super.setQueryArguments("n", shareId);
        }
    }

    private final int c;

    public int getC() {
        return c;
    }
}
