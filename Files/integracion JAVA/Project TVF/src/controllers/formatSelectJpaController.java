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
import entities.creditPaymentFormat;
import entities.formatSelect;
import entities.paypal;
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
public class formatSelectJpaController implements Serializable {

    public formatSelectJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(formatSelect formatSelect) throws PreexistingEntityException, Exception {
        if (formatSelect.getPurchaseCollection() == null) {
            formatSelect.setPurchaseCollection(new ArrayList<purchase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            creditPaymentFormat cpfIdpayformat = formatSelect.getCpfIdpayformat();
            if (cpfIdpayformat != null) {
                cpfIdpayformat = em.getReference(cpfIdpayformat.getClass(), cpfIdpayformat.getIdpayformat());
                formatSelect.setCpfIdpayformat(cpfIdpayformat);
            }
            paypal paypalIdpp = formatSelect.getPaypalIdpp();
            if (paypalIdpp != null) {
                paypalIdpp = em.getReference(paypalIdpp.getClass(), paypalIdpp.getIdpp());
                formatSelect.setPaypalIdpp(paypalIdpp);
            }
            Collection<purchase> attachedPurchaseCollection = new ArrayList<purchase>();
            for (purchase purchaseCollectionpurchaseToAttach : formatSelect.getPurchaseCollection()) {
                purchaseCollectionpurchaseToAttach = em.getReference(purchaseCollectionpurchaseToAttach.getClass(), purchaseCollectionpurchaseToAttach.getId());
                attachedPurchaseCollection.add(purchaseCollectionpurchaseToAttach);
            }
            formatSelect.setPurchaseCollection(attachedPurchaseCollection);
            em.persist(formatSelect);
            if (cpfIdpayformat != null) {
                cpfIdpayformat.getFormatSelectCollection().add(formatSelect);
                cpfIdpayformat = em.merge(cpfIdpayformat);
            }
            if (paypalIdpp != null) {
                paypalIdpp.getFormatSelectCollection().add(formatSelect);
                paypalIdpp = em.merge(paypalIdpp);
            }
            for (purchase purchaseCollectionpurchase : formatSelect.getPurchaseCollection()) {
                purchaseCollectionpurchase.getFormatSelectCollection().add(formatSelect);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findformatSelect(formatSelect.getIdfs()) != null) {
                throw new PreexistingEntityException("formatSelect " + formatSelect + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(formatSelect formatSelect) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            formatSelect persistentformatSelect = em.find(formatSelect.class, formatSelect.getIdfs());
            creditPaymentFormat cpfIdpayformatOld = persistentformatSelect.getCpfIdpayformat();
            creditPaymentFormat cpfIdpayformatNew = formatSelect.getCpfIdpayformat();
            paypal paypalIdppOld = persistentformatSelect.getPaypalIdpp();
            paypal paypalIdppNew = formatSelect.getPaypalIdpp();
            Collection<purchase> purchaseCollectionOld = persistentformatSelect.getPurchaseCollection();
            Collection<purchase> purchaseCollectionNew = formatSelect.getPurchaseCollection();
            if (cpfIdpayformatNew != null) {
                cpfIdpayformatNew = em.getReference(cpfIdpayformatNew.getClass(), cpfIdpayformatNew.getIdpayformat());
                formatSelect.setCpfIdpayformat(cpfIdpayformatNew);
            }
            if (paypalIdppNew != null) {
                paypalIdppNew = em.getReference(paypalIdppNew.getClass(), paypalIdppNew.getIdpp());
                formatSelect.setPaypalIdpp(paypalIdppNew);
            }
            Collection<purchase> attachedPurchaseCollectionNew = new ArrayList<purchase>();
            for (purchase purchaseCollectionNewpurchaseToAttach : purchaseCollectionNew) {
                purchaseCollectionNewpurchaseToAttach = em.getReference(purchaseCollectionNewpurchaseToAttach.getClass(), purchaseCollectionNewpurchaseToAttach.getId());
                attachedPurchaseCollectionNew.add(purchaseCollectionNewpurchaseToAttach);
            }
            purchaseCollectionNew = attachedPurchaseCollectionNew;
            formatSelect.setPurchaseCollection(purchaseCollectionNew);
            formatSelect = em.merge(formatSelect);
            if (cpfIdpayformatOld != null && !cpfIdpayformatOld.equals(cpfIdpayformatNew)) {
                cpfIdpayformatOld.getFormatSelectCollection().remove(formatSelect);
                cpfIdpayformatOld = em.merge(cpfIdpayformatOld);
            }
            if (cpfIdpayformatNew != null && !cpfIdpayformatNew.equals(cpfIdpayformatOld)) {
                cpfIdpayformatNew.getFormatSelectCollection().add(formatSelect);
                cpfIdpayformatNew = em.merge(cpfIdpayformatNew);
            }
            if (paypalIdppOld != null && !paypalIdppOld.equals(paypalIdppNew)) {
                paypalIdppOld.getFormatSelectCollection().remove(formatSelect);
                paypalIdppOld = em.merge(paypalIdppOld);
            }
            if (paypalIdppNew != null && !paypalIdppNew.equals(paypalIdppOld)) {
                paypalIdppNew.getFormatSelectCollection().add(formatSelect);
                paypalIdppNew = em.merge(paypalIdppNew);
            }
            for (purchase purchaseCollectionOldpurchase : purchaseCollectionOld) {
                if (!purchaseCollectionNew.contains(purchaseCollectionOldpurchase)) {
                    purchaseCollectionOldpurchase.getFormatSelectCollection().remove(formatSelect);
                    purchaseCollectionOldpurchase = em.merge(purchaseCollectionOldpurchase);
                }
            }
            for (purchase purchaseCollectionNewpurchase : purchaseCollectionNew) {
                if (!purchaseCollectionOld.contains(purchaseCollectionNewpurchase)) {
                    purchaseCollectionNewpurchase.getFormatSelectCollection().add(formatSelect);
                    purchaseCollectionNewpurchase = em.merge(purchaseCollectionNewpurchase);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = formatSelect.getIdfs();
                if (findformatSelect(id) == null) {
                    throw new NonexistentEntityException("The formatSelect with id " + id + " no longer exists.");
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
            formatSelect formatSelect;
            try {
                formatSelect = em.getReference(formatSelect.class, id);
                formatSelect.getIdfs();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formatSelect with id " + id + " no longer exists.", enfe);
            }
            creditPaymentFormat cpfIdpayformat = formatSelect.getCpfIdpayformat();
            if (cpfIdpayformat != null) {
                cpfIdpayformat.getFormatSelectCollection().remove(formatSelect);
                cpfIdpayformat = em.merge(cpfIdpayformat);
            }
            paypal paypalIdpp = formatSelect.getPaypalIdpp();
            if (paypalIdpp != null) {
                paypalIdpp.getFormatSelectCollection().remove(formatSelect);
                paypalIdpp = em.merge(paypalIdpp);
            }
            Collection<purchase> purchaseCollection = formatSelect.getPurchaseCollection();
            for (purchase purchaseCollectionpurchase : purchaseCollection) {
                purchaseCollectionpurchase.getFormatSelectCollection().remove(formatSelect);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
            }
            em.remove(formatSelect);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<formatSelect> findformatSelectEntities() {
        return findformatSelectEntities(true, -1, -1);
    }

    public List<formatSelect> findformatSelectEntities(int maxResults, int firstResult) {
        return findformatSelectEntities(false, maxResults, firstResult);
    }

    private List<formatSelect> findformatSelectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(formatSelect.class));
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

    public formatSelect findformatSelect(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(formatSelect.class, id);
        } finally {
            em.close();
        }
    }

    public int getformatSelectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<formatSelect> rt = cq.from(formatSelect.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
