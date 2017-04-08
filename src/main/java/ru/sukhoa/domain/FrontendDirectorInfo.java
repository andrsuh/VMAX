package ru.sukhoa.domain;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(FrontendDirectorInfo.class)
@Table(name = "dwf_fedirector_r")
public class FrontendDirectorInfo implements Serializable {

    private DateTimeStatement dateStamp;

    private FrontendDirector director;

    private double ioRate;

    private double mbRate;

    private double queueBucket7Rate;

    private double queueBucket8Rate;

    private double queueBucket9Rate;

    @Id
    @OneToOne
    @JoinColumn(name = "timekey", referencedColumnName = "timekey")
    public DateTimeStatement getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(DateTimeStatement dateStamp) {
        this.dateStamp = dateStamp;
    }

    @Id
    @OneToOne
    @JoinColumn(name = "fedirectorkey", referencedColumnName = "fedirectorkey")
    public FrontendDirector getDirector() {
        return director;
    }

    public void setDirector(FrontendDirector director) {
        this.director = director;
    }


    @Column(name = "spmiorate")
    public double getIoRate() {
        return ioRate;
    }

    public void setIoRate(double ioRate) {
        this.ioRate = ioRate;
    }

    @Column(name = "spmmbrate")
    public double getMbRate() {
        return mbRate;
    }

    public void setMbRate(double mbRate) {
        this.mbRate = mbRate;
    }

    @Column(name = "spmqueuedepcount7")
    public double getQueueBucket7Rate() {
        return queueBucket7Rate;
    }

    public void setQueueBucket7Rate(double queueBucket7Rate) {
        this.queueBucket7Rate = queueBucket7Rate;
    }

    @Column(name = "spmqueuedepcount8")
    public double getQueueBucket8Rate() {
        return queueBucket8Rate;
    }

    public void setQueueBucket8Rate(double queueBucket8Rate) {
        this.queueBucket8Rate = queueBucket8Rate;
    }

    @Column(name = "spmqueuedepcount9")
    public double getQueueBucket9Rate() {
        return queueBucket9Rate;
    }

    public void setQueueBucket9Rate(double queueBucket9Rate) {
        this.queueBucket9Rate = queueBucket9Rate;
    }

    public boolean satisfiedDate(@Nullable Date fromDate, @Nullable Date toDate) {
        if (fromDate != null && dateStamp.getDatestamp().compareTo(fromDate) <= 0) {
            return false;
        }

        return !(toDate != null && dateStamp.getDatestamp().compareTo(toDate) > 0);
    }

    @Transient
    public double getSummaryBucketRate() {
        return queueBucket7Rate + queueBucket8Rate + queueBucket9Rate;
    }

}
