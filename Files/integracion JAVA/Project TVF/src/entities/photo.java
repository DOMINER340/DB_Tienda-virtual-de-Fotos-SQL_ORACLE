/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Lob;
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
@Table(name = "PH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "photo.findAll", query = "SELECT p FROM photo p"),
    @NamedQuery(name = "photo.findByName", query = "SELECT p FROM photo p WHERE p.name = :name"),
    @NamedQuery(name = "photo.findByDescription", query = "SELECT p FROM photo p WHERE p.description = :description"),
    @NamedQuery(name = "photo.findBySizeph", query = "SELECT p FROM photo p WHERE p.sizeph = :sizeph"),
    @NamedQuery(name = "photo.findByFormat", query = "SELECT p FROM photo p WHERE p.format = :format"),
    @NamedQuery(name = "photo.findByResolution", query = "SELECT p FROM photo p WHERE p.resolution = :resolution"),
    @NamedQuery(name = "photo.findByDateCreate", query = "SELECT p FROM photo p WHERE p.dateCreate = :dateCreate"),
    @NamedQuery(name = "photo.findByUploadDate", query = "SELECT p FROM photo p WHERE p.uploadDate = :uploadDate"),
    @NamedQuery(name = "photo.findByLocation", query = "SELECT p FROM photo p WHERE p.location = :location"),
    @NamedQuery(name = "photo.findByPrice", query = "SELECT p FROM photo p WHERE p.price = :price"),
    @NamedQuery(name = "photo.findByAmountViews", query = "SELECT p FROM photo p WHERE p.amountViews = :amountViews"),
    @NamedQuery(name = "photo.findByState", query = "SELECT p FROM photo p WHERE p.state = :state")})
public class photo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "PIMAGE")
    private byte[] pimage;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "SIZEPH")
    private BigDecimal sizeph;
    @Basic(optional = false)
    @Column(name = "FORMAT")
    private String format;
    @Basic(optional = false)
    @Column(name = "RESOLUTION")
    private String resolution;
    @Basic(optional = false)
    @Column(name = "DATE_CREATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;
    @Basic(optional = false)
    @Column(name = "UPLOAD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @Column(name = "LOCATION")
    private String location;
    @Basic(optional = false)
    @Column(name = "PRICE")
    private int price;
    @Basic(optional = false)
    @Column(name = "AMOUNT_VIEWS")
    private int amountViews;
    @Basic(optional = false)
    @Column(name = "STATE")
    private String state;
    @JoinTable(name = "SHOPCARTPH", joinColumns = {
        @JoinColumn(name = "PH_NAME", referencedColumnName = "NAME")}, inverseJoinColumns = {
        @JoinColumn(name = "SC_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<shoppingCart> shoppingCartCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photo", fetch = FetchType.EAGER)
    private Collection<phxva> phxvaCollection;
    @JoinColumn(name = "A_USERNAME", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private account aUsername;
    @JoinColumn(name = "C_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private country cId;
    @JoinColumn(name = "CH_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private character chId;
    @JoinColumn(name = "E_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private event eId;
    @JoinColumn(name = "PTVF_NAME", referencedColumnName = "NAME")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ptvf ptvfName;
    @JoinColumn(name = "S_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private season sId;
    @JoinColumn(name = "TOPIC_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private topic topicId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photo", fetch = FetchType.EAGER)
    private Collection<link> linkCollection;

    public photo() {
    }

    public photo(String name) {
        this.name = name;
    }

    public photo(String name, String description, BigDecimal sizeph, String format, String resolution, Date dateCreate, Date uploadDate, int price, int amountViews, String state) {
        this.name = name;
        this.description = description;
        this.sizeph = sizeph;
        this.format = format;
        this.resolution = resolution;
        this.dateCreate = dateCreate;
        this.uploadDate = uploadDate;
        this.price = price;
        this.amountViews = amountViews;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Serializable getPimage() {
        return pimage;
    }

    public void setPimage(byte[] pimage) {
        this.pimage = pimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSizeph() {
        return sizeph;
    }

    public void setSizeph(BigDecimal sizeph) {
        this.sizeph = sizeph;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmountViews() {
        return amountViews;
    }

    public void setAmountViews(int amountViews) {
        this.amountViews = amountViews;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @XmlTransient
    public Collection<shoppingCart> getShoppingCartCollection() {
        return shoppingCartCollection;
    }

    public void setShoppingCartCollection(Collection<shoppingCart> shoppingCartCollection) {
        this.shoppingCartCollection = shoppingCartCollection;
    }

    @XmlTransient
    public Collection<phxva> getPhxvaCollection() {
        return phxvaCollection;
    }

    public void setPhxvaCollection(Collection<phxva> phxvaCollection) {
        this.phxvaCollection = phxvaCollection;
    }

    public account getAUsername() {
        return aUsername;
    }

    public void setAUsername(account aUsername) {
        this.aUsername = aUsername;
    }

    public country getCId() {
        return cId;
    }

    public void setCId(country cId) {
        this.cId = cId;
    }

    public character getChId() {
        return chId;
    }

    public void setChId(character chId) {
        this.chId = chId;
    }

    public event getEId() {
        return eId;
    }

    public void setEId(event eId) {
        this.eId = eId;
    }

    public ptvf getPtvfName() {
        return ptvfName;
    }

    public void setPtvfName(ptvf ptvfName) {
        this.ptvfName = ptvfName;
    }

    public season getSId() {
        return sId;
    }

    public void setSId(season sId) {
        this.sId = sId;
    }

    public topic getTopicId() {
        return topicId;
    }

    public void setTopicId(topic topicId) {
        this.topicId = topicId;
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
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof photo)) {
            return false;
        }
        photo other = (photo) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.photo[ name=" + name + " ]";
    }
    
}
