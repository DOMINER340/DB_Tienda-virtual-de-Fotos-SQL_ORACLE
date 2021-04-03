/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gustavo
 */
@Entity
@Table(name = "FS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "formatSelect.findAll", query = "SELECT f FROM formatSelect f"),
    @NamedQuery(name = "formatSelect.findByIdfs", query = "SELECT f FROM formatSelect f WHERE f.idfs = :idfs"),
    @NamedQuery(name = "formatSelect.findByFormatname", query = "SELECT f FROM formatSelect f WHERE f.formatname = :formatname")})
public class formatSelect implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDFS")
    private String idfs;
    @Basic(optional = false)
    @Column(name = "FORMATNAME")
    private String formatname;
    @JoinTable(name = "PM", joinColumns = {
        @JoinColumn(name = "FS_IDFS", referencedColumnName = "IDFS")}, inverseJoinColumns = {
        @JoinColumn(name = "P_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<purchase> purchaseCollection;
    @JoinColumn(name = "CPF_IDPAYFORMAT", referencedColumnName = "IDPAYFORMAT")
    @ManyToOne(fetch = FetchType.EAGER)
    private creditPaymentFormat cpfIdpayformat;
    @JoinColumn(name = "PAYPAL_IDPP", referencedColumnName = "IDPP")
    @ManyToOne(fetch = FetchType.EAGER)
    private paypal paypalIdpp;

    public formatSelect() {
    }

    public formatSelect(String idfs) {
        this.idfs = idfs;
    }

    public formatSelect(String idfs, String formatname) {
        this.idfs = idfs;
        this.formatname = formatname;
    }

    public String getIdfs() {
        return idfs;
    }

    public void setIdfs(String idfs) {
        this.idfs = idfs;
    }

    public String getFormatname() {
        return formatname;
    }

    public void setFormatname(String formatname) {
        this.formatname = formatname;
    }

    @XmlTransient
    public Collection<purchase> getPurchaseCollection() {
        return purchaseCollection;
    }

    public void setPurchaseCollection(Collection<purchase> purchaseCollection) {
        this.purchaseCollection = purchaseCollection;
    }

    public creditPaymentFormat getCpfIdpayformat() {
        return cpfIdpayformat;
    }

    public void setCpfIdpayformat(creditPaymentFormat cpfIdpayformat) {
        this.cpfIdpayformat = cpfIdpayformat;
    }

    public paypal getPaypalIdpp() {
        return paypalIdpp;
    }

    public void setPaypalIdpp(paypal paypalIdpp) {
        this.paypalIdpp = paypalIdpp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfs != null ? idfs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof formatSelect)) {
            return false;
        }
        formatSelect other = (formatSelect) object;
        if ((this.idfs == null && other.idfs != null) || (this.idfs != null && !this.idfs.equals(other.idfs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.formatSelect[ idfs=" + idfs + " ]";
    }
    
}
