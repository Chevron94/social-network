package socialnetwork.dto;

/**
 * Created by Roman on 21.01.2018 12:02.
 */
public class VocabularySearchDto {
    private Long userId;
    private Long fromId;
    private Long toId;

    public VocabularySearchDto() {
    }

    public VocabularySearchDto(Long userId, Long fromId, Long toId) {
        this.userId = userId;
        this.fromId = fromId;
        this.toId = toId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "VocabularySearchDto{" +
                "userId=" + userId +
                ", fromId=" + fromId +
                ", toId=" + toId +
                '}';
    }
}
