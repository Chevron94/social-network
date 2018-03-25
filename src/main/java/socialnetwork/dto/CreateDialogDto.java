package socialnetwork.dto;

import java.util.Set;

/**
 * Created by Roman on 10.03.2018 12:46.
 */
public class CreateDialogDto {
    private String name;
    private Set<Long> users;

    public CreateDialogDto() {
    }

    public CreateDialogDto(String name, Set<Long> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getUsers() {
        return users;
    }

    public void setUsers(Set<Long> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "CreateDialogDto{" +
                "name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
