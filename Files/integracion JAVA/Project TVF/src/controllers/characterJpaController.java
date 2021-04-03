/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.character;
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
public class characterJpaController implements Serializable {

    public characterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(character character) throws PreexistingEntityException, Exception {
        if (character.getPhotoCollection() == null) {
            character.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : character.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            character.setPhotoCollection(attachedPhotoCollection);
            em.persist(character);
            for (photo photoCollectionphoto : character.getPhotoCollection()) {
                character oldChIdOfPhotoCollectionphoto = photoCollectionphoto.getChId();
                photoCollectionphoto.setChId(character);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldChIdOfPhotoCollectionphoto != null) {
                    oldChIdOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldChIdOfPhotoCollectionphoto = em.merge(oldChIdOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findcharacter(character.getId()) != null) {
                throw new PreexistingEntityException("character " + character + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(character character) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            character persistentcharacter = em.find(character.class, character.getId());
            Collection<photo> photoCollectionOld = persistentcharacter.getPhotoCollection();
            Collection<photo> photoCollectionNew = character.getPhotoCollection();
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            character.setPhotoCollection(photoCollectionNew);
            character = em.merge(character);
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    photoCollectionOldphoto.setChId(null);
                    photoCollectionOldphoto = em.merge(photoCollectionOldphoto);
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    character oldChIdOfPhotoCollectionNewphoto = photoCollectionNewphoto.getChId();
                    photoCollectionNewphoto.setChId(character);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldChIdOfPhotoCollectionNewphoto != null && !oldChIdOfPhotoCollectionNewphoto.equals(character)) {
                        oldChIdOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldChIdOfPhotoCollectionNewphoto = em.merge(oldChIdOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = character.getId();
                if (findcharacter(id) == null) {
                    throw new NonexistentEntityException("The character with id " + id + " no longer exists.");
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
            character character;
            try {
                character = em.getReference(character.class, id);
                character.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The character with id " + id + " no longer exists.", enfe);
            }
            Collection<photo> photoCollection = character.getPhotoCollection();
            for (photo photoCollectionphoto : photoCollection) {
                photoCollectionphoto.setChId(null);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            em.remove(character);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<character> findcharacterEntities() {
        return findcharacterEntities(true, -1, -1);
    }

    public List<character> findcharacterEntities(int maxResults, int firstResult) {
        return findcharacterEntities(false, maxResults, firstResult);
    }

    private List<character> findcharacterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(character.class));
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

    public character findcharacter(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(character.class, id);
        } finally {
            em.close();
        }
    }

    public int getcharacterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<character> rt = cq.from(character.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
