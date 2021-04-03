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
@Table(name = "D")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "discount.findAll", query = "SELECT d FROM discount d"),
    @NamedQuery(name = "discount.findById", query = "SELECT d FROM discount d WHERE d.id = :id"),
    @NamedQuery(name = "discount.findByName", query = "SELECT d FROM discount d WHERE d.name = :name"),
    @NamedQuery(name = "discount.findByPercent", query = "SELECT d FROM discount d WHERE d.percent = :percent"),
    @NamedQuery(name = "discount.findByCondition", query = "SELECT d FROM discount d WHERE d.condition = :condition")})
public class discount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "PERCENT")
    private short percent;
    @Basic(optional = false)
    @Column(name = "CONDITION")
    private int condition;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dId", fetch = FetchType.EAGER)
    private Collection<purchase> purchaseCollection;

    public discount() {
    }

    public discount(String id) {
        this.id = id;
    }

    public discount(String id, String name, short percent, int condition) {
        this.id = id;
        this.name = name;
        this.percent = percent;
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof discount)) {
            return false;
        }
        discount other = (discount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.discount[ id=" + id + " ]";
    }
    
}
