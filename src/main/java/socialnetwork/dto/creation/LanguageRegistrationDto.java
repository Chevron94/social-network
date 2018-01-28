package socialnetwork.dto.creation;

import java.util.List;

/**
 * Created by Роман on 23.04.2016.
 */
public class LanguageRegistrationDto {
    private List<Long> languages;
    private List<Long> languageLevels;

    public LanguageRegistrationDto() {
    }

    public LanguageRegistrationDto(List<Long> languages, List<Long> languageLevels) {
        this.languages = languages;
        this.languageLevels = languageLevels;
    }

    public List<Long> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Long> languages) {
        this.languages = languages;
    }

    public List<Long> getLanguageLevels() {
        return languageLevels;
    }

    public void setLanguageLevels(List<Long> languageLevels) {
        this.languageLevels = languageLevels;
    }
}
