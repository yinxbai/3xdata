package com.txdata.modules.daily.domain;


public class MyUserDO {
    private String id; // 用户id
    private String name; // 用户姓名
    private String position; // 用户职位

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
