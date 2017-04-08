package ru.sukhoa.domain;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@IdClass(StorageGroupInfo.class)
@Table(name = "dwf_storagegroup_r")
public class StorageGroupInfo extends BaseInfo implements Serializable {

    private StorageGroup storageGroup;

    private double respTimeReadRateBucket6;

    private double respTimeReadRateBucket7;

    private double respTimeWriteRateBucket6;

    private double respTimeWriteRateBucket7;

    @Id
    @OneToOne
    @JoinColumn(name = "storagegroupkey", referencedColumnName = "storagegroupkey")
    public StorageGroup getStorageGroup() {
        return storageGroup;
    }

    public void setStorageGroup(StorageGroup storageGroup) {
        this.storageGroup = storageGroup;
    }

    @Column(name = "spmreadrtcount6")
    public double getRespTimeReadRateBucket6() {
        return respTimeReadRateBucket6;
    }

    public void setRespTimeReadRateBucket6(double respTimeReadRateBucket6) {
        this.respTimeReadRateBucket6 = respTimeReadRateBucket6;
    }

    @Column(name = "spmreadrtcount7")
    public double getRespTimeReadRateBucket7() {
        return respTimeReadRateBucket7;
    }

    public void setRespTimeReadRateBucket7(double respTimeReadRateBucket7) {
        this.respTimeReadRateBucket7 = respTimeReadRateBucket7;
    }

    @Column(name = "spmwritertcount6")
    public double getRespTimeWriteRateBucket6() {
        return respTimeWriteRateBucket6;
    }

    public void setRespTimeWriteRateBucket6(double respTimeWriteRateBucket6) {
        this.respTimeWriteRateBucket6 = respTimeWriteRateBucket6;
    }

    @Column(name = "spmwritertcount7")
    public double getRespTimeWriteRateBucket7() {
        return respTimeWriteRateBucket7;
    }

    public void setRespTimeWriteRateBucket7(double respTimeWriteRateBucket7) {
        this.respTimeWriteRateBucket7 = respTimeWriteRateBucket7;
    }

    @Transient
    public double getSummaryBucketRate() {
        return respTimeReadRateBucket6 + respTimeReadRateBucket7 + respTimeWriteRateBucket6 + respTimeWriteRateBucket7;
    }

}