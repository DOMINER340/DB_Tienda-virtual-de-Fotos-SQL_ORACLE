/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.bank;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.paypal;
import java.util.ArrayList;
import java.util.Collection;
import entities.creditPaymentFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class bankJpaController implements Serializable {

    public bankJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(bank bank) throws PreexistingEntityException, Exception {
        if (bank.getPaypalCollection() == null) {
            bank.setPaypalCollection(new ArrayList<paypal>());
        }
        if (bank.getCreditPaymentFormatCollection() == null) {
            bank.setCreditPaymentFormatCollection(new ArrayList<creditPaymentFormat>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<paypal> attachedPaypalCollection = new ArrayList<paypal>();
            for (paypal paypalCollectionpaypalToAttach : bank.getPaypalCollection()) {
                paypalCollectionpaypalToAttach = em.getReference(paypalCollectionpaypalToAttach.getClass(), paypalCollectionpaypalToAttach.getIdpp());
                attachedPaypalCollection.add(paypalCollectionpaypalToAttach);
            }
            bank.setPaypalCollection(attachedPaypalCollection);
            Collection<creditPaymentFormat> attachedCreditPaymentFormatCollection = new ArrayList<creditPaymentFormat>();
            for (creditPaymentFormat creditPaymentFormatCollectioncreditPaymentFormatToAttach : bank.getCreditPaymentFormatCollection()) {
                creditPaymentFormatCollectioncreditPaymentFormatToAttach = em.getReference(creditPaymentFormatCollectioncreditPaymentFormatToAttach.getClass(), creditPaymentFormatCollectioncreditPaymentFormatToAttach.getIdpayformat());
                attachedCreditPaymentFormatCollection.add(creditPaymentFormatCollectioncreditPaymentFormatToAttach);
            }
            bank.setCreditPaymentFormatCollection(attachedCreditPaymentFormatCollection);
            em.persist(bank);
            for (paypal paypalCollectionpaypal : bank.getPaypalCollection()) {
                bank oldBankIdOfPaypalCollectionpaypal = paypalCollectionpaypal.getBankId();
                paypalCollectionpaypal.setBankId(bank);
                paypalCollectionpaypal = em.merge(paypalCollectionpaypal);
                if (oldBankIdOfPaypalCollectionpaypal != null) {
                    oldBankIdOfPaypalCollectionpaypal.getPaypalCollection().remove(paypalCollectionpaypal);
                    oldBankIdOfPaypalCollectionpaypal = em.merge(oldBankIdOfPaypalCollectionpaypal);
                }
            }
            for (creditPaymentFormat creditPaymentFormatCollectioncreditPaymentFormat : bank.getCreditPaymentFormatCollection()) {
                bank oldBankIdOfCreditPaymentFormatCollectioncreditPaymentFormat = creditPaymentFormatCollectioncreditPaymentFormat.getBankId();
                creditPaymentFormatCollectioncreditPaymentFormat.setBankId(bank);
                creditPaymentFormatCollectioncreditPaymentFormat = em.merge(creditPaymentFormatCollectioncreditPaymentFormat);
                if (oldBankIdOfCreditPaymentFormatCollectioncreditPaymentFormat != null) {
                    oldBankIdOfCreditPaymentFormatCollectioncreditPaymentFormat.getCreditPaymentFormatCollection().remove(creditPaymentFormatCollectioncreditPaymentFormat);
                    oldBankIdOfCreditPaymentFormatCollectioncreditPaymentFormat = em.merge(oldBankIdOfCreditPaymentFormatCollectioncreditPaymentFormat);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findbank(bank.getId()) != null) {
                throw new PreexistingEntityException("bank " + bank + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(bank bank) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bank persistentbank = em.find(bank.class, bank.getId());
            Collection<paypal> paypalCollectionOld = persistentbank.getPaypalCollection();
            Collection<paypal> paypalCollectionNew = bank.getPaypalCollection();
            Collection<creditPaymentFormat> creditPaymentFormatCollectionOld = persistentbank.getCreditPaymentFormatCollection();
            Collection<creditPaymentFormat> creditPaymentFormatCollectionNew = bank.getCreditPaymentFormatCollection();
            List<String> illegalOrphanMessages = null;
            for (paypal paypalCollectionOldpaypal : paypalCollectionOld) {
                if (!paypalCollectionNew.contains(paypalCollectionOldpaypal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain paypal " + paypalCollectionOldpaypal + " since its bankId field is not nullable.");
                }
            }
            for (creditPaymentFormat creditPaymentFormatCollectionOldcreditPaymentFormat : creditPaymentFormatCollectionOld) {
                if (!creditPaymentFormatCollectionNew.contains(creditPaymentFormatCollectionOldcreditPaymentFormat)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain creditPaymentFormat " + creditPaymentFormatCollectionOldcreditPaymentFormat + " since its bankId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<paypal> attachedPaypalCollectionNew = new ArrayList<paypal>();
            for (paypal paypalCollectionNewpaypalToAttach : paypalCollectionNew) {
                paypalCollectionNewpaypalToAttach = em.getReference(paypalCollectionNewpaypalToAttach.getClass(), paypalCollectionNewpaypalToAttach.getIdpp());
                attachedPaypalCollectionNew.add(paypalCollectionNewpaypalToAttach);
            }
            paypalCollectionNew = attachedPaypalCollectionNew;
            bank.setPaypalCollection(paypalCollectionNew);
            Collection<creditPaymentFormat> attachedCreditPaymentFormatCollectionNew = new ArrayList<creditPaymentFormat>();
            for (creditPaymentFormat creditPaymentFormatCollectionNewcreditPaymentFormatToAttach : creditPaymentFormatCollectionNew) {
                creditPaymentFormatCollectionNewcreditPaymentFormatToAttach = em.getReference(creditPaymentFormatCollectionNewcreditPaymentFormatToAttach.getClass(), creditPaymentFormatCollectionNewcreditPaymentFormatToAttach.getIdpayformat());
                attachedCreditPaymentFormatCollectionNew.add(creditPaymentFormatCollectionNewcreditPaymentFormatToAttach);
            }
            creditPaymentFormatCollectionNew = attachedCreditPaymentFormatCollectionNew;
            bank.setCreditPaymentFormatCollection(creditPaymentFormatCollectionNew);
            bank = em.merge(bank);
            for (paypal paypalCollectionNewpaypal : paypalCollectionNew) {
                if (!paypalCollectionOld.contains(paypalCollectionNewpaypal)) {
                    bank oldBankIdOfPaypalCollectionNewpaypal = paypalCollectionNewpaypal.getBankId();
                    paypalCollectionNewpaypal.setBankId(bank);
                    paypalCollectionNewpaypal = em.merge(paypalCollectionNewpaypal);
                    if (oldBankIdOfPaypalCollectionNewpaypal != null && !oldBankIdOfPaypalCollectionNewpaypal.equals(bank)) {
                        oldBankIdOfPaypalCollectionNewpaypal.getPaypalCollection().remove(paypalCollectionNewpaypal);
                        oldBankIdOfPaypalCollectionNewpaypal = em.merge(oldBankIdOfPaypalCollectionNewpaypal);
                    }
                }
            }
            for (creditPaymentFormat creditPaymentFormatCollectionNewcreditPaymentFormat : creditPaymentFormatCollectionNew) {
                if (!creditPaymentFormatCollectionOld.contains(creditPaymentFormatCollectionNewcreditPaymentFormat)) {
                    bank oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat = creditPaymentFormatCollectionNewcreditPaymentFormat.getBankId();
                    creditPaymentFormatCollectionNewcreditPaymentFormat.setBankId(bank);
                    creditPaymentFormatCollectionNewcreditPaymentFormat = em.merge(creditPaymentFormatCollectionNewcreditPaymentFormat);
                    if (oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat != null && !oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat.equals(bank)) {
                        oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat.getCreditPaymentFormatCollection().remove(creditPaymentFormatCollectionNewcreditPaymentFormat);
                        oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat = em.merge(oldBankIdOfCreditPaymentFormatCollectionNewcreditPaymentFormat);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = bank.getId();
                if (findbank(id) == null) {
                    throw new NonexistentEntityException("The bank with id " + id + " no longer exists.");
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
            bank bank;
            try {
                bank = em.getReference(bank.class, id);
                bank.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bank with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<paypal> paypalCollectionOrphanCheck = bank.getPaypalCollection();
            for (paypal paypalCollectionOrphanCheckpaypal : paypalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This bank (" + bank + ") cannot be destroyed since the paypal " + paypalCollectionOrphanCheckpaypal + " in its paypalCollection field has a non-nullable bankId field.");
            }
            Collection<creditPaymentFormat> creditPaymentFormatCollectionOrphanCheck = bank.getCreditPaymentFormatCollection();
            for (creditPaymentFormat creditPaymentFormatCollectionOrphanCheckcreditPaymentFormat : creditPaymentFormatCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This bank (" + bank + ") cannot be destroyed since the creditPaymentFormat " + creditPaymentFormatCollectionOrphanCheckcreditPaymentFormat + " in its creditPaymentFormatCollection field has a non-nullable bankId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(bank);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<bank> findbankEntities() {
        return findbankEntities(true, -1, -1);
    }

    public List<bank> findbankEntities(int maxResults, int firstResult) {
        return findbankEntities(false, maxResults, firstResult);
    }

    private List<bank> findbankEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(bank.class));
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

    public bank findbank(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(bank.class, id);
        } finally {
            em.close();
        }
    }

    public int getbankCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<bank> rt = cq.from(bank.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
