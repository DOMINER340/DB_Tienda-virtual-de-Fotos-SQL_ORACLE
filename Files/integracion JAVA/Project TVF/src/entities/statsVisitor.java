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
@Table(name = "SV")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "statsVisitor.findAll", query = "SELECT s FROM statsVisitor s"),
    @NamedQuery(name = "statsVisitor.findById", query = "SELECT s FROM statsVisitor s WHERE s.id = :id")})
public class statsVisitor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statsVisitor", fetch = FetchType.EAGER)
    private Collection<phxva> phxvaCollection;

    public statsVisitor() {
    }

    public statsVisitor(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<phxva> getPhxvaCollection() {
        return phxvaCollection;
    }

    public void setPhxvaCollection(Collection<phxva> phxvaCollection) {
        this.phxvaCollection = phxvaCollection;
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
        if (!(object instanceof statsVisitor)) {
            return false;
        }
        statsVisitor other = (statsVisitor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.statsVisitor[ id=" + id + " ]";
    }
    
}
