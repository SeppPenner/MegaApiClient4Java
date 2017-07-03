package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.IAccountInformation;
import megaapi.megaapiclient4java.Interfaces.IStorageMetrics;
import java.util.HashMap;

public class AccountInformationResponse implements IAccountInformation {

    @JsonProperty("mstrg")
    private long totalQuota;

    @Override
    public long getTotalQuota() {
        return totalQuota;
    }

    @JsonProperty("cstrg")
    private long usedQuota;

    @Override
    public long getUsedQuota() {
        return usedQuota;
    }

    @JsonProperty("cstrgn")
    private HashMap<String, long[]> metricsSerialized;

    public HashMap<String, long[]> getMetricsSerialized() {
        return metricsSerialized;
    }

    private Iterable<IStorageMetrics> metrics;

    public Iterable<IStorageMetrics> getMetrics() {
        return metrics;
    }

    [
    OnDeserialized

    ]
    public void OnDeserialized(StreamingContext context) {
        this.Metrics = this.MetricsSerialized.Select(x =  > (IStorageMetrics) new StorageMetrics(x.Key, x.Value));
    }

}
