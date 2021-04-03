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
@Table(name = "A")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "account.findAll", query = "SELECT a FROM account a"),
    @NamedQuery(name = "account.findByUsername", query = "SELECT a FROM account a WHERE a.username = :username"),
    @NamedQuery(name = "account.findByPassword", query = "SELECT a FROM account a WHERE a.password = :password"),
    @NamedQuery(name = "account.findByName", query = "SELECT a FROM account a WHERE a.name = :name"),
    @NamedQuery(name = "account.findByLastname", query = "SELECT a FROM account a WHERE a.lastname = :lastname"),
    @NamedQuery(name = "account.findByCreationdate", query = "SELECT a FROM account a WHERE a.creationdate = :creationdate"),
    @NamedQuery(name = "account.findByEmail", query = "SELECT a FROM account a WHERE a.email = :email"),
    @NamedQuery(name = "account.findByType", query = "SELECT a FROM account a WHERE a.type = :type")})
public class account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastname;
    @Basic(optional = false)
    @Column(name = "CREATIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationdate;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @JoinColumn(name = "PTVF_NAME", referencedColumnName = "NAME")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ptvf ptvfName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aUsername", fetch = FetchType.EAGER)
    private Collection<purchase> purchaseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aUsername", fetch = FetchType.EAGER)
    private Collection<shoppingCart> shoppingCartCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aUsername", fetch = FetchType.EAGER)
    private Collection<photo> photoCollection;

    public account() {
    }

    public account(String username) {
        this.username = username;
    }

    public account(String username, String password, String name, String lastname, Date creationdate, String email, String type) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.creationdate = creationdate;
        this.email = email;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ptvf getPtvfName() {
        return ptvfName;
    }

    public void setPtvfName(ptvf ptvfName) {
        this.ptvfName = ptvfName;
    }

    @XmlTransient
    public Collection<purchase> getPurchaseCollection() {
        return purchaseCollection;
    }

    public void setPurchaseCollection(Collection<purchase> purchaseCollection) {
        this.purchaseCollection = purchaseCollection;
    }

    @XmlTransient
    public Collection<shoppingCart> getShoppingCartCollection() {
        return shoppingCartCollection;
    }

    public void setShoppingCartCollection(Collection<shoppingCart> shoppingCartCollection) {
        this.shoppingCartCollection = shoppingCartCollection;
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
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof account)) {
            return false;
        }
        account other = (account) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.account[ username=" + username + " ]";
    }
    
}
