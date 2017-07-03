package megaapi.megaapiclient4java.Interfaces;

import java.util.Date;

public interface INode extends INodeInfo {

    public String ParentId();

    public Date CreationDate();

    public String Owner();
}
