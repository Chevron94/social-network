package socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 13.09.15.
 */
@Entity
@Table(name = "COUNTRY")
public class Country
{
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name = "FLAG_URL")
    private String flagURL;

    @Column(name = "ISO")
    private String iso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONTINENT_ID", nullable = false)
    private Continent continent;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<City> cities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnore
    private List<User> users = new ArrayList<>();



    public Country() {
    }

    public Country(String name, String flagURL, Continent continent) {
        this.name = name;
        this.flagURL = flagURL;
        this.continent = continent;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return !(id != null ? !id.equals(country.id) : country.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flagURL='" + flagURL + '\'' +
                ", continent=" + continent +
                '}';
    }
}
