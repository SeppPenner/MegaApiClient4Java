package megaapi.megaapiclient4java.Interfaces;

import java.util.Date;

public interface INode extends INodeInfo {

    public String getParentId();

    public Date getCreationDate();

    public String getOwner();
}
