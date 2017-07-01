package megaapi.megaapiclient4java.Interfaces;

public interface IAccountInformation{
    
    public long getTotalQuota();

    public long getUsedQuota();

    public Iterable<IStorageMetrics> getMetrics();
}