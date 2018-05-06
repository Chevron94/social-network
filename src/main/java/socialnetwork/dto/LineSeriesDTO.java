package socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 06.05.2018 18:18.
 */
public class LineSeriesDTO {
    private String type;
    private String name;
    @JsonProperty("data")
    private List<Object[]> values;

    public LineSeriesDTO() {
        values = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object[]> getValues() {
        return values;
    }

    public void setValues(List<Object[]> values) {
        this.values = values;
    }

    public void addValue(Object x, Object y){
        values.add(new Object[]{x, y});
    }

    @Override
    public String toString() {
        return "LineSeriesDTO{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
