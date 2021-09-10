package com.mydictionary.dictionary.model;

/**
 * Holder to all needed translations of one word
 */
//todo: DELETE
public class BasicProperties {

    protected int userId;
    protected String srcLangCode;
    protected String destLangCode;

    public BasicProperties() {
    }

    public BasicProperties(int userId, String srcLangCode, String destLangCode) {
        this.userId = userId;
        this.srcLangCode = srcLangCode;
        this.destLangCode = destLangCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSrcLangCode() {
        return srcLangCode;
    }

    public void setSrcLangCode(String srcLangCode) {
        this.srcLangCode = srcLangCode;
    }

    public String getDestLangCode() {
        return destLangCode;
    }

    public void setDestLangCode(String destLangCode) {
        this.destLangCode = destLangCode;
    }
}
