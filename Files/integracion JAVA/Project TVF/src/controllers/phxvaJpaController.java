/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.photo;
import entities.phxva;
import entities.phxvaPK;
import entities.statsVisitor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class phxvaJpaController implements Serializable {

    public phxvaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(phxva phxva) throws PreexistingEntityException, Exception {
        if (phxva.getPhxvaPK() == null) {
            phxva.setPhxvaPK(new phxvaPK());
        }
        phxva.getPhxvaPK().setSvId(phxva.getStatsVisitor().getId());
        phxva.getPhxvaPK().setPhName(phxva.getPhoto().getName());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            photo photo = phxva.getPhoto();
            if (photo != null) {
                photo = em.getReference(photo.getClass(), photo.getName());
                phxva.setPhoto(photo);
            }
            statsVisitor statsVisitor = phxva.getStatsVisitor();
            if (statsVisitor != null) {
                statsVisitor = em.getReference(statsVisitor.getClass(), statsVisitor.getId());
                phxva.setStatsVisitor(statsVisitor);
            }
            em.persist(phxva);
            if (photo != null) {
                photo.getPhxvaCollection().add(phxva);
                photo = em.merge(photo);
            }
            if (statsVisitor != null) {
                statsVisitor.getPhxvaCollection().add(phxva);
                statsVisitor = em.merge(statsVisitor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findphxva(phxva.getPhxvaPK()) != null) {
                throw new PreexistingEntityException("phxva " + phxva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(phxva phxva) throws NonexistentEntityException, Exception {
        phxva.getPhxvaPK().setSvId(phxva.getStatsVisitor().getId());
        phxva.getPhxvaPK().setPhName(phxva.getPhoto().getName());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            phxva persistentphxva = em.find(phxva.class, phxva.getPhxvaPK());
            photo photoOld = persistentphxva.getPhoto();
            photo photoNew = phxva.getPhoto();
            statsVisitor statsVisitorOld = persistentphxva.getStatsVisitor();
            statsVisitor statsVisitorNew = phxva.getStatsVisitor();
            if (photoNew != null) {
                photoNew = em.getReference(photoNew.getClass(), photoNew.getName());
                phxva.setPhoto(photoNew);
            }
            if (statsVisitorNew != null) {
                statsVisitorNew = em.getReference(statsVisitorNew.getClass(), statsVisitorNew.getId());
                phxva.setStatsVisitor(statsVisitorNew);
            }
            phxva = em.merge(phxva);
            if (photoOld != null && !photoOld.equals(photoNew)) {
                photoOld.getPhxvaCollection().remove(phxva);
                photoOld = em.merge(photoOld);
            }
            if (photoNew != null && !photoNew.equals(photoOld)) {
                photoNew.getPhxvaCollection().add(phxva);
                photoNew = em.merge(photoNew);
            }
            if (statsVisitorOld != null && !statsVisitorOld.equals(statsVisitorNew)) {
                statsVisitorOld.getPhxvaCollection().remove(phxva);
                statsVisitorOld = em.merge(statsVisitorOld);
            }
            if (statsVisitorNew != null && !statsVisitorNew.equals(statsVisitorOld)) {
                statsVisitorNew.getPhxvaCollection().add(phxva);
                statsVisitorNew = em.merge(statsVisitorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                phxvaPK id = phxva.getPhxvaPK();
                if (findphxva(id) == null) {
                    throw new NonexistentEntityException("The phxva with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(phxvaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            phxva phxva;
            try {
                phxva = em.getReference(phxva.class, id);
                phxva.getPhxvaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phxva with id " + id + " no longer exists.", enfe);
            }
            photo photo = phxva.getPhoto();
            if (photo != null) {
                photo.getPhxvaCollection().remove(phxva);
                photo = em.merge(photo);
            }
            statsVisitor statsVisitor = phxva.getStatsVisitor();
            if (statsVisitor != null) {
                statsVisitor.getPhxvaCollection().remove(phxva);
                statsVisitor = em.merge(statsVisitor);
            }
            em.remove(phxva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<phxva> findphxvaEntities() {
        return findphxvaEntities(true, -1, -1);
    }

    public List<phxva> findphxvaEntities(int maxResults, int firstResult) {
        return findphxvaEntities(false, maxResults, firstResult);
    }

    private List<phxva> findphxvaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(phxva.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public phxva findphxva(phxvaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(phxva.class, id);
        } finally {
            em.close();
        }
    }

    public int getphxvaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<phxva> rt = cq.from(phxva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
