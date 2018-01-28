package socialnetwork.dto.creation;

/**
 * Created by Roman on 28.01.2018 17:01.
 */
public class VocabularyCreationDto {
    private Long userId;
    private Long from;
    private Long to;

    public VocabularyCreationDto() {
    }

    public VocabularyCreationDto(Long userId, Long from, Long to) {
        this.userId = userId;
        this.from = from;
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VocabularyCreationDto{" +
                "userId=" + userId +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
