package com.company;

import java.io.Serializable;

public class Command implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Object[] args;

    public String getName() {
        return name;
    }

    public Object[] getArgs() {
        return args;
    }

    private String login, password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Command(String name, Object[] args) {
        this.name = name;
        this.args = args;
    }
}