package socialnetwork.dto;

import java.util.List;

/**
 * Created by Roman on 26.04.2018 1:01.
 */
public class BlockGraphicDTO {
    private List<String> categories;
    private List<BlockSeriesDTO> series;

    public BlockGraphicDTO() {
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<BlockSeriesDTO> getSeries() {
        return series;
    }

    public void setSeries(List<BlockSeriesDTO> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "BlockGraphicDTO{" +
                "categories=" + categories +
                ", series=" + series +
                '}';
    }
}
