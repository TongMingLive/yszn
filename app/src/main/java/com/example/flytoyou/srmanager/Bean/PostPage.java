package com.example.flytoyou.srmanager.Bean;

/**
 * Created by Tong on 2017/3/7.
 */
public class PostPage {
    private int pageId;
    private int postId;
    private String pageTitle;
    private String postName;
    private String userName;
    private int userId;
    private String pageTime;
    private String pageTxt;
    private String pageImg;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String pageName) {
        this.postName = pageName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPageTime() {
        return pageTime;
    }

    public void setPageTime(String pageTime) {
        this.pageTime = pageTime;
    }

    public String getPageTxt() {
        return pageTxt;
    }

    public void setPageTxt(String pageTxt) {
        this.pageTxt = pageTxt;
    }

    public String getPageImg() {
        return pageImg;
    }

    public void setPageImg(String pageImg) {
        this.pageImg = pageImg;
    }
}
