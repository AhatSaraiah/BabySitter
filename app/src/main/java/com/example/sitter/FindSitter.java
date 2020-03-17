package com.example.sitter;

public class FindSitter {
    public String profileimage, fullname, aboutMe, userID;



    public FindSitter(String profileimage, String fullname, String aboutMe, String userID) {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.aboutMe = aboutMe;
        this.userID = userID;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

}
