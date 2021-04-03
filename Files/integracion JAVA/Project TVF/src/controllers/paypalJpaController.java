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
import entities.bank;
import entities.formatSelect;
import entities.paypal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class paypalJpaController implements Serializable {

    public paypalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(paypal paypal) throws PreexistingEntityException, Exception {
        if (paypal.getFormatSelectCollection() == null) {
            paypal.setFormatSelectCollection(new ArrayList<formatSelect>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bank bankId = paypal.getBankId();
            if (bankId != null) {
                bankId = em.getReference(bankId.getClass(), bankId.getId());
                paypal.setBankId(bankId);
            }
            Collection<formatSelect> attachedFormatSelectCollection = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionformatSelectToAttach : paypal.getFormatSelectCollection()) {
                formatSelectCollectionformatSelectToAttach = em.getReference(formatSelectCollectionformatSelectToAttach.getClass(), formatSelectCollectionformatSelectToAttach.getIdfs());
                attachedFormatSelectCollection.add(formatSelectCollectionformatSelectToAttach);
            }
            paypal.setFormatSelectCollection(attachedFormatSelectCollection);
            em.persist(paypal);
            if (bankId != null) {
                bankId.getPaypalCollection().add(paypal);
                bankId = em.merge(bankId);
            }
            for (formatSelect formatSelectCollectionformatSelect : paypal.getFormatSelectCollection()) {
                paypal oldPaypalIdppOfFormatSelectCollectionformatSelect = formatSelectCollectionformatSelect.getPaypalIdpp();
                formatSelectCollectionformatSelect.setPaypalIdpp(paypal);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
                if (oldPaypalIdppOfFormatSelectCollectionformatSelect != null) {
                    oldPaypalIdppOfFormatSelectCollectionformatSelect.getFormatSelectCollection().remove(formatSelectCollectionformatSelect);
                    oldPaypalIdppOfFormatSelectCollectionformatSelect = em.merge(oldPaypalIdppOfFormatSelectCollectionformatSelect);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findpaypal(paypal.getIdpp()) != null) {
                throw new PreexistingEntityException("paypal " + paypal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(paypal paypal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            paypal persistentpaypal = em.find(paypal.class, paypal.getIdpp());
            bank bankIdOld = persistentpaypal.getBankId();
            bank bankIdNew = paypal.getBankId();
            Collection<formatSelect> formatSelectCollectionOld = persistentpaypal.getFormatSelectCollection();
            Collection<formatSelect> formatSelectCollectionNew = paypal.getFormatSelectCollection();
            if (bankIdNew != null) {
                bankIdNew = em.getReference(bankIdNew.getClass(), bankIdNew.getId());
                paypal.setBankId(bankIdNew);
            }
            Collection<formatSelect> attachedFormatSelectCollectionNew = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionNewformatSelectToAttach : formatSelectCollectionNew) {
                formatSelectCollectionNewformatSelectToAttach = em.getReference(formatSelectCollectionNewformatSelectToAttach.getClass(), formatSelectCollectionNewformatSelectToAttach.getIdfs());
                attachedFormatSelectCollectionNew.add(formatSelectCollectionNewformatSelectToAttach);
            }
            formatSelectCollectionNew = attachedFormatSelectCollectionNew;
            paypal.setFormatSelectCollection(formatSelectCollectionNew);
            paypal = em.merge(paypal);
            if (bankIdOld != null && !bankIdOld.equals(bankIdNew)) {
                bankIdOld.getPaypalCollection().remove(paypal);
                bankIdOld = em.merge(bankIdOld);
            }
            if (bankIdNew != null && !bankIdNew.equals(bankIdOld)) {
                bankIdNew.getPaypalCollection().add(paypal);
                bankIdNew = em.merge(bankIdNew);
            }
            for (formatSelect formatSelectCollectionOldformatSelect : formatSelectCollectionOld) {
                if (!formatSelectCollectionNew.contains(formatSelectCollectionOldformatSelect)) {
                    formatSelectCollectionOldformatSelect.setPaypalIdpp(null);
                    formatSelectCollectionOldformatSelect = em.merge(formatSelectCollectionOldformatSelect);
                }
            }
            for (formatSelect formatSelectCollectionNewformatSelect : formatSelectCollectionNew) {
                if (!formatSelectCollectionOld.contains(formatSelectCollectionNewformatSelect)) {
                    paypal oldPaypalIdppOfFormatSelectCollectionNewformatSelect = formatSelectCollectionNewformatSelect.getPaypalIdpp();
                    formatSelectCollectionNewformatSelect.setPaypalIdpp(paypal);
                    formatSelectCollectionNewformatSelect = em.merge(formatSelectCollectionNewformatSelect);
                    if (oldPaypalIdppOfFormatSelectCollectionNewformatSelect != null && !oldPaypalIdppOfFormatSelectCollectionNewformatSelect.equals(paypal)) {
                        oldPaypalIdppOfFormatSelectCollectionNewformatSelect.getFormatSelectCollection().remove(formatSelectCollectionNewformatSelect);
                        oldPaypalIdppOfFormatSelectCollectionNewformatSelect = em.merge(oldPaypalIdppOfFormatSelectCollectionNewformatSelect);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = paypal.getIdpp();
                if (findpaypal(id) == null) {
                    throw new NonexistentEntityException("The paypal with id " + id + " no longer exists.");
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
            paypal paypal;
            try {
                paypal = em.getReference(paypal.class, id);
                paypal.getIdpp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paypal with id " + id + " no longer exists.", enfe);
            }
            bank bankId = paypal.getBankId();
            if (bankId != null) {
                bankId.getPaypalCollection().remove(paypal);
                bankId = em.merge(bankId);
            }
            Collection<formatSelect> formatSelectCollection = paypal.getFormatSelectCollection();
            for (formatSelect formatSelectCollectionformatSelect : formatSelectCollection) {
                formatSelectCollectionformatSelect.setPaypalIdpp(null);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
            }
            em.remove(paypal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<paypal> findpaypalEntities() {
        return findpaypalEntities(true, -1, -1);
    }

    public List<paypal> findpaypalEntities(int maxResults, int firstResult) {
        return findpaypalEntities(false, maxResults, firstResult);
    }

    private List<paypal> findpaypalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(paypal.class));
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

    public paypal findpaypal(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(paypal.class, id);
        } finally {
            em.close();
        }
    }

    public int getpaypalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<paypal> rt = cq.from(paypal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
