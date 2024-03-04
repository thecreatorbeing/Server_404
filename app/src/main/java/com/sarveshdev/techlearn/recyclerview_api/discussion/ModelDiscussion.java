package com.sarveshdev.techlearn.recyclerview_api.discussion;

public class ModelDiscussion {
    private String userIconUrl;
    private String query;
    private String userName;
    private String commentSectionId;


    public String getCommentSectionId() {
        return commentSectionId;
    }

    public void setCommentSectionId(String commentSectionId) {
        this.commentSectionId = commentSectionId;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ModelDiscussion() {
    }

    public ModelDiscussion(String userIconUrl, String query, String userName, String commentSectionId) {
        this.userIconUrl = userIconUrl;
        this.query = query;
        this.userName = userName;
        this.commentSectionId = commentSectionId;
    }
}
