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
@Table(name = "PTVF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ptvf.findAll", query = "SELECT p FROM ptvf p"),
    @NamedQuery(name = "ptvf.findByName", query = "SELECT p FROM ptvf p WHERE p.name = :name")})
public class ptvf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ptvfName", fetch = FetchType.EAGER)
    private Collection<account> accountCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ptvfName", fetch = FetchType.EAGER)
    private Collection<photo> photoCollection;

    public ptvf() {
    }

    public ptvf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<account> getAccountCollection() {
        return accountCollection;
    }

    public void setAccountCollection(Collection<account> accountCollection) {
        this.accountCollection = accountCollection;
    }

    @XmlTransient
    public Collection<photo> getPhotoCollection() {
        return photoCollection;
    }

    public void setPhotoCollection(Collection<photo> photoCollection) {
        this.photoCollection = photoCollection;
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
        if (!(object instanceof ptvf)) {
            return false;
        }
        ptvf other = (ptvf) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ptvf[ name=" + name + " ]";
    }
    
}
