package com.example.pooja.knowyourgov;

import java.io.Serializable;

/**
 * Created by pooja on 21,October,2018
 */
public class KYGDetails implements Serializable{


// declarations
    private String add;
    private String phone;
    private String website;
    private String email;
    private String photoLink;
    private String google;
    private String facebook;
    private String twitter;
    private String youtube;
    private String city;
    private String state;
    private String zip;
    private String office_name;
    private String official_name;
    private String party;

    // getters and setters

    public void setOffState(String state) {
        this.state = state;
    }

    public String getOffZip() {
        return zip;
    }

    public String getOffwebsite() {
        return website;
    }

    public void setOffwebsite(String urlofweb) {
        this.website = urlofweb;
    }

    public String getEmail() {
        return email;
    }

    public String getOffCity() {
        return city;
    }

    public void setOffCity(String city) {
        this.city = city;
    }

    public String getOffState() {
        return state;
    }



    public void setEmail(String email_id) {
        this.email = email_id;
    }

    public String getPhotoUrl() {
        return photoLink;
    }

    public void setOffZip(String zip) {
        this.zip = zip;
    }

    public String getOffAddress() {
        return add;
    }

    public void setOffAddress(String address) {
        this.add = address;
    }

    public String getOffContact() {
        return phone;
    }

    public void setOffContact(String contact_no) {
        this.phone = contact_no;
    }



    public void setPhotoUrl(String urlofphoto) {
        this.photoLink = urlofphoto;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String googleplus_channelid) {
        this.google = googleplus_channelid;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook_channelid) {
        this.facebook = facebook_channelid;
    }
    public String getOfficial_name() {
        return official_name;
    }

    public void setOffname(String title) {
        this.official_name = title;
    }

    public String getOffParty() {
        return party;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twiter_channedlid) {
        this.twitter = twiter_channedlid;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube_channeelid) {
        this.youtube = youtube_channeelid;
    }




    public String getOfficename() {
        return office_name;
    }

    public void setOfficename(String office_name) {
        this.office_name = office_name;
    }



    public void setOffParty(String party) {
        this.party = party;
    }

    @Override
    public String toString() {
        return "Government{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", address='" + add + '\'' +
                ", contact_no='" + phone + '\'' +
                ", urlofweb='" + website + '\'' +
                ", email_id='" + email + '\'' +
                ", urlofphoto='" + photoLink + '\'' +
                ", googleplus_channelid='" + google + '\'' +
                ", facebook_channelid='" + facebook + '\'' +
                ", twiter_channedlid='" + twitter + '\'' +
                ", youtube_channeelid='" + youtube + '\'' +
                ", office_name='" + office_name + '\'' +
                ", title='" + official_name + '\'' +
                ", party='" + party + '\'' +
                '}';
    }


}
