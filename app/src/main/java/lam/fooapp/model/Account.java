package lam.fooapp.model;

/**
 * Created by a.lam.tuan on 23. 5. 2018.
 */

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Account {


    private String id;
    private String email;

    private String password;

    private String username;
    private boolean enabled = true;


    private List<String> roles;


    public String getId() {
            return id;
        }
    
        public void setId(String id) {
            this.id = id;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }



    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return this.username;
    }



    public void setPassword(String password) {
        this.password = password;
    }


    public void setUsername(String fullname) {
        this.username = fullname;
    }

    public boolean isEnabled() {
        return enabled;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}