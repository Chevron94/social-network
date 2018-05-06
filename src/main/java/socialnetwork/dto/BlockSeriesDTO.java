package socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Roman on 06.05.2018 18:12.
 */
public class BlockSeriesDTO {
    private String name;
    @JsonProperty("data")
    private List<Integer> values;

    public BlockSeriesDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "BlockSeriesDTO{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
