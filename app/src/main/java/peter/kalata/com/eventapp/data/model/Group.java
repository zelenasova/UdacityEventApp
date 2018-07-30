package peter.kalata.com.eventapp.data.model;

import android.widget.EditText;

import butterknife.BindView;
import peter.kalata.com.eventapp.R;

public class Group {

    private String name;
    private String password;
    private String leaderName;
    private String leaderMail;
    private String leaderPhone;
    private String description;

    public Group() {

    }

    public Group(String name, String password, String leaderName, String leaderMail,
                 String leaderPhone, String description) {
        this.name = name;
        this.password = password;
        this.leaderName = leaderName;
        this.leaderMail = leaderMail;
        this.leaderPhone = leaderPhone;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderMail() {
        return leaderMail;
    }

    public void setLeaderMail(String leaderMail) {
        this.leaderMail = leaderMail;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}