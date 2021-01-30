package ru.itmo.wp.model.domain;

import java.io.Serializable;
import java.util.Date;

public class Talk implements Serializable {
    private long id;
    private long sourceUserId;
    private long targetUserId;
    private String text;
    private Date creationTime;

    public static class TalkWithNames {
        private long id;
        private String sourceUserName;
        private String targetUserName;
        private String text;
        private Date creationTime;

        public TalkWithNames(Talk talk) {
            id = talk.id;
            text = talk.text;
            creationTime = talk.creationTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getSourceUserName() {
            return sourceUserName;
        }

        public void setSourceUserName(String sourceUserName) {
            this.sourceUserName = sourceUserName;
        }

        public String getTargetUserName() {
            return targetUserName;
        }

        public void setTargetUserName(String targetUserName) {
            this.targetUserName = targetUserName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

}
