package megaapi.megaapiclient4java.JsonSerialization;

public class GetNodesRequest implements RequestBase{
    
    public GetNodesRequest(String shareId = null): base("f"){
        this.c = 1;
        if (shareId != null){
            this.QueryArguments["n"] = shareId;
        }
    }
    
    private final int c;
    
    public int getC(){
        return c;
    }
}