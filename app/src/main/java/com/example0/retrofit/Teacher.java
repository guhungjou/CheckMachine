package com.example0.retrofit;

public class Teacher {

    private int id;
    private String username;
    private KindergartenDTO kindergarten;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public KindergartenDTO getKindergarten() {
        return kindergarten;
    }

    public void setKindergarten(KindergartenDTO kindergarten) {
        this.kindergarten = kindergarten;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class KindergartenDTO {

        private int id;

        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
