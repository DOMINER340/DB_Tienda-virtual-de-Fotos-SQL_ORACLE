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
import entities.country;
import entities.character;
import entities.event;
import entities.ptvf;
import entities.season;
import entities.topic;
import entities.shoppingCart;
import java.util.ArrayList;
import java.util.Collection;
import entities.phxva;
import entities.link;
import entities.photo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class photoJpaController implements Serializable {

    public photoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(photo photo) throws PreexistingEntityException, Exception {
        if (photo.getShoppingCartCollection() == null) {
            photo.setShoppingCartCollection(new ArrayList<shoppingCart>());
        }
        if (photo.getPhxvaCollection() == null) {
            photo.setPhxvaCollection(new ArrayList<phxva>());
        }
        if (photo.getLinkCollection() == null) {
            photo.setLinkCollection(new ArrayList<link>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            account AUsername = photo.getAUsername();
            if (AUsername != null) {
                AUsername = em.getReference(AUsername.getClass(), AUsername.getUsername());
                photo.setAUsername(AUsername);
            }
            country CId = photo.getCId();
            if (CId != null) {
                CId = em.getReference(CId.getClass(), CId.getId());
                photo.setCId(CId);
            }
            character chId = photo.getChId();
            if (chId != null) {
                chId = em.getReference(chId.getClass(), chId.getId());
                photo.setChId(chId);
            }
            event EId = photo.getEId();
            if (EId != null) {
                EId = em.getReference(EId.getClass(), EId.getId());
                photo.setEId(EId);
            }
            ptvf ptvfName = photo.getPtvfName();
            if (ptvfName != null) {
                ptvfName = em.getReference(ptvfName.getClass(), ptvfName.getName());
                photo.setPtvfName(ptvfName);
            }
            season SId = photo.getSId();
            if (SId != null) {
                SId = em.getReference(SId.getClass(), SId.getId());
                photo.setSId(SId);
            }
            topic topicId = photo.getTopicId();
            if (topicId != null) {
                topicId = em.getReference(topicId.getClass(), topicId.getId());
                photo.setTopicId(topicId);
            }
            Collection<shoppingCart> attachedShoppingCartCollection = new ArrayList<shoppingCart>();
            for (shoppingCart shoppingCartCollectionshoppingCartToAttach : photo.getShoppingCartCollection()) {
                shoppingCartCollectionshoppingCartToAttach = em.getReference(shoppingCartCollectionshoppingCartToAttach.getClass(), shoppingCartCollectionshoppingCartToAttach.getId());
                attachedShoppingCartCollection.add(shoppingCartCollectionshoppingCartToAttach);
            }
            photo.setShoppingCartCollection(attachedShoppingCartCollection);
            Collection<phxva> attachedPhxvaCollection = new ArrayList<phxva>();
            for (phxva phxvaCollectionphxvaToAttach : photo.getPhxvaCollection()) {
                phxvaCollectionphxvaToAttach = em.getReference(phxvaCollectionphxvaToAttach.getClass(), phxvaCollectionphxvaToAttach.getPhxvaPK());
                attachedPhxvaCollection.add(phxvaCollectionphxvaToAttach);
            }
            photo.setPhxvaCollection(attachedPhxvaCollection);
            Collection<link> attachedLinkCollection = new ArrayList<link>();
            for (link linkCollectionlinkToAttach : photo.getLinkCollection()) {
                linkCollectionlinkToAttach = em.getReference(linkCollectionlinkToAttach.getClass(), linkCollectionlinkToAttach.getLinkPK());
                attachedLinkCollection.add(linkCollectionlinkToAttach);
            }
            photo.setLinkCollection(attachedLinkCollection);
            em.persist(photo);
            if (AUsername != null) {
                AUsername.getPhotoCollection().add(photo);
                AUsername = em.merge(AUsername);
            }
            if (CId != null) {
                CId.getPhotoCollection().add(photo);
                CId = em.merge(CId);
            }
            if (chId != null) {
                chId.getPhotoCollection().add(photo);
                chId = em.merge(chId);
            }
            if (EId != null) {
                EId.getPhotoCollection().add(photo);
                EId = em.merge(EId);
            }
            if (ptvfName != null) {
                ptvfName.getPhotoCollection().add(photo);
                ptvfName = em.merge(ptvfName);
            }
            if (SId != null) {
                SId.getPhotoCollection().add(photo);
                SId = em.merge(SId);
            }
            if (topicId != null) {
                topicId.getPhotoCollection().add(photo);
                topicId = em.merge(topicId);
            }
            for (shoppingCart shoppingCartCollectionshoppingCart : photo.getShoppingCartCollection()) {
                shoppingCartCollectionshoppingCart.getPhotoCollection().add(photo);
                shoppingCartCollectionshoppingCart = em.merge(shoppingCartCollectionshoppingCart);
            }
            for (phxva phxvaCollectionphxva : photo.getPhxvaCollection()) {
                photo oldPhotoOfPhxvaCollectionphxva = phxvaCollectionphxva.getPhoto();
                phxvaCollectionphxva.setPhoto(photo);
                phxvaCollectionphxva = em.merge(phxvaCollectionphxva);
                if (oldPhotoOfPhxvaCollectionphxva != null) {
                    oldPhotoOfPhxvaCollectionphxva.getPhxvaCollection().remove(phxvaCollectionphxva);
                    oldPhotoOfPhxvaCollectionphxva = em.merge(oldPhotoOfPhxvaCollectionphxva);
                }
            }
            for (link linkCollectionlink : photo.getLinkCollection()) {
                photo oldPhotoOfLinkCollectionlink = linkCollectionlink.getPhoto();
                linkCollectionlink.setPhoto(photo);
                linkCollectionlink = em.merge(linkCollectionlink);
                if (oldPhotoOfLinkCollectionlink != null) {
                    oldPhotoOfLinkCollectionlink.getLinkCollection().remove(linkCollectionlink);
                    oldPhotoOfLinkCollectionlink = em.merge(oldPhotoOfLinkCollectionlink);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findphoto(photo.getName()) != null) {
                throw new PreexistingEntityException("photo " + photo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(photo photo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            photo persistentphoto = em.find(photo.class, photo.getName());
            account AUsernameOld = persistentphoto.getAUsername();
            account AUsernameNew = photo.getAUsername();
            country CIdOld = persistentphoto.getCId();
            country CIdNew = photo.getCId();
            character chIdOld = persistentphoto.getChId();
            character chIdNew = photo.getChId();
            event EIdOld = persistentphoto.getEId();
            event EIdNew = photo.getEId();
            ptvf ptvfNameOld = persistentphoto.getPtvfName();
            ptvf ptvfNameNew = photo.getPtvfName();
            season SIdOld = persistentphoto.getSId();
            season SIdNew = photo.getSId();
            topic topicIdOld = persistentphoto.getTopicId();
            topic topicIdNew = photo.getTopicId();
            Collection<shoppingCart> shoppingCartCollectionOld = persistentphoto.getShoppingCartCollection();
            Collection<shoppingCart> shoppingCartCollectionNew = photo.getShoppingCartCollection();
            Collection<phxva> phxvaCollectionOld = persistentphoto.getPhxvaCollection();
            Collection<phxva> phxvaCollectionNew = photo.getPhxvaCollection();
            Collection<link> linkCollectionOld = persistentphoto.getLinkCollection();
            Collection<link> linkCollectionNew = photo.getLinkCollection();
            List<String> illegalOrphanMessages = null;
            for (phxva phxvaCollectionOldphxva : phxvaCollectionOld) {
                if (!phxvaCollectionNew.contains(phxvaCollectionOldphxva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain phxva " + phxvaCollectionOldphxva + " since its photo field is not nullable.");
                }
            }
            for (link linkCollectionOldlink : linkCollectionOld) {
                if (!linkCollectionNew.contains(linkCollectionOldlink)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain link " + linkCollectionOldlink + " since its photo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (AUsernameNew != null) {
                AUsernameNew = em.getReference(AUsernameNew.getClass(), AUsernameNew.getUsername());
                photo.setAUsername(AUsernameNew);
            }
            if (CIdNew != null) {
                CIdNew = em.getReference(CIdNew.getClass(), CIdNew.getId());
                photo.setCId(CIdNew);
            }
            if (chIdNew != null) {
                chIdNew = em.getReference(chIdNew.getClass(), chIdNew.getId());
                photo.setChId(chIdNew);
            }
            if (EIdNew != null) {
                EIdNew = em.getReference(EIdNew.getClass(), EIdNew.getId());
                photo.setEId(EIdNew);
            }
            if (ptvfNameNew != null) {
                ptvfNameNew = em.getReference(ptvfNameNew.getClass(), ptvfNameNew.getName());
                photo.setPtvfName(ptvfNameNew);
            }
            if (SIdNew != null) {
                SIdNew = em.getReference(SIdNew.getClass(), SIdNew.getId());
                photo.setSId(SIdNew);
            }
            if (topicIdNew != null) {
                topicIdNew = em.getReference(topicIdNew.getClass(), topicIdNew.getId());
                photo.setTopicId(topicIdNew);
            }
            Collection<shoppingCart> attachedShoppingCartCollectionNew = new ArrayList<shoppingCart>();
            for (shoppingCart shoppingCartCollectionNewshoppingCartToAttach : shoppingCartCollectionNew) {
                shoppingCartCollectionNewshoppingCartToAttach = em.getReference(shoppingCartCollectionNewshoppingCartToAttach.getClass(), shoppingCartCollectionNewshoppingCartToAttach.getId());
                attachedShoppingCartCollectionNew.add(shoppingCartCollectionNewshoppingCartToAttach);
            }
            shoppingCartCollectionNew = attachedShoppingCartCollectionNew;
            photo.setShoppingCartCollection(shoppingCartCollectionNew);
            Collection<phxva> attachedPhxvaCollectionNew = new ArrayList<phxva>();
            for (phxva phxvaCollectionNewphxvaToAttach : phxvaCollectionNew) {
                phxvaCollectionNewphxvaToAttach = em.getReference(phxvaCollectionNewphxvaToAttach.getClass(), phxvaCollectionNewphxvaToAttach.getPhxvaPK());
                attachedPhxvaCollectionNew.add(phxvaCollectionNewphxvaToAttach);
            }
            phxvaCollectionNew = attachedPhxvaCollectionNew;
            photo.setPhxvaCollection(phxvaCollectionNew);
            Collection<link> attachedLinkCollectionNew = new ArrayList<link>();
            for (link linkCollectionNewlinkToAttach : linkCollectionNew) {
                linkCollectionNewlinkToAttach = em.getReference(linkCollectionNewlinkToAttach.getClass(), linkCollectionNewlinkToAttach.getLinkPK());
                attachedLinkCollectionNew.add(linkCollectionNewlinkToAttach);
            }
            linkCollectionNew = attachedLinkCollectionNew;
            photo.setLinkCollection(linkCollectionNew);
            photo = em.merge(photo);
            if (AUsernameOld != null && !AUsernameOld.equals(AUsernameNew)) {
                AUsernameOld.getPhotoCollection().remove(photo);
                AUsernameOld = em.merge(AUsernameOld);
            }
            if (AUsernameNew != null && !AUsernameNew.equals(AUsernameOld)) {
                AUsernameNew.getPhotoCollection().add(photo);
                AUsernameNew = em.merge(AUsernameNew);
            }
            if (CIdOld != null && !CIdOld.equals(CIdNew)) {
                CIdOld.getPhotoCollection().remove(photo);
                CIdOld = em.merge(CIdOld);
            }
            if (CIdNew != null && !CIdNew.equals(CIdOld)) {
                CIdNew.getPhotoCollection().add(photo);
                CIdNew = em.merge(CIdNew);
            }
            if (chIdOld != null && !chIdOld.equals(chIdNew)) {
                chIdOld.getPhotoCollection().remove(photo);
                chIdOld = em.merge(chIdOld);
            }
            if (chIdNew != null && !chIdNew.equals(chIdOld)) {
                chIdNew.getPhotoCollection().add(photo);
                chIdNew = em.merge(chIdNew);
            }
            if (EIdOld != null && !EIdOld.equals(EIdNew)) {
                EIdOld.getPhotoCollection().remove(photo);
                EIdOld = em.merge(EIdOld);
            }
            if (EIdNew != null && !EIdNew.equals(EIdOld)) {
                EIdNew.getPhotoCollection().add(photo);
                EIdNew = em.merge(EIdNew);
            }
            if (ptvfNameOld != null && !ptvfNameOld.equals(ptvfNameNew)) {
                ptvfNameOld.getPhotoCollection().remove(photo);
                ptvfNameOld = em.merge(ptvfNameOld);
            }
            if (ptvfNameNew != null && !ptvfNameNew.equals(ptvfNameOld)) {
                ptvfNameNew.getPhotoCollection().add(photo);
                ptvfNameNew = em.merge(ptvfNameNew);
            }
            if (SIdOld != null && !SIdOld.equals(SIdNew)) {
                SIdOld.getPhotoCollection().remove(photo);
                SIdOld = em.merge(SIdOld);
            }
            if (SIdNew != null && !SIdNew.equals(SIdOld)) {
                SIdNew.getPhotoCollection().add(photo);
                SIdNew = em.merge(SIdNew);
            }
            if (topicIdOld != null && !topicIdOld.equals(topicIdNew)) {
                topicIdOld.getPhotoCollection().remove(photo);
                topicIdOld = em.merge(topicIdOld);
            }
            if (topicIdNew != null && !topicIdNew.equals(topicIdOld)) {
                topicIdNew.getPhotoCollection().add(photo);
                topicIdNew = em.merge(topicIdNew);
            }
            for (shoppingCart shoppingCartCollectionOldshoppingCart : shoppingCartCollectionOld) {
                if (!shoppingCartCollectionNew.contains(shoppingCartCollectionOldshoppingCart)) {
                    shoppingCartCollectionOldshoppingCart.getPhotoCollection().remove(photo);
                    shoppingCartCollectionOldshoppingCart = em.merge(shoppingCartCollectionOldshoppingCart);
                }
            }
            for (shoppingCart shoppingCartCollectionNewshoppingCart : shoppingCartCollectionNew) {
                if (!shoppingCartCollectionOld.contains(shoppingCartCollectionNewshoppingCart)) {
                    shoppingCartCollectionNewshoppingCart.getPhotoCollection().add(photo);
                    shoppingCartCollectionNewshoppingCart = em.merge(shoppingCartCollectionNewshoppingCart);
                }
            }
            for (phxva phxvaCollectionNewphxva : phxvaCollectionNew) {
                if (!phxvaCollectionOld.contains(phxvaCollectionNewphxva)) {
                    photo oldPhotoOfPhxvaCollectionNewphxva = phxvaCollectionNewphxva.getPhoto();
                    phxvaCollectionNewphxva.setPhoto(photo);
                    phxvaCollectionNewphxva = em.merge(phxvaCollectionNewphxva);
                    if (oldPhotoOfPhxvaCollectionNewphxva != null && !oldPhotoOfPhxvaCollectionNewphxva.equals(photo)) {
                        oldPhotoOfPhxvaCollectionNewphxva.getPhxvaCollection().remove(phxvaCollectionNewphxva);
                        oldPhotoOfPhxvaCollectionNewphxva = em.merge(oldPhotoOfPhxvaCollectionNewphxva);
                    }
                }
            }
            for (link linkCollectionNewlink : linkCollectionNew) {
                if (!linkCollectionOld.contains(linkCollectionNewlink)) {
                    photo oldPhotoOfLinkCollectionNewlink = linkCollectionNewlink.getPhoto();
                    linkCollectionNewlink.setPhoto(photo);
                    linkCollectionNewlink = em.merge(linkCollectionNewlink);
                    if (oldPhotoOfLinkCollectionNewlink != null && !oldPhotoOfLinkCollectionNewlink.equals(photo)) {
                        oldPhotoOfLinkCollectionNewlink.getLinkCollection().remove(linkCollectionNewlink);
                        oldPhotoOfLinkCollectionNewlink = em.merge(oldPhotoOfLinkCollectionNewlink);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = photo.getName();
                if (findphoto(id) == null) {
                    throw new NonexistentEntityException("The photo with id " + id + " no longer exists.");
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
            photo photo;
            try {
                photo = em.getReference(photo.class, id);
                photo.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The photo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<phxva> phxvaCollectionOrphanCheck = photo.getPhxvaCollection();
            for (phxva phxvaCollectionOrphanCheckphxva : phxvaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This photo (" + photo + ") cannot be destroyed since the phxva " + phxvaCollectionOrphanCheckphxva + " in its phxvaCollection field has a non-nullable photo field.");
            }
            Collection<link> linkCollectionOrphanCheck = photo.getLinkCollection();
            for (link linkCollectionOrphanChecklink : linkCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This photo (" + photo + ") cannot be destroyed since the link " + linkCollectionOrphanChecklink + " in its linkCollection field has a non-nullable photo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            account AUsername = photo.getAUsername();
            if (AUsername != null) {
                AUsername.getPhotoCollection().remove(photo);
                AUsername = em.merge(AUsername);
            }
            country CId = photo.getCId();
            if (CId != null) {
                CId.getPhotoCollection().remove(photo);
                CId = em.merge(CId);
            }
            character chId = photo.getChId();
            if (chId != null) {
                chId.getPhotoCollection().remove(photo);
                chId = em.merge(chId);
            }
            event EId = photo.getEId();
            if (EId != null) {
                EId.getPhotoCollection().remove(photo);
                EId = em.merge(EId);
            }
            ptvf ptvfName = photo.getPtvfName();
            if (ptvfName != null) {
                ptvfName.getPhotoCollection().remove(photo);
                ptvfName = em.merge(ptvfName);
            }
            season SId = photo.getSId();
            if (SId != null) {
                SId.getPhotoCollection().remove(photo);
                SId = em.merge(SId);
            }
            topic topicId = photo.getTopicId();
            if (topicId != null) {
                topicId.getPhotoCollection().remove(photo);
                topicId = em.merge(topicId);
            }
            Collection<shoppingCart> shoppingCartCollection = photo.getShoppingCartCollection();
            for (shoppingCart shoppingCartCollectionshoppingCart : shoppingCartCollection) {
                shoppingCartCollectionshoppingCart.getPhotoCollection().remove(photo);
                shoppingCartCollectionshoppingCart = em.merge(shoppingCartCollectionshoppingCart);
            }
            em.remove(photo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<photo> findphotoEntities() {
        return findphotoEntities(true, -1, -1);
    }

    public List<photo> findphotoEntities(int maxResults, int firstResult) {
        return findphotoEntities(false, maxResults, firstResult);
    }

    private List<photo> findphotoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(photo.class));
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

    public photo findphoto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(photo.class, id);
        } finally {
            em.close();
        }
    }

    public int getphotoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<photo> rt = cq.from(photo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
