/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Gustavo
 */
@Embeddable
public class linkPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "P_ID")
    private String pId;
    @Basic(optional = false)
    @Column(name = "PH_NAME")
    private String phName;

    public linkPK() {
    }

    public linkPK(String pId, String phName) {
        this.pId = pId;
        this.phName = phName;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPhName() {
        return phName;
    }

    public void setPhName(String phName) {
        this.phName = phName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pId != null ? pId.hashCode() : 0);
        hash += (phName != null ? phName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof linkPK)) {
            return false;
        }
        linkPK other = (linkPK) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        if ((this.phName == null && other.phName != null) || (this.phName != null && !this.phName.equals(other.phName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.linkPK[ pId=" + pId + ", phName=" + phName + " ]";
    }
    
}
