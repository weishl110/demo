package com.wei.demo.bean;

/**
 * Created by ${wei} on 2017/6/9.
 */

public class ChannelTab {
    private String newsChannelName;
    private String newsChannelId;
    private String newsChannelType;
    private boolean newsChannelSelect;
    private int newsChannelIndex;
    private Boolean newsChannelFixed;

    public String getNewsChannelName() {
        return newsChannelName;
    }

    public void setNewsChannelName(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }

    public String getNewsChannelId() {
        return newsChannelId;
    }

    public void setNewsChannelId(String newsChannelId) {
        this.newsChannelId = newsChannelId;
    }

    public String getNewsChannelType() {
        return newsChannelType;
    }

    public void setNewsChannelType(String newsChannelType) {
        this.newsChannelType = newsChannelType;
    }

    public boolean isNewsChannelSelect() {
        return newsChannelSelect;
    }

    public void setNewsChannelSelect(boolean newsChannelSelect) {
        this.newsChannelSelect = newsChannelSelect;
    }

    public int getNewsChannelIndex() {
        return newsChannelIndex;
    }

    public void setNewsChannelIndex(int newsChannelIndex) {
        this.newsChannelIndex = newsChannelIndex;
    }

    public Boolean getNewsChannelFixed() {
        return newsChannelFixed;
    }

    public void setNewsChannelFixed(Boolean newsChannelFixed) {
        this.newsChannelFixed = newsChannelFixed;
    }

    @Override
    public String toString() {
        return "{" +
                "newsChannelName='" + newsChannelName + '\'' +
                ", newsChannelId='" + newsChannelId + '\'' +
                ", newsChannelType='" + newsChannelType + '\'' +
                ", newsChannelSelect=" + newsChannelSelect +
                ", newsChannelIndex=" + newsChannelIndex +
                ", newsChannelFixed=" + newsChannelFixed +
                '}';
    }
}
