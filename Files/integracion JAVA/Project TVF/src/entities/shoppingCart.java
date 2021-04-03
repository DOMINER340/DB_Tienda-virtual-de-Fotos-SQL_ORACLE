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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "SC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "shoppingCart.findAll", query = "SELECT s FROM shoppingCart s"),
    @NamedQuery(name = "shoppingCart.findById", query = "SELECT s FROM shoppingCart s WHERE s.id = :id")})
public class shoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @ManyToMany(mappedBy = "shoppingCartCollection", fetch = FetchType.EAGER)
    private Collection<photo> photoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scId", fetch = FetchType.EAGER)
    private Collection<purchase> purchaseCollection;
    @JoinColumn(name = "A_USERNAME", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private account aUsername;

    public shoppingCart() {
    }

    public shoppingCart(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<photo> getPhotoCollection() {
        return photoCollection;
    }

    public void setPhotoCollection(Collection<photo> photoCollection) {
        this.photoCollection = photoCollection;
    }

    @XmlTransient
    public Collection<purchase> getPurchaseCollection() {
        return purchaseCollection;
    }

    public void setPurchaseCollection(Collection<purchase> purchaseCollection) {
        this.purchaseCollection = purchaseCollection;
    }

    public account getAUsername() {
        return aUsername;
    }

    public void setAUsername(account aUsername) {
        this.aUsername = aUsername;
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
        if (!(object instanceof shoppingCart)) {
            return false;
        }
        shoppingCart other = (shoppingCart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.shoppingCart[ id=" + id + " ]";
    }
    
}
