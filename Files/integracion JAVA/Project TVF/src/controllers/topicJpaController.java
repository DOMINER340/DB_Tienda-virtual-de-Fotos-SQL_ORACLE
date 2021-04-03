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
import entities.topic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class topicJpaController implements Serializable {

    public topicJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(topic topic) throws PreexistingEntityException, Exception {
        if (topic.getPhotoCollection() == null) {
            topic.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : topic.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            topic.setPhotoCollection(attachedPhotoCollection);
            em.persist(topic);
            for (photo photoCollectionphoto : topic.getPhotoCollection()) {
                topic oldTopicIdOfPhotoCollectionphoto = photoCollectionphoto.getTopicId();
                photoCollectionphoto.setTopicId(topic);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldTopicIdOfPhotoCollectionphoto != null) {
                    oldTopicIdOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldTopicIdOfPhotoCollectionphoto = em.merge(oldTopicIdOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findtopic(topic.getId()) != null) {
                throw new PreexistingEntityException("topic " + topic + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(topic topic) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            topic persistenttopic = em.find(topic.class, topic.getId());
            Collection<photo> photoCollectionOld = persistenttopic.getPhotoCollection();
            Collection<photo> photoCollectionNew = topic.getPhotoCollection();
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            topic.setPhotoCollection(photoCollectionNew);
            topic = em.merge(topic);
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    photoCollectionOldphoto.setTopicId(null);
                    photoCollectionOldphoto = em.merge(photoCollectionOldphoto);
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    topic oldTopicIdOfPhotoCollectionNewphoto = photoCollectionNewphoto.getTopicId();
                    photoCollectionNewphoto.setTopicId(topic);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldTopicIdOfPhotoCollectionNewphoto != null && !oldTopicIdOfPhotoCollectionNewphoto.equals(topic)) {
                        oldTopicIdOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldTopicIdOfPhotoCollectionNewphoto = em.merge(oldTopicIdOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = topic.getId();
                if (findtopic(id) == null) {
                    throw new NonexistentEntityException("The topic with id " + id + " no longer exists.");
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
            topic topic;
            try {
                topic = em.getReference(topic.class, id);
                topic.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The topic with id " + id + " no longer exists.", enfe);
            }
            Collection<photo> photoCollection = topic.getPhotoCollection();
            for (photo photoCollectionphoto : photoCollection) {
                photoCollectionphoto.setTopicId(null);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            em.remove(topic);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<topic> findtopicEntities() {
        return findtopicEntities(true, -1, -1);
    }

    public List<topic> findtopicEntities(int maxResults, int firstResult) {
        return findtopicEntities(false, maxResults, firstResult);
    }

    private List<topic> findtopicEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(topic.class));
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

    public topic findtopic(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(topic.class, id);
        } finally {
            em.close();
        }
    }

    public int gettopicCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<topic> rt = cq.from(topic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
