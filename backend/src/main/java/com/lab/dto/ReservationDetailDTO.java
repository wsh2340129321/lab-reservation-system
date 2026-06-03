package com.lab.dto;

import com.lab.entity.Reservation;

import java.time.LocalDate;

public class ReservationDetailDTO {
    private Long id;
    private Long userId;
    private String studentId;
    private String userName;
    private Long laboratoryId;
    private String laboratoryName;
    private Long reservationTimeId;
    private String startTime;
    private String endTime;
    private LocalDate reservationDate;
    private String status;
    private String rejectReason;
    private String createdAt;

    public ReservationDetailDTO() {}

    public ReservationDetailDTO(Reservation reservation, String studentId, String userName, 
                               String laboratoryName, String startTime, String endTime) {
        this.id = reservation.getId();
        this.userId = reservation.getUserId();
        this.studentId = studentId;
        this.userName = userName;
        this.laboratoryId = reservation.getLaboratoryId();
        this.laboratoryName = laboratoryName;
        this.reservationTimeId = reservation.getReservationTimeId();
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationDate = reservation.getReservationDate();
        this.status = reservation.getStatus();
        this.rejectReason = reservation.getRejectReason();
        this.createdAt = reservation.getCreatedAt() != null ? reservation.getCreatedAt().toString() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Long getLaboratoryId() { return laboratoryId; }
    public void setLaboratoryId(Long laboratoryId) { this.laboratoryId = laboratoryId; }
    public String getLaboratoryName() { return laboratoryName; }
    public void setLaboratoryName(String laboratoryName) { this.laboratoryName = laboratoryName; }
    public Long getReservationTimeId() { return reservationTimeId; }
    public void setReservationTimeId(Long reservationTimeId) { this.reservationTimeId = reservationTimeId; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}