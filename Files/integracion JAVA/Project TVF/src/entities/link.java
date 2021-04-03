/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gustavo
 */
@Entity
@Table(name = "LINK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "link.findAll", query = "SELECT l FROM link l"),
    @NamedQuery(name = "link.findByPId", query = "SELECT l FROM link l WHERE l.linkPK.pId = :pId"),
    @NamedQuery(name = "link.findByPhName", query = "SELECT l FROM link l WHERE l.linkPK.phName = :phName"),
    @NamedQuery(name = "link.findByLink", query = "SELECT l FROM link l WHERE l.link = :link"),
    @NamedQuery(name = "link.findByDownlCount", query = "SELECT l FROM link l WHERE l.downlCount = :downlCount")})
public class link implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected linkPK linkPK;
    @Basic(optional = false)
    @Column(name = "LINK")
    private String link;
    @Basic(optional = false)
    @Column(name = "DOWNL_COUNT")
    private int downlCount;
    @JoinColumn(name = "P_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private purchase purchase;
    @JoinColumn(name = "PH_NAME", referencedColumnName = "NAME", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private photo photo;

    public link() {
    }

    public link(linkPK linkPK) {
        this.linkPK = linkPK;
    }

    public link(linkPK linkPK, String link, int downlCount) {
        this.linkPK = linkPK;
        this.link = link;
        this.downlCount = downlCount;
    }

    public link(String pId, String phName) {
        this.linkPK = new linkPK(pId, phName);
    }

    public linkPK getLinkPK() {
        return linkPK;
    }

    public void setLinkPK(linkPK linkPK) {
        this.linkPK = linkPK;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDownlCount() {
        return downlCount;
    }

    public void setDownlCount(int downlCount) {
        this.downlCount = downlCount;
    }

    public purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(purchase purchase) {
        this.purchase = purchase;
    }

    public photo getPhoto() {
        return photo;
    }

    public void setPhoto(photo photo) {
        this.photo = photo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (linkPK != null ? linkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof link)) {
            return false;
        }
        link other = (link) object;
        if ((this.linkPK == null && other.linkPK != null) || (this.linkPK != null && !this.linkPK.equals(other.linkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.link[ linkPK=" + linkPK + " ]";
    }
    
}
