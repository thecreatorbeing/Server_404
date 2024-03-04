package com.sarveshdev.techlearn.recyclerview_api.discussion.chatting;

public class ModelChatting {
    private String userName, comment, userIconUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public ModelChatting() {/*used by firebase!*/}

    public ModelChatting(String userName, String comment, String userIconUrl) {
        this.userName = userName;
        this.comment = comment;
        this.userIconUrl = userIconUrl;
    }
}
