package megaapi.megaapiclient4java.Interfaces;

import megaapi.megaapiclient4java.Enumerations.NodeType;
import java.util.Date;

public interface INodeInfo {

    public String getId();

    public NodeType getType();

    public String getName();

    public long getSize();

    public Date getModificationDate();
}
