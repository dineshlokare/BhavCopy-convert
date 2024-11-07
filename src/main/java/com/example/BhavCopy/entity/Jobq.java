package com.example.BhavCopy.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobq")
public class Jobq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reqid", nullable = false, unique = true)
    private String reqid;

    @Column(name = "addedat", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "startedat")
    private LocalDateTime startedAt;

    @Column(name = "endedat")
    private LocalDateTime endedAt;

    @Column(name = "duration")
    private Long duration;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "params", columnDefinition = "jsonb")
    private JsonNode params;  // Changed type to JsonNode

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response", columnDefinition = "jsonb")
    private JsonNode response;  // Changed type to JsonNode

    @Column(name = "status", nullable = false)
    private String status;

    // Default constructor
    public Jobq() {
    }

    // Parameterized constructor
    public Jobq(String reqid, LocalDateTime addedAt, JsonNode params, String status) {
        this.reqid = reqid;
        this.addedAt = addedAt;
        this.params = params;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration2) {
        this.duration = duration2;
    }

    public JsonNode getParams() {
        return params;
    }

    public void setParams(JsonNode param) {
        this.params = param;
    }

    public JsonNode getResponse() {
        return response;
    }

    public void setResponse(JsonNode response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
