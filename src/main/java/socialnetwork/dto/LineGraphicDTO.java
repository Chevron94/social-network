package socialnetwork.dto;

import java.util.List;

/**
 * Created by Roman on 26.04.2018 1:00.
 */
public class LineGraphicDTO {
    private List<LineSeriesDTO> series;

    public LineGraphicDTO() {
    }

    public List<LineSeriesDTO> getSeries() {
        return series;
    }

    public void setSeries(List<LineSeriesDTO> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "LineGraphicDTO{" +
                "series=" + series +
                '}';
    }
}
