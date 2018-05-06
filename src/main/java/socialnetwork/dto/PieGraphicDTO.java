package socialnetwork.dto;

import java.util.List;

/**
 * Created by Roman on 26.04.2018 1:01.
 */
public class PieGraphicDTO {
    private String name;
    private List<PieSeriesDTO> data;

    public PieGraphicDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PieSeriesDTO> getData() {
        return data;
    }

    public void setData(List<PieSeriesDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PieGraphicDTO{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
