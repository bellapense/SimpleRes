package com.example.simpleres;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WaitlistEntry {
    private int id;
    private String name;
    private String telephone;
    private int numberOfPeople;
    private String formattedDateTime;
    private int reservationFlag = 0;
    private LocalDateTime reservationTime;
// general purpose constructor
    WaitlistEntry(int Id, String Name, String Telephone,int NumberOfPeople, String FormattedDateTime, LocalDateTime ReservationTime) {
        this.id = Id;
        this.name = Name;
        this.telephone = Telephone;
        this.numberOfPeople = NumberOfPeople;

        this.reservationTime = ReservationTime;

        this.formattedDateTime = FormatDate(reservationTime);
    }
    // constructor for walk-in
    WaitlistEntry(int Id, String Name, String Telephone,int NumberOfPeople, String FormattedDateTime, long QuotedTime) {
        this.id = Id;
        this.name = Name;
        this.telephone = Telephone;
        this.numberOfPeople = NumberOfPeople;

        this.reservationTime = LocalDateTime.now().plusMinutes(QuotedTime); //this adds quoted time to current time

        this.formattedDateTime = FormatDate(reservationTime);
    }
    // constructor for reservation
    WaitlistEntry(int Id, String Name, String Telephone,int NumberOfPeople, String FormattedDateTime, LocalDateTime ReservationTime, int ReservationFlag) {
        this.id = Id;
        this.name = Name;
        this.telephone = Telephone;
        this.numberOfPeople = NumberOfPeople;

        this.reservationTime = ReservationTime;
        this.reservationFlag = ReservationFlag;
        this.formattedDateTime = FormatDate(reservationTime);
    }
    public WaitlistEntry() {

    }

    public int getId() { return this.id; }
    public void setId(int Id) { this.id = Id; }

    public String getName() {
        return this.name;
    }
    public void setName(String Name) {this.name=Name;}


    public String getTelephone() { return this.telephone; }
    public void setTelephone(String Telephone) {this.telephone=Telephone;}

    public int getNumberOfPeople() { return this.numberOfPeople; }
    public void setNumberOfPeople(int NumberOfPeople) {this.numberOfPeople = NumberOfPeople;}

    public LocalDateTime getReservationTime() {
        return this.reservationTime;
    }
    public void setReservedTime(LocalDateTime ReservationTime){this.reservationTime=ReservationTime;}

    public String getFormattedDateTime(){return this.formattedDateTime;}
    public void setFormattedDateTime(String FormattedDateTime) {this.formattedDateTime=FormattedDateTime;}

    public int getReservationFlag(){return this.reservationFlag;}
    public void setReservationFlag(int ReservationFlag){this.reservationFlag=ReservationFlag;}

//date format for easy sorting year-month-day hours:minutes:seconds
    public static String FormatDate(LocalDateTime myDateTimeObj) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ssss");//maybe add .ssss to end of pattern

        String FormattedDate = myDateTimeObj.format(myFormatObj);
        System.out.println("Date formatted from " + myDateTimeObj.toString() + " to " + FormattedDate);
        return FormattedDate;
    }

}
