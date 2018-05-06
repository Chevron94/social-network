package socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Roman on 26.04.2018 1:05.
 */
public class PieSeriesDTO {
    private String name;
    @JsonProperty(value = "y")
    private Integer value;

    public PieSeriesDTO() {
    }

    public PieSeriesDTO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PieSeriesDTO{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
