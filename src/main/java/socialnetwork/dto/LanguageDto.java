package socialnetwork.dto;

/**
 * Created by Roman on 30.08.2017 19:39.
 */
public class LanguageDto {
    private String name;
    private Long level;
    private String levelName;

    public LanguageDto() {
    }

    public LanguageDto(String name, Long level, String levelName) {
        this.name = name;
        this.level = level;
        this.levelName = levelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public String toString() {
        return "LanguageDto{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", levelName='" + levelName + '\'' +
                '}';
    }
}
