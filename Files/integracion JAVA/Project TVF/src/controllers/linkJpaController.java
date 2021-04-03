/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.link;
import entities.linkPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.purchase;
import entities.photo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class linkJpaController implements Serializable {

    public linkJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(link link) throws PreexistingEntityException, Exception {
        if (link.getLinkPK() == null) {
            link.setLinkPK(new linkPK());
        }
        link.getLinkPK().setPhName(link.getPhoto().getName());
        link.getLinkPK().setPId(link.getPurchase().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            purchase purchase = link.getPurchase();
            if (purchase != null) {
                purchase = em.getReference(purchase.getClass(), purchase.getId());
                link.setPurchase(purchase);
            }
            photo photo = link.getPhoto();
            if (photo != null) {
                photo = em.getReference(photo.getClass(), photo.getName());
                link.setPhoto(photo);
            }
            em.persist(link);
            if (purchase != null) {
                purchase.getLinkCollection().add(link);
                purchase = em.merge(purchase);
            }
            if (photo != null) {
                photo.getLinkCollection().add(link);
                photo = em.merge(photo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findlink(link.getLinkPK()) != null) {
                throw new PreexistingEntityException("link " + link + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(link link) throws NonexistentEntityException, Exception {
        link.getLinkPK().setPhName(link.getPhoto().getName());
        link.getLinkPK().setPId(link.getPurchase().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            link persistentlink = em.find(link.class, link.getLinkPK());
            purchase purchaseOld = persistentlink.getPurchase();
            purchase purchaseNew = link.getPurchase();
            photo photoOld = persistentlink.getPhoto();
            photo photoNew = link.getPhoto();
            if (purchaseNew != null) {
                purchaseNew = em.getReference(purchaseNew.getClass(), purchaseNew.getId());
                link.setPurchase(purchaseNew);
            }
            if (photoNew != null) {
                photoNew = em.getReference(photoNew.getClass(), photoNew.getName());
                link.setPhoto(photoNew);
            }
            link = em.merge(link);
            if (purchaseOld != null && !purchaseOld.equals(purchaseNew)) {
                purchaseOld.getLinkCollection().remove(link);
                purchaseOld = em.merge(purchaseOld);
            }
            if (purchaseNew != null && !purchaseNew.equals(purchaseOld)) {
                purchaseNew.getLinkCollection().add(link);
                purchaseNew = em.merge(purchaseNew);
            }
            if (photoOld != null && !photoOld.equals(photoNew)) {
                photoOld.getLinkCollection().remove(link);
                photoOld = em.merge(photoOld);
            }
            if (photoNew != null && !photoNew.equals(photoOld)) {
                photoNew.getLinkCollection().add(link);
                photoNew = em.merge(photoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                linkPK id = link.getLinkPK();
                if (findlink(id) == null) {
                    throw new NonexistentEntityException("The link with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(linkPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            link link;
            try {
                link = em.getReference(link.class, id);
                link.getLinkPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The link with id " + id + " no longer exists.", enfe);
            }
            purchase purchase = link.getPurchase();
            if (purchase != null) {
                purchase.getLinkCollection().remove(link);
                purchase = em.merge(purchase);
            }
            photo photo = link.getPhoto();
            if (photo != null) {
                photo.getLinkCollection().remove(link);
                photo = em.merge(photo);
            }
            em.remove(link);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<link> findlinkEntities() {
        return findlinkEntities(true, -1, -1);
    }

    public List<link> findlinkEntities(int maxResults, int firstResult) {
        return findlinkEntities(false, maxResults, firstResult);
    }

    private List<link> findlinkEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(link.class));
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

    public link findlink(linkPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(link.class, id);
        } finally {
            em.close();
        }
    }

    public int getlinkCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<link> rt = cq.from(link.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
