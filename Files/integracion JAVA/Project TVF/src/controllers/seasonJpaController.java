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
import entities.season;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class seasonJpaController implements Serializable {

    public seasonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(season season) throws PreexistingEntityException, Exception {
        if (season.getPhotoCollection() == null) {
            season.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : season.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            season.setPhotoCollection(attachedPhotoCollection);
            em.persist(season);
            for (photo photoCollectionphoto : season.getPhotoCollection()) {
                season oldSIdOfPhotoCollectionphoto = photoCollectionphoto.getSId();
                photoCollectionphoto.setSId(season);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldSIdOfPhotoCollectionphoto != null) {
                    oldSIdOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldSIdOfPhotoCollectionphoto = em.merge(oldSIdOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findseason(season.getId()) != null) {
                throw new PreexistingEntityException("season " + season + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(season season) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            season persistentseason = em.find(season.class, season.getId());
            Collection<photo> photoCollectionOld = persistentseason.getPhotoCollection();
            Collection<photo> photoCollectionNew = season.getPhotoCollection();
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            season.setPhotoCollection(photoCollectionNew);
            season = em.merge(season);
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    photoCollectionOldphoto.setSId(null);
                    photoCollectionOldphoto = em.merge(photoCollectionOldphoto);
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    season oldSIdOfPhotoCollectionNewphoto = photoCollectionNewphoto.getSId();
                    photoCollectionNewphoto.setSId(season);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldSIdOfPhotoCollectionNewphoto != null && !oldSIdOfPhotoCollectionNewphoto.equals(season)) {
                        oldSIdOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldSIdOfPhotoCollectionNewphoto = em.merge(oldSIdOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = season.getId();
                if (findseason(id) == null) {
                    throw new NonexistentEntityException("The season with id " + id + " no longer exists.");
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
            season season;
            try {
                season = em.getReference(season.class, id);
                season.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The season with id " + id + " no longer exists.", enfe);
            }
            Collection<photo> photoCollection = season.getPhotoCollection();
            for (photo photoCollectionphoto : photoCollection) {
                photoCollectionphoto.setSId(null);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            em.remove(season);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<season> findseasonEntities() {
        return findseasonEntities(true, -1, -1);
    }

    public List<season> findseasonEntities(int maxResults, int firstResult) {
        return findseasonEntities(false, maxResults, firstResult);
    }

    private List<season> findseasonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(season.class));
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

    public season findseason(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(season.class, id);
        } finally {
            em.close();
        }
    }

    public int getseasonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<season> rt = cq.from(season.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
