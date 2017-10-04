package socialnetwork.dto;

import socialnetwork.entities.Dialog;
import socialnetwork.entities.User;
import socialnetwork.entities.UserDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Roman on 30.08.2017 20:14.
 */
public class DialogDto {
    private Long id;
    private String name;
    private Boolean isPrivate;
    private Date lastMessage;
    private List<UserDto> users;

    public DialogDto() {
    }

    public DialogDto(Dialog dialog) {
        this.id = dialog.getId();
        this.name = dialog.getName();
        this.isPrivate = dialog.getPrivate();
        this.lastMessage = dialog.getLastMessageDate();
        this.users = new ArrayList<>();
        for (UserDialog userDialog: dialog.getUserDialogs()){
            User user = userDialog.getUser();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setLogin(user.getLogin());
            userDto.setName(user.getName());
            userDto.setPhotoURL(user.getPhotoURL());
            userDto.setOnline(user.getOnline());
            userDto.setCity(user.getCity().getName());
            userDto.setCountry(user.getCountry().getName());
            userDto.setCountryFlag(user.getCountry().getFlagURL());
            users.add(userDto);
        }
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "DialogDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", lastMessage=" + lastMessage +
                ", users=" + users +
                '}';
    }
}
