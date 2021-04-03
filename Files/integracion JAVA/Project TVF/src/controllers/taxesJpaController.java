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
import entities.purchase;
import entities.taxes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class taxesJpaController implements Serializable {

    public taxesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(taxes taxes) throws PreexistingEntityException, Exception {
        if (taxes.getPurchaseCollection() == null) {
            taxes.setPurchaseCollection(new ArrayList<purchase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<purchase> attachedPurchaseCollection = new ArrayList<purchase>();
            for (purchase purchaseCollectionpurchaseToAttach : taxes.getPurchaseCollection()) {
                purchaseCollectionpurchaseToAttach = em.getReference(purchaseCollectionpurchaseToAttach.getClass(), purchaseCollectionpurchaseToAttach.getId());
                attachedPurchaseCollection.add(purchaseCollectionpurchaseToAttach);
            }
            taxes.setPurchaseCollection(attachedPurchaseCollection);
            em.persist(taxes);
            for (purchase purchaseCollectionpurchase : taxes.getPurchaseCollection()) {
                purchaseCollectionpurchase.getTaxesCollection().add(taxes);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findtaxes(taxes.getName()) != null) {
                throw new PreexistingEntityException("taxes " + taxes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(taxes taxes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taxes persistenttaxes = em.find(taxes.class, taxes.getName());
            Collection<purchase> purchaseCollectionOld = persistenttaxes.getPurchaseCollection();
            Collection<purchase> purchaseCollectionNew = taxes.getPurchaseCollection();
            Collection<purchase> attachedPurchaseCollectionNew = new ArrayList<purchase>();
            for (purchase purchaseCollectionNewpurchaseToAttach : purchaseCollectionNew) {
                purchaseCollectionNewpurchaseToAttach = em.getReference(purchaseCollectionNewpurchaseToAttach.getClass(), purchaseCollectionNewpurchaseToAttach.getId());
                attachedPurchaseCollectionNew.add(purchaseCollectionNewpurchaseToAttach);
            }
            purchaseCollectionNew = attachedPurchaseCollectionNew;
            taxes.setPurchaseCollection(purchaseCollectionNew);
            taxes = em.merge(taxes);
            for (purchase purchaseCollectionOldpurchase : purchaseCollectionOld) {
                if (!purchaseCollectionNew.contains(purchaseCollectionOldpurchase)) {
                    purchaseCollectionOldpurchase.getTaxesCollection().remove(taxes);
                    purchaseCollectionOldpurchase = em.merge(purchaseCollectionOldpurchase);
                }
            }
            for (purchase purchaseCollectionNewpurchase : purchaseCollectionNew) {
                if (!purchaseCollectionOld.contains(purchaseCollectionNewpurchase)) {
                    purchaseCollectionNewpurchase.getTaxesCollection().add(taxes);
                    purchaseCollectionNewpurchase = em.merge(purchaseCollectionNewpurchase);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = taxes.getName();
                if (findtaxes(id) == null) {
                    throw new NonexistentEntityException("The taxes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taxes taxes;
            try {
                taxes = em.getReference(taxes.class, id);
                taxes.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taxes with id " + id + " no longer exists.", enfe);
            }
            Collection<purchase> purchaseCollection = taxes.getPurchaseCollection();
            for (purchase purchaseCollectionpurchase : purchaseCollection) {
                purchaseCollectionpurchase.getTaxesCollection().remove(taxes);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
            }
            em.remove(taxes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<taxes> findtaxesEntities() {
        return findtaxesEntities(true, -1, -1);
    }

    public List<taxes> findtaxesEntities(int maxResults, int firstResult) {
        return findtaxesEntities(false, maxResults, firstResult);
    }

    private List<taxes> findtaxesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(taxes.class));
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

    public taxes findtaxes(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(taxes.class, id);
        } finally {
            em.close();
        }
    }

    public int gettaxesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<taxes> rt = cq.from(taxes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
