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
public class phxvaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "SV_ID")
    private String svId;
    @Basic(optional = false)
    @Column(name = "PH_NAME")
    private String phName;

    public phxvaPK() {
    }

    public phxvaPK(String svId, String phName) {
        this.svId = svId;
        this.phName = phName;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
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
        hash += (svId != null ? svId.hashCode() : 0);
        hash += (phName != null ? phName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof phxvaPK)) {
            return false;
        }
        phxvaPK other = (phxvaPK) object;
        if ((this.svId == null && other.svId != null) || (this.svId != null && !this.svId.equals(other.svId))) {
            return false;
        }
        if ((this.phName == null && other.phName != null) || (this.phName != null && !this.phName.equals(other.phName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.phxvaPK[ svId=" + svId + ", phName=" + phName + " ]";
    }
    
}
