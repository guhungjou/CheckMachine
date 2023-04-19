package com.example0.retrofit;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Student {

    private int id;
    private String name;
    public String gender;
    private String birthday;

    private KindergartenDTO kindergarten;

    @SerializedName("class")
    private ClassDTO classX;

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

    //将性别输出格式改写
    public String getGender() {

        if (gender.equals("male")) {
            return "男";
        }
        if (gender.equals("female")) {
            return "女";
        }
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    //改写get方法，将生日转换成年龄--几年几个月
    public String getBirthday() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date birth = df.parse(birthday);
        Calendar now = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        b.setTime(birth);
        int year = now.get(Calendar.YEAR) - b.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) - b.get(Calendar.MONTH);
        month = year * 12 + month;
        int day = now.get(Calendar.DAY_OF_MONTH) - b.get(Calendar.DAY_OF_MONTH);
        if (day >= 15) {
            month += 1;
        } else if (day < -15) {
            month -= 1;
        }
        return  month/12 + "年" + month%12 + "月";
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public ClassDTO getClassX() {
        return classX;
    }

    public void setClassX(ClassDTO classX) {
        this.classX = classX;
    }

    public KindergartenDTO getKindergarten() {
        return kindergarten;
    }

    public void setKindergarten(KindergartenDTO kindergarten) {
        this.kindergarten = kindergarten;
    }
    public  class ClassDTO {
        private String name;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }

    public  class KindergartenDTO {
        private String name;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}