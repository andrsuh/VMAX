package ru.sukhoa.domain;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseInfo {

    private DateTimeStatement dateStamp;

    private double mbRate;

    @Id
    @OneToOne
    @JoinColumn(name = "timekey", referencedColumnName = "timekey")
    public DateTimeStatement getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(DateTimeStatement dateStamp) {
        this.dateStamp = dateStamp;
    }


    @Column(name = "spmmbrate")
    public double getMbRate() {
        return mbRate;
    }

    public void setMbRate(double mbRate) {
        this.mbRate = mbRate;
    }

    public boolean satisfiedDate(@Nullable Date fromDate, @Nullable Date toDate) {
        if (fromDate != null && dateStamp.getDatestamp().compareTo(fromDate) <= 0) {
            return false;
        }

        return !(toDate != null && dateStamp.getDatestamp().compareTo(toDate) > 0);
    }

    @Transient
    public abstract double getSummaryBucketRate();

}
