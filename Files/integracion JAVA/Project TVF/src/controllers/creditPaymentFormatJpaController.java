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
import entities.creditPaymentFormat;
import entities.formatSelect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class creditPaymentFormatJpaController implements Serializable {

    public creditPaymentFormatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(creditPaymentFormat creditPaymentFormat) throws PreexistingEntityException, Exception {
        if (creditPaymentFormat.getFormatSelectCollection() == null) {
            creditPaymentFormat.setFormatSelectCollection(new ArrayList<formatSelect>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bank bankId = creditPaymentFormat.getBankId();
            if (bankId != null) {
                bankId = em.getReference(bankId.getClass(), bankId.getId());
                creditPaymentFormat.setBankId(bankId);
            }
            Collection<formatSelect> attachedFormatSelectCollection = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionformatSelectToAttach : creditPaymentFormat.getFormatSelectCollection()) {
                formatSelectCollectionformatSelectToAttach = em.getReference(formatSelectCollectionformatSelectToAttach.getClass(), formatSelectCollectionformatSelectToAttach.getIdfs());
                attachedFormatSelectCollection.add(formatSelectCollectionformatSelectToAttach);
            }
            creditPaymentFormat.setFormatSelectCollection(attachedFormatSelectCollection);
            em.persist(creditPaymentFormat);
            if (bankId != null) {
                bankId.getCreditPaymentFormatCollection().add(creditPaymentFormat);
                bankId = em.merge(bankId);
            }
            for (formatSelect formatSelectCollectionformatSelect : creditPaymentFormat.getFormatSelectCollection()) {
                creditPaymentFormat oldCpfIdpayformatOfFormatSelectCollectionformatSelect = formatSelectCollectionformatSelect.getCpfIdpayformat();
                formatSelectCollectionformatSelect.setCpfIdpayformat(creditPaymentFormat);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
                if (oldCpfIdpayformatOfFormatSelectCollectionformatSelect != null) {
                    oldCpfIdpayformatOfFormatSelectCollectionformatSelect.getFormatSelectCollection().remove(formatSelectCollectionformatSelect);
                    oldCpfIdpayformatOfFormatSelectCollectionformatSelect = em.merge(oldCpfIdpayformatOfFormatSelectCollectionformatSelect);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findcreditPaymentFormat(creditPaymentFormat.getIdpayformat()) != null) {
                throw new PreexistingEntityException("creditPaymentFormat " + creditPaymentFormat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(creditPaymentFormat creditPaymentFormat) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            creditPaymentFormat persistentcreditPaymentFormat = em.find(creditPaymentFormat.class, creditPaymentFormat.getIdpayformat());
            bank bankIdOld = persistentcreditPaymentFormat.getBankId();
            bank bankIdNew = creditPaymentFormat.getBankId();
            Collection<formatSelect> formatSelectCollectionOld = persistentcreditPaymentFormat.getFormatSelectCollection();
            Collection<formatSelect> formatSelectCollectionNew = creditPaymentFormat.getFormatSelectCollection();
            if (bankIdNew != null) {
                bankIdNew = em.getReference(bankIdNew.getClass(), bankIdNew.getId());
                creditPaymentFormat.setBankId(bankIdNew);
            }
            Collection<formatSelect> attachedFormatSelectCollectionNew = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionNewformatSelectToAttach : formatSelectCollectionNew) {
                formatSelectCollectionNewformatSelectToAttach = em.getReference(formatSelectCollectionNewformatSelectToAttach.getClass(), formatSelectCollectionNewformatSelectToAttach.getIdfs());
                attachedFormatSelectCollectionNew.add(formatSelectCollectionNewformatSelectToAttach);
            }
            formatSelectCollectionNew = attachedFormatSelectCollectionNew;
            creditPaymentFormat.setFormatSelectCollection(formatSelectCollectionNew);
            creditPaymentFormat = em.merge(creditPaymentFormat);
            if (bankIdOld != null && !bankIdOld.equals(bankIdNew)) {
                bankIdOld.getCreditPaymentFormatCollection().remove(creditPaymentFormat);
                bankIdOld = em.merge(bankIdOld);
            }
            if (bankIdNew != null && !bankIdNew.equals(bankIdOld)) {
                bankIdNew.getCreditPaymentFormatCollection().add(creditPaymentFormat);
                bankIdNew = em.merge(bankIdNew);
            }
            for (formatSelect formatSelectCollectionOldformatSelect : formatSelectCollectionOld) {
                if (!formatSelectCollectionNew.contains(formatSelectCollectionOldformatSelect)) {
                    formatSelectCollectionOldformatSelect.setCpfIdpayformat(null);
                    formatSelectCollectionOldformatSelect = em.merge(formatSelectCollectionOldformatSelect);
                }
            }
            for (formatSelect formatSelectCollectionNewformatSelect : formatSelectCollectionNew) {
                if (!formatSelectCollectionOld.contains(formatSelectCollectionNewformatSelect)) {
                    creditPaymentFormat oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect = formatSelectCollectionNewformatSelect.getCpfIdpayformat();
                    formatSelectCollectionNewformatSelect.setCpfIdpayformat(creditPaymentFormat);
                    formatSelectCollectionNewformatSelect = em.merge(formatSelectCollectionNewformatSelect);
                    if (oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect != null && !oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect.equals(creditPaymentFormat)) {
                        oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect.getFormatSelectCollection().remove(formatSelectCollectionNewformatSelect);
                        oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect = em.merge(oldCpfIdpayformatOfFormatSelectCollectionNewformatSelect);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = creditPaymentFormat.getIdpayformat();
                if (findcreditPaymentFormat(id) == null) {
                    throw new NonexistentEntityException("The creditPaymentFormat with id " + id + " no longer exists.");
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
            creditPaymentFormat creditPaymentFormat;
            try {
                creditPaymentFormat = em.getReference(creditPaymentFormat.class, id);
                creditPaymentFormat.getIdpayformat();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The creditPaymentFormat with id " + id + " no longer exists.", enfe);
            }
            bank bankId = creditPaymentFormat.getBankId();
            if (bankId != null) {
                bankId.getCreditPaymentFormatCollection().remove(creditPaymentFormat);
                bankId = em.merge(bankId);
            }
            Collection<formatSelect> formatSelectCollection = creditPaymentFormat.getFormatSelectCollection();
            for (formatSelect formatSelectCollectionformatSelect : formatSelectCollection) {
                formatSelectCollectionformatSelect.setCpfIdpayformat(null);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
            }
            em.remove(creditPaymentFormat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<creditPaymentFormat> findcreditPaymentFormatEntities() {
        return findcreditPaymentFormatEntities(true, -1, -1);
    }

    public List<creditPaymentFormat> findcreditPaymentFormatEntities(int maxResults, int firstResult) {
        return findcreditPaymentFormatEntities(false, maxResults, firstResult);
    }

    private List<creditPaymentFormat> findcreditPaymentFormatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(creditPaymentFormat.class));
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

    public creditPaymentFormat findcreditPaymentFormat(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(creditPaymentFormat.class, id);
        } finally {
            em.close();
        }
    }

    public int getcreditPaymentFormatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<creditPaymentFormat> rt = cq.from(creditPaymentFormat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
