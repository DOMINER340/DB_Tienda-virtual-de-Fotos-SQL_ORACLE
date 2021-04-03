/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.event;
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
public class eventJpaController implements Serializable {

    public eventJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(event event) throws PreexistingEntityException, Exception {
        if (event.getPhotoCollection() == null) {
            event.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : event.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            event.setPhotoCollection(attachedPhotoCollection);
            em.persist(event);
            for (photo photoCollectionphoto : event.getPhotoCollection()) {
                event oldEIdOfPhotoCollectionphoto = photoCollectionphoto.getEId();
                photoCollectionphoto.setEId(event);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldEIdOfPhotoCollectionphoto != null) {
                    oldEIdOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldEIdOfPhotoCollectionphoto = em.merge(oldEIdOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findevent(event.getId()) != null) {
                throw new PreexistingEntityException("event " + event + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(event event) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            event persistentevent = em.find(event.class, event.getId());
            Collection<photo> photoCollectionOld = persistentevent.getPhotoCollection();
            Collection<photo> photoCollectionNew = event.getPhotoCollection();
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            event.setPhotoCollection(photoCollectionNew);
            event = em.merge(event);
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    photoCollectionOldphoto.setEId(null);
                    photoCollectionOldphoto = em.merge(photoCollectionOldphoto);
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    event oldEIdOfPhotoCollectionNewphoto = photoCollectionNewphoto.getEId();
                    photoCollectionNewphoto.setEId(event);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldEIdOfPhotoCollectionNewphoto != null && !oldEIdOfPhotoCollectionNewphoto.equals(event)) {
                        oldEIdOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldEIdOfPhotoCollectionNewphoto = em.merge(oldEIdOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = event.getId();
                if (findevent(id) == null) {
                    throw new NonexistentEntityException("The event with id " + id + " no longer exists.");
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
            event event;
            try {
                event = em.getReference(event.class, id);
                event.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The event with id " + id + " no longer exists.", enfe);
            }
            Collection<photo> photoCollection = event.getPhotoCollection();
            for (photo photoCollectionphoto : photoCollection) {
                photoCollectionphoto.setEId(null);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            em.remove(event);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<event> findeventEntities() {
        return findeventEntities(true, -1, -1);
    }

    public List<event> findeventEntities(int maxResults, int firstResult) {
        return findeventEntities(false, maxResults, firstResult);
    }

    private List<event> findeventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(event.class));
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

    public event findevent(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(event.class, id);
        } finally {
            em.close();
        }
    }

    public int geteventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<event> rt = cq.from(event.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
