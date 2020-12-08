package com.example.sm_capstone;

public class Contact {
    private String name;
    private String phoneNum;
    private String time;
    private String contactId;
    private String accept;

    public String getAccept() {   return accept;   }
    public void setAccept(String accept) {   this.accept = accept;  }

    public String getName() {   return name;   }
    public void setName(String name) {   this.name = name;    }

    public String getPhoneNum() {    return phoneNum;    }
    public void setPhoneNum(String phoneNum) {    this.phoneNum = phoneNum;    }

    public String getTime() {    return time;    }
    public void setTime(String time) {    this.time = time;    }

    public String getContactId() {    return contactId;    }
    public void setContactId(String contactId) {    this.contactId = contactId;   }


    public Contact(String name, String phoneNum, String accept){
        this.name = name;
        this.phoneNum = phoneNum;
        this.accept = accept;
    }
}
