/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gustavo
 */
@Entity
@Table(name = "P")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "purchase.findAll", query = "SELECT p FROM purchase p"),
    @NamedQuery(name = "purchase.findById", query = "SELECT p FROM purchase p WHERE p.id = :id"),
    @NamedQuery(name = "purchase.findByPurchasedate", query = "SELECT p FROM purchase p WHERE p.purchasedate = :purchasedate")})
public class purchase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "PURCHASEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchasedate;
    @JoinTable(name = "PXT", joinColumns = {
        @JoinColumn(name = "P_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "T_NAME", referencedColumnName = "NAME")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<taxes> taxesCollection;
    @ManyToMany(mappedBy = "purchaseCollection", fetch = FetchType.EAGER)
    private Collection<formatSelect> formatSelectCollection;
    @JoinColumn(name = "A_USERNAME", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private account aUsername;
    @JoinColumn(name = "D_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private discount dId;
    @JoinColumn(name = "SC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private shoppingCart scId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchase", fetch = FetchType.EAGER)
    private Collection<link> linkCollection;

    public purchase() {
    }

    public purchase(String id) {
        this.id = id;
    }

    public purchase(String id, Date purchasedate) {
        this.id = id;
        this.purchasedate = purchasedate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(Date purchasedate) {
        this.purchasedate = purchasedate;
    }

    @XmlTransient
    public Collection<taxes> getTaxesCollection() {
        return taxesCollection;
    }

    public void setTaxesCollection(Collection<taxes> taxesCollection) {
        this.taxesCollection = taxesCollection;
    }

    @XmlTransient
    public Collection<formatSelect> getFormatSelectCollection() {
        return formatSelectCollection;
    }

    public void setFormatSelectCollection(Collection<formatSelect> formatSelectCollection) {
        this.formatSelectCollection = formatSelectCollection;
    }

    public account getAUsername() {
        return aUsername;
    }

    public void setAUsername(account aUsername) {
        this.aUsername = aUsername;
    }

    public discount getDId() {
        return dId;
    }

    public void setDId(discount dId) {
        this.dId = dId;
    }

    public shoppingCart getScId() {
        return scId;
    }

    public void setScId(shoppingCart scId) {
        this.scId = scId;
    }

    @XmlTransient
    public Collection<link> getLinkCollection() {
        return linkCollection;
    }

    public void setLinkCollection(Collection<link> linkCollection) {
        this.linkCollection = linkCollection;
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
        if (!(object instanceof purchase)) {
            return false;
        }
        purchase other = (purchase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.purchase[ id=" + id + " ]";
    }
    
}
