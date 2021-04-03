/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.discount;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.purchase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class discountJpaController implements Serializable {

    public discountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(discount discount) throws PreexistingEntityException, Exception {
        if (discount.getPurchaseCollection() == null) {
            discount.setPurchaseCollection(new ArrayList<purchase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<purchase> attachedPurchaseCollection = new ArrayList<purchase>();
            for (purchase purchaseCollectionpurchaseToAttach : discount.getPurchaseCollection()) {
                purchaseCollectionpurchaseToAttach = em.getReference(purchaseCollectionpurchaseToAttach.getClass(), purchaseCollectionpurchaseToAttach.getId());
                attachedPurchaseCollection.add(purchaseCollectionpurchaseToAttach);
            }
            discount.setPurchaseCollection(attachedPurchaseCollection);
            em.persist(discount);
            for (purchase purchaseCollectionpurchase : discount.getPurchaseCollection()) {
                discount oldDIdOfPurchaseCollectionpurchase = purchaseCollectionpurchase.getDId();
                purchaseCollectionpurchase.setDId(discount);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
                if (oldDIdOfPurchaseCollectionpurchase != null) {
                    oldDIdOfPurchaseCollectionpurchase.getPurchaseCollection().remove(purchaseCollectionpurchase);
                    oldDIdOfPurchaseCollectionpurchase = em.merge(oldDIdOfPurchaseCollectionpurchase);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (finddiscount(discount.getId()) != null) {
                throw new PreexistingEntityException("discount " + discount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(discount discount) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            discount persistentdiscount = em.find(discount.class, discount.getId());
            Collection<purchase> purchaseCollectionOld = persistentdiscount.getPurchaseCollection();
            Collection<purchase> purchaseCollectionNew = discount.getPurchaseCollection();
            List<String> illegalOrphanMessages = null;
            for (purchase purchaseCollectionOldpurchase : purchaseCollectionOld) {
                if (!purchaseCollectionNew.contains(purchaseCollectionOldpurchase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain purchase " + purchaseCollectionOldpurchase + " since its DId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<purchase> attachedPurchaseCollectionNew = new ArrayList<purchase>();
            for (purchase purchaseCollectionNewpurchaseToAttach : purchaseCollectionNew) {
                purchaseCollectionNewpurchaseToAttach = em.getReference(purchaseCollectionNewpurchaseToAttach.getClass(), purchaseCollectionNewpurchaseToAttach.getId());
                attachedPurchaseCollectionNew.add(purchaseCollectionNewpurchaseToAttach);
            }
            purchaseCollectionNew = attachedPurchaseCollectionNew;
            discount.setPurchaseCollection(purchaseCollectionNew);
            discount = em.merge(discount);
            for (purchase purchaseCollectionNewpurchase : purchaseCollectionNew) {
                if (!purchaseCollectionOld.contains(purchaseCollectionNewpurchase)) {
                    discount oldDIdOfPurchaseCollectionNewpurchase = purchaseCollectionNewpurchase.getDId();
                    purchaseCollectionNewpurchase.setDId(discount);
                    purchaseCollectionNewpurchase = em.merge(purchaseCollectionNewpurchase);
                    if (oldDIdOfPurchaseCollectionNewpurchase != null && !oldDIdOfPurchaseCollectionNewpurchase.equals(discount)) {
                        oldDIdOfPurchaseCollectionNewpurchase.getPurchaseCollection().remove(purchaseCollectionNewpurchase);
                        oldDIdOfPurchaseCollectionNewpurchase = em.merge(oldDIdOfPurchaseCollectionNewpurchase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = discount.getId();
                if (finddiscount(id) == null) {
                    throw new NonexistentEntityException("The discount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            discount discount;
            try {
                discount = em.getReference(discount.class, id);
                discount.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discount with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<purchase> purchaseCollectionOrphanCheck = discount.getPurchaseCollection();
            for (purchase purchaseCollectionOrphanCheckpurchase : purchaseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This discount (" + discount + ") cannot be destroyed since the purchase " + purchaseCollectionOrphanCheckpurchase + " in its purchaseCollection field has a non-nullable DId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(discount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<discount> finddiscountEntities() {
        return finddiscountEntities(true, -1, -1);
    }

    public List<discount> finddiscountEntities(int maxResults, int firstResult) {
        return finddiscountEntities(false, maxResults, firstResult);
    }

    private List<discount> finddiscountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(discount.class));
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

    public discount finddiscount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(discount.class, id);
        } finally {
            em.close();
        }
    }

    public int getdiscountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<discount> rt = cq.from(discount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
