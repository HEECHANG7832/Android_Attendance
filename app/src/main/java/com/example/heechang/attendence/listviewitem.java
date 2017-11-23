package com.example.heechang.attendence;

import java.io.Serializable;

/**
 * Created by heechang on 2017-11-08.
 */

//수업을 출력하는 listview의 한 객체
public class listviewitem implements Serializable {

    public String Lecnum;
    public String Lecname;
    public String Lecday;
    public String professor;
    public String lecloc;
    public String weekcount;
    public String ongoing;


//    public void setName(String name) {
//        Name = name;
//    }
//
//    public void setAddress(String address) {
//        Address = address;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public String getAddress() {
//        return Address;
//    }
}
