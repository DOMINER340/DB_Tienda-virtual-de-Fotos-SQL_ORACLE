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
@Table(name = "CPF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "creditPaymentFormat.findAll", query = "SELECT c FROM creditPaymentFormat c"),
    @NamedQuery(name = "creditPaymentFormat.findByIdpayformat", query = "SELECT c FROM creditPaymentFormat c WHERE c.idpayformat = :idpayformat"),
    @NamedQuery(name = "creditPaymentFormat.findByEmailaddr", query = "SELECT c FROM creditPaymentFormat c WHERE c.emailaddr = :emailaddr"),
    @NamedQuery(name = "creditPaymentFormat.findByCardtype", query = "SELECT c FROM creditPaymentFormat c WHERE c.cardtype = :cardtype"),
    @NamedQuery(name = "creditPaymentFormat.findByCardnumber", query = "SELECT c FROM creditPaymentFormat c WHERE c.cardnumber = :cardnumber"),
    @NamedQuery(name = "creditPaymentFormat.findByHeadlinename", query = "SELECT c FROM creditPaymentFormat c WHERE c.headlinename = :headlinename"),
    @NamedQuery(name = "creditPaymentFormat.findByExpmonth", query = "SELECT c FROM creditPaymentFormat c WHERE c.expmonth = :expmonth"),
    @NamedQuery(name = "creditPaymentFormat.findByExpyear", query = "SELECT c FROM creditPaymentFormat c WHERE c.expyear = :expyear"),
    @NamedQuery(name = "creditPaymentFormat.findByRegisterdate", query = "SELECT c FROM creditPaymentFormat c WHERE c.registerdate = :registerdate")})
public class creditPaymentFormat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPAYFORMAT")
    private String idpayformat;
    @Basic(optional = false)
    @Column(name = "EMAILADDR")
    private String emailaddr;
    @Basic(optional = false)
    @Column(name = "CARDTYPE")
    private String cardtype;
    @Basic(optional = false)
    @Column(name = "CARDNUMBER")
    private String cardnumber;
    @Basic(optional = false)
    @Column(name = "HEADLINENAME")
    private String headlinename;
    @Basic(optional = false)
    @Column(name = "EXPMONTH")
    private short expmonth;
    @Basic(optional = false)
    @Column(name = "EXPYEAR")
    private short expyear;
    @Basic(optional = false)
    @Column(name = "REGISTERDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerdate;
    @OneToMany(mappedBy = "cpfIdpayformat", fetch = FetchType.EAGER)
    private Collection<formatSelect> formatSelectCollection;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private bank bankId;

    public creditPaymentFormat() {
    }

    public creditPaymentFormat(String idpayformat) {
        this.idpayformat = idpayformat;
    }

    public creditPaymentFormat(String idpayformat, String emailaddr, String cardtype, String cardnumber, String headlinename, short expmonth, short expyear, Date registerdate) {
        this.idpayformat = idpayformat;
        this.emailaddr = emailaddr;
        this.cardtype = cardtype;
        this.cardnumber = cardnumber;
        this.headlinename = headlinename;
        this.expmonth = expmonth;
        this.expyear = expyear;
        this.registerdate = registerdate;
    }

    public String getIdpayformat() {
        return idpayformat;
    }

    public void setIdpayformat(String idpayformat) {
        this.idpayformat = idpayformat;
    }

    public String getEmailaddr() {
        return emailaddr;
    }

    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getHeadlinename() {
        return headlinename;
    }

    public void setHeadlinename(String headlinename) {
        this.headlinename = headlinename;
    }

    public short getExpmonth() {
        return expmonth;
    }

    public void setExpmonth(short expmonth) {
        this.expmonth = expmonth;
    }

    public short getExpyear() {
        return expyear;
    }

    public void setExpyear(short expyear) {
        this.expyear = expyear;
    }

    public Date getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Date registerdate) {
        this.registerdate = registerdate;
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
        hash += (idpayformat != null ? idpayformat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof creditPaymentFormat)) {
            return false;
        }
        creditPaymentFormat other = (creditPaymentFormat) object;
        if ((this.idpayformat == null && other.idpayformat != null) || (this.idpayformat != null && !this.idpayformat.equals(other.idpayformat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.creditPaymentFormat[ idpayformat=" + idpayformat + " ]";
    }
    
}
