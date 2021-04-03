/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.country;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.photo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class countryJpaController implements Serializable {

    public countryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(country country) throws PreexistingEntityException, Exception {
        if (country.getPhotoCollection() == null) {
            country.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : country.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            country.setPhotoCollection(attachedPhotoCollection);
            em.persist(country);
            for (photo photoCollectionphoto : country.getPhotoCollection()) {
                country oldCIdOfPhotoCollectionphoto = photoCollectionphoto.getCId();
                photoCollectionphoto.setCId(country);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldCIdOfPhotoCollectionphoto != null) {
                    oldCIdOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldCIdOfPhotoCollectionphoto = em.merge(oldCIdOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findcountry(country.getId()) != null) {
                throw new PreexistingEntityException("country " + country + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(country country) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            country persistentcountry = em.find(country.class, country.getId());
            Collection<photo> photoCollectionOld = persistentcountry.getPhotoCollection();
            Collection<photo> photoCollectionNew = country.getPhotoCollection();
            List<String> illegalOrphanMessages = null;
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain photo " + photoCollectionOldphoto + " since its CId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            country.setPhotoCollection(photoCollectionNew);
            country = em.merge(country);
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    country oldCIdOfPhotoCollectionNewphoto = photoCollectionNewphoto.getCId();
                    photoCollectionNewphoto.setCId(country);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldCIdOfPhotoCollectionNewphoto != null && !oldCIdOfPhotoCollectionNewphoto.equals(country)) {
                        oldCIdOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldCIdOfPhotoCollectionNewphoto = em.merge(oldCIdOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = country.getId();
                if (findcountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
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
            country country;
            try {
                country = em.getReference(country.class, id);
                country.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<photo> photoCollectionOrphanCheck = country.getPhotoCollection();
            for (photo photoCollectionOrphanCheckphoto : photoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This country (" + country + ") cannot be destroyed since the photo " + photoCollectionOrphanCheckphoto + " in its photoCollection field has a non-nullable CId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(country);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<country> findcountryEntities() {
        return findcountryEntities(true, -1, -1);
    }

    public List<country> findcountryEntities(int maxResults, int firstResult) {
        return findcountryEntities(false, maxResults, firstResult);
    }

    private List<country> findcountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(country.class));
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

    public country findcountry(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(country.class, id);
        } finally {
            em.close();
        }
    }

    public int getcountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<country> rt = cq.from(country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
