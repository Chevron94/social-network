package socialnetwork.dto;

import socialnetwork.entities.LanguageUser;
import socialnetwork.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Roman on 30.08.2017 19:32.
 */
public class UserDto {
    private Long id;
    private String login;
    private String name;
    private Boolean online;
    private Date birthday;
    private String photoURL;
    private String description;
    private String gender;
    private String country;
    private String countryFlag;
    private String city;
    private AlbumDto album;
    private List<LanguageDto> languages;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.online = user.getOnline();
        this.birthday = user.getBirthday();
        this.gender = user.getGender().getName();
        this.photoURL = user.getPhotoURL();
        this.description = user.getDescription();
        this.country = user.getCountry().getName();
        this.countryFlag = user.getCountry().getFlagURL();
        this.city = user.getCity().getName();
        this.album = new AlbumDto(user.getAlbums().get(0));
        this.languages = new ArrayList<>();
        for (LanguageUser languageUser: user.getLanguageUsers()) {
            this.languages.add(new LanguageDto(languageUser.getLanguage().getName(),
                    languageUser.getLanguageLevel().getId(),
                    languageUser.getLanguageLevel().getName()));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AlbumDto getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDto album) {
        this.album = album;
    }

    public List<LanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageDto> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", online=" + online +
                ", birthday=" + birthday +
                ", photoURL='" + photoURL + '\'' +
                ", description='" + description + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", countryFlag='" + countryFlag + '\'' +
                ", city='" + city + '\'' +
                ", album=" + album +
                ", languages=" + languages +
                '}';
    }
}
