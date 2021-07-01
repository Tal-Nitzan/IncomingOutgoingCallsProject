package com.example.incomingoutgoingcallsproject.entity;

public class PhoneRecord {
    private String phoneNumber;
    private Long callsLength;

    public PhoneRecord(String phoneNumber, Long callsLength) {
        this.phoneNumber = phoneNumber;
        this.callsLength = callsLength;
    }

    public PhoneRecord() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PhoneRecord setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Long getCallsLength() {
        return callsLength;
    }

    public PhoneRecord setCallsLength(Long callsLength) {
        this.callsLength = callsLength;
        return this;
    }
}
