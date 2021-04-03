/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gustavo
 */
@Entity
@Table(name = "PHXV")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "phxva.findAll", query = "SELECT p FROM phxva p"),
    @NamedQuery(name = "phxva.findBySvId", query = "SELECT p FROM phxva p WHERE p.phxvaPK.svId = :svId"),
    @NamedQuery(name = "phxva.findByPhName", query = "SELECT p FROM phxva p WHERE p.phxvaPK.phName = :phName"),
    @NamedQuery(name = "phxva.findByAmountviews", query = "SELECT p FROM phxva p WHERE p.amountviews = :amountviews")})
public class phxva implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected phxvaPK phxvaPK;
    @Basic(optional = false)
    @Column(name = "AMOUNTVIEWS")
    private int amountviews;
    @JoinColumn(name = "PH_NAME", referencedColumnName = "NAME", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private photo photo;
    @JoinColumn(name = "SV_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private statsVisitor statsVisitor;

    public phxva() {
    }

    public phxva(phxvaPK phxvaPK) {
        this.phxvaPK = phxvaPK;
    }

    public phxva(phxvaPK phxvaPK, int amountviews) {
        this.phxvaPK = phxvaPK;
        this.amountviews = amountviews;
    }

    public phxva(String svId, String phName) {
        this.phxvaPK = new phxvaPK(svId, phName);
    }

    public phxvaPK getPhxvaPK() {
        return phxvaPK;
    }

    public void setPhxvaPK(phxvaPK phxvaPK) {
        this.phxvaPK = phxvaPK;
    }

    public int getAmountviews() {
        return amountviews;
    }

    public void setAmountviews(int amountviews) {
        this.amountviews = amountviews;
    }

    public photo getPhoto() {
        return photo;
    }

    public void setPhoto(photo photo) {
        this.photo = photo;
    }

    public statsVisitor getStatsVisitor() {
        return statsVisitor;
    }

    public void setStatsVisitor(statsVisitor statsVisitor) {
        this.statsVisitor = statsVisitor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phxvaPK != null ? phxvaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof phxva)) {
            return false;
        }
        phxva other = (phxva) object;
        if ((this.phxvaPK == null && other.phxvaPK != null) || (this.phxvaPK != null && !this.phxvaPK.equals(other.phxvaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.phxva[ phxvaPK=" + phxvaPK + " ]";
    }
    
}
