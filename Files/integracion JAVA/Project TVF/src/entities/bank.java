/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gustavo
 */
@Entity
@Table(name = "BANK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "bank.findAll", query = "SELECT b FROM bank b"),
    @NamedQuery(name = "bank.findById", query = "SELECT b FROM bank b WHERE b.id = :id"),
    @NamedQuery(name = "bank.findByFinancialename", query = "SELECT b FROM bank b WHERE b.financialename = :financialename"),
    @NamedQuery(name = "bank.findByCommission", query = "SELECT b FROM bank b WHERE b.commission = :commission")})
public class bank implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "FINANCIALENAME")
    private String financialename;
    @Basic(optional = false)
    @Column(name = "COMMISSION")
    private short commission;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId", fetch = FetchType.EAGER)
    private Collection<paypal> paypalCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId", fetch = FetchType.EAGER)
    private Collection<creditPaymentFormat> creditPaymentFormatCollection;

    public bank() {
    }

    public bank(String id) {
        this.id = id;
    }

    public bank(String id, String financialename, short commission) {
        this.id = id;
        this.financialename = financialename;
        this.commission = commission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFinancialename() {
        return financialename;
    }

    public void setFinancialename(String financialename) {
        this.financialename = financialename;
    }

    public short getCommission() {
        return commission;
    }

    public void setCommission(short commission) {
        this.commission = commission;
    }

    @XmlTransient
    public Collection<paypal> getPaypalCollection() {
        return paypalCollection;
    }

    public void setPaypalCollection(Collection<paypal> paypalCollection) {
        this.paypalCollection = paypalCollection;
    }

    @XmlTransient
    public Collection<creditPaymentFormat> getCreditPaymentFormatCollection() {
        return creditPaymentFormatCollection;
    }

    public void setCreditPaymentFormatCollection(Collection<creditPaymentFormat> creditPaymentFormatCollection) {
        this.creditPaymentFormatCollection = creditPaymentFormatCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof bank)) {
            return false;
        }
        bank other = (bank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.bank[ id=" + id + " ]";
    }
    
}
