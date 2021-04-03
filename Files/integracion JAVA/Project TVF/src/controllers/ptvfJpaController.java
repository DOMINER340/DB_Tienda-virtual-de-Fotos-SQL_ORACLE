/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.account;
import java.util.ArrayList;
import java.util.Collection;
import entities.photo;
import entities.ptvf;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class ptvfJpaController implements Serializable {

    public ptvfJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ptvf ptvf) throws PreexistingEntityException, Exception {
        if (ptvf.getAccountCollection() == null) {
            ptvf.setAccountCollection(new ArrayList<account>());
        }
        if (ptvf.getPhotoCollection() == null) {
            ptvf.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<account> attachedAccountCollection = new ArrayList<account>();
            for (account accountCollectionaccountToAttach : ptvf.getAccountCollection()) {
                accountCollectionaccountToAttach = em.getReference(accountCollectionaccountToAttach.getClass(), accountCollectionaccountToAttach.getUsername());
                attachedAccountCollection.add(accountCollectionaccountToAttach);
            }
            ptvf.setAccountCollection(attachedAccountCollection);
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : ptvf.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            ptvf.setPhotoCollection(attachedPhotoCollection);
            em.persist(ptvf);
            for (account accountCollectionaccount : ptvf.getAccountCollection()) {
                ptvf oldPtvfNameOfAccountCollectionaccount = accountCollectionaccount.getPtvfName();
                accountCollectionaccount.setPtvfName(ptvf);
                accountCollectionaccount = em.merge(accountCollectionaccount);
                if (oldPtvfNameOfAccountCollectionaccount != null) {
                    oldPtvfNameOfAccountCollectionaccount.getAccountCollection().remove(accountCollectionaccount);
                    oldPtvfNameOfAccountCollectionaccount = em.merge(oldPtvfNameOfAccountCollectionaccount);
                }
            }
            for (photo photoCollectionphoto : ptvf.getPhotoCollection()) {
                ptvf oldPtvfNameOfPhotoCollectionphoto = photoCollectionphoto.getPtvfName();
                photoCollectionphoto.setPtvfName(ptvf);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldPtvfNameOfPhotoCollectionphoto != null) {
                    oldPtvfNameOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldPtvfNameOfPhotoCollectionphoto = em.merge(oldPtvfNameOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findptvf(ptvf.getName()) != null) {
                throw new PreexistingEntityException("ptvf " + ptvf + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ptvf ptvf) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ptvf persistentptvf = em.find(ptvf.class, ptvf.getName());
            Collection<account> accountCollectionOld = persistentptvf.getAccountCollection();
            Collection<account> accountCollectionNew = ptvf.getAccountCollection();
            Collection<photo> photoCollectionOld = persistentptvf.getPhotoCollection();
            Collection<photo> photoCollectionNew = ptvf.getPhotoCollection();
            List<String> illegalOrphanMessages = null;
            for (account accountCollectionOldaccount : accountCollectionOld) {
                if (!accountCollectionNew.contains(accountCollectionOldaccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain account " + accountCollectionOldaccount + " since its ptvfName field is not nullable.");
                }
            }
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain photo " + photoCollectionOldphoto + " since its ptvfName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<account> attachedAccountCollectionNew = new ArrayList<account>();
            for (account accountCollectionNewaccountToAttach : accountCollectionNew) {
                accountCollectionNewaccountToAttach = em.getReference(accountCollectionNewaccountToAttach.getClass(), accountCollectionNewaccountToAttach.getUsername());
                attachedAccountCollectionNew.add(accountCollectionNewaccountToAttach);
            }
            accountCollectionNew = attachedAccountCollectionNew;
            ptvf.setAccountCollection(accountCollectionNew);
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            ptvf.setPhotoCollection(photoCollectionNew);
            ptvf = em.merge(ptvf);
            for (account accountCollectionNewaccount : accountCollectionNew) {
                if (!accountCollectionOld.contains(accountCollectionNewaccount)) {
                    ptvf oldPtvfNameOfAccountCollectionNewaccount = accountCollectionNewaccount.getPtvfName();
                    accountCollectionNewaccount.setPtvfName(ptvf);
                    accountCollectionNewaccount = em.merge(accountCollectionNewaccount);
                    if (oldPtvfNameOfAccountCollectionNewaccount != null && !oldPtvfNameOfAccountCollectionNewaccount.equals(ptvf)) {
                        oldPtvfNameOfAccountCollectionNewaccount.getAccountCollection().remove(accountCollectionNewaccount);
                        oldPtvfNameOfAccountCollectionNewaccount = em.merge(oldPtvfNameOfAccountCollectionNewaccount);
                    }
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    ptvf oldPtvfNameOfPhotoCollectionNewphoto = photoCollectionNewphoto.getPtvfName();
                    photoCollectionNewphoto.setPtvfName(ptvf);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldPtvfNameOfPhotoCollectionNewphoto != null && !oldPtvfNameOfPhotoCollectionNewphoto.equals(ptvf)) {
                        oldPtvfNameOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldPtvfNameOfPhotoCollectionNewphoto = em.merge(oldPtvfNameOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ptvf.getName();
                if (findptvf(id) == null) {
                    throw new NonexistentEntityException("The ptvf with id " + id + " no longer exists.");
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
            ptvf ptvf;
            try {
                ptvf = em.getReference(ptvf.class, id);
                ptvf.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ptvf with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<account> accountCollectionOrphanCheck = ptvf.getAccountCollection();
            for (account accountCollectionOrphanCheckaccount : accountCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ptvf (" + ptvf + ") cannot be destroyed since the account " + accountCollectionOrphanCheckaccount + " in its accountCollection field has a non-nullable ptvfName field.");
            }
            Collection<photo> photoCollectionOrphanCheck = ptvf.getPhotoCollection();
            for (photo photoCollectionOrphanCheckphoto : photoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ptvf (" + ptvf + ") cannot be destroyed since the photo " + photoCollectionOrphanCheckphoto + " in its photoCollection field has a non-nullable ptvfName field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ptvf);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ptvf> findptvfEntities() {
        return findptvfEntities(true, -1, -1);
    }

    public List<ptvf> findptvfEntities(int maxResults, int firstResult) {
        return findptvfEntities(false, maxResults, firstResult);
    }

    private List<ptvf> findptvfEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ptvf.class));
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

    public ptvf findptvf(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ptvf.class, id);
        } finally {
            em.close();
        }
    }

    public int getptvfCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ptvf> rt = cq.from(ptvf.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
