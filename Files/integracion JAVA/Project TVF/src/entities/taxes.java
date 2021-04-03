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
import javax.persistence.ManyToMany;
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
@Table(name = "T")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "taxes.findAll", query = "SELECT t FROM taxes t"),
    @NamedQuery(name = "taxes.findByName", query = "SELECT t FROM taxes t WHERE t.name = :name"),
    @NamedQuery(name = "taxes.findByPercent", query = "SELECT t FROM taxes t WHERE t.percent = :percent")})
public class taxes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "PERCENT")
    private short percent;
    @ManyToMany(mappedBy = "taxesCollection", fetch = FetchType.EAGER)
    private Collection<purchase> purchaseCollection;

    public taxes() {
    }

    public taxes(String name) {
        this.name = name;
    }

    public taxes(String name, short percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getPercent() {
        return percent;
    }

    public void setPercent(short percent) {
        this.percent = percent;
    }

    @XmlTransient
    public Collection<purchase> getPurchaseCollection() {
        return purchaseCollection;
    }

    public void setPurchaseCollection(Collection<purchase> purchaseCollection) {
        this.purchaseCollection = purchaseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof taxes)) {
            return false;
        }
        taxes other = (taxes) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.taxes[ name=" + name + " ]";
    }
    
}
