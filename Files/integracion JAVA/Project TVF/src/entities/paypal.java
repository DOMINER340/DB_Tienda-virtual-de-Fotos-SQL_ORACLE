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
import javax.persistence.ManyToOne;
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
@Table(name = "PAYPAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "paypal.findAll", query = "SELECT p FROM paypal p"),
    @NamedQuery(name = "paypal.findByIdpp", query = "SELECT p FROM paypal p WHERE p.idpp = :idpp"),
    @NamedQuery(name = "paypal.findByConfirmationnumber", query = "SELECT p FROM paypal p WHERE p.confirmationnumber = :confirmationnumber")})
public class paypal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPP")
    private String idpp;
    @Basic(optional = false)
    @Column(name = "CONFIRMATIONNUMBER")
    private String confirmationnumber;
    @OneToMany(mappedBy = "paypalIdpp", fetch = FetchType.EAGER)
    private Collection<formatSelect> formatSelectCollection;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private bank bankId;

    public paypal() {
    }

    public paypal(String idpp) {
        this.idpp = idpp;
    }

    public paypal(String idpp, String confirmationnumber) {
        this.idpp = idpp;
        this.confirmationnumber = confirmationnumber;
    }

    public String getIdpp() {
        return idpp;
    }

    public void setIdpp(String idpp) {
        this.idpp = idpp;
    }

    public String getConfirmationnumber() {
        return confirmationnumber;
    }

    public void setConfirmationnumber(String confirmationnumber) {
        this.confirmationnumber = confirmationnumber;
    }

    @XmlTransient
    public Collection<formatSelect> getFormatSelectCollection() {
        return formatSelectCollection;
    }

    public void setFormatSelectCollection(Collection<formatSelect> formatSelectCollection) {
        this.formatSelectCollection = formatSelectCollection;
    }

    public bank getBankId() {
        return bankId;
    }

    public void setBankId(bank bankId) {
        this.bankId = bankId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpp != null ? idpp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof paypal)) {
            return false;
        }
        paypal other = (paypal) object;
        if ((this.idpp == null && other.idpp != null) || (this.idpp != null && !this.idpp.equals(other.idpp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.paypal[ idpp=" + idpp + " ]";
    }
    
}
