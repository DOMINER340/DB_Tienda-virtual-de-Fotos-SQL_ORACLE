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
import entities.photo;
import java.util.ArrayList;
import java.util.Collection;
import entities.purchase;
import entities.shoppingCart;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class shoppingCartJpaController implements Serializable {

    public shoppingCartJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(shoppingCart shoppingCart) throws PreexistingEntityException, Exception {
        if (shoppingCart.getPhotoCollection() == null) {
            shoppingCart.setPhotoCollection(new ArrayList<photo>());
        }
        if (shoppingCart.getPurchaseCollection() == null) {
            shoppingCart.setPurchaseCollection(new ArrayList<purchase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            account AUsername = shoppingCart.getAUsername();
            if (AUsername != null) {
                AUsername = em.getReference(AUsername.getClass(), AUsername.getUsername());
                shoppingCart.setAUsername(AUsername);
            }
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : shoppingCart.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            shoppingCart.setPhotoCollection(attachedPhotoCollection);
            Collection<purchase> attachedPurchaseCollection = new ArrayList<purchase>();
            for (purchase purchaseCollectionpurchaseToAttach : shoppingCart.getPurchaseCollection()) {
                purchaseCollectionpurchaseToAttach = em.getReference(purchaseCollectionpurchaseToAttach.getClass(), purchaseCollectionpurchaseToAttach.getId());
                attachedPurchaseCollection.add(purchaseCollectionpurchaseToAttach);
            }
            shoppingCart.setPurchaseCollection(attachedPurchaseCollection);
            em.persist(shoppingCart);
            if (AUsername != null) {
                AUsername.getShoppingCartCollection().add(shoppingCart);
                AUsername = em.merge(AUsername);
            }
            for (photo photoCollectionphoto : shoppingCart.getPhotoCollection()) {
                photoCollectionphoto.getShoppingCartCollection().add(shoppingCart);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            for (purchase purchaseCollectionpurchase : shoppingCart.getPurchaseCollection()) {
                shoppingCart oldScIdOfPurchaseCollectionpurchase = purchaseCollectionpurchase.getScId();
                purchaseCollectionpurchase.setScId(shoppingCart);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
                if (oldScIdOfPurchaseCollectionpurchase != null) {
                    oldScIdOfPurchaseCollectionpurchase.getPurchaseCollection().remove(purchaseCollectionpurchase);
                    oldScIdOfPurchaseCollectionpurchase = em.merge(oldScIdOfPurchaseCollectionpurchase);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findshoppingCart(shoppingCart.getId()) != null) {
                throw new PreexistingEntityException("shoppingCart " + shoppingCart + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(shoppingCart shoppingCart) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            shoppingCart persistentshoppingCart = em.find(shoppingCart.class, shoppingCart.getId());
            account AUsernameOld = persistentshoppingCart.getAUsername();
            account AUsernameNew = shoppingCart.getAUsername();
            Collection<photo> photoCollectionOld = persistentshoppingCart.getPhotoCollection();
            Collection<photo> photoCollectionNew = shoppingCart.getPhotoCollection();
            Collection<purchase> purchaseCollectionOld = persistentshoppingCart.getPurchaseCollection();
            Collection<purchase> purchaseCollectionNew = shoppingCart.getPurchaseCollection();
            List<String> illegalOrphanMessages = null;
            for (purchase purchaseCollectionOldpurchase : purchaseCollectionOld) {
                if (!purchaseCollectionNew.contains(purchaseCollectionOldpurchase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain purchase " + purchaseCollectionOldpurchase + " since its scId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (AUsernameNew != null) {
                AUsernameNew = em.getReference(AUsernameNew.getClass(), AUsernameNew.getUsername());
                shoppingCart.setAUsername(AUsernameNew);
            }
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            shoppingCart.setPhotoCollection(photoCollectionNew);
            Collection<purchase> attachedPurchaseCollectionNew = new ArrayList<purchase>();
            for (purchase purchaseCollectionNewpurchaseToAttach : purchaseCollectionNew) {
                purchaseCollectionNewpurchaseToAttach = em.getReference(purchaseCollectionNewpurchaseToAttach.getClass(), purchaseCollectionNewpurchaseToAttach.getId());
                attachedPurchaseCollectionNew.add(purchaseCollectionNewpurchaseToAttach);
            }
            purchaseCollectionNew = attachedPurchaseCollectionNew;
            shoppingCart.setPurchaseCollection(purchaseCollectionNew);
            shoppingCart = em.merge(shoppingCart);
            if (AUsernameOld != null && !AUsernameOld.equals(AUsernameNew)) {
                AUsernameOld.getShoppingCartCollection().remove(shoppingCart);
                AUsernameOld = em.merge(AUsernameOld);
            }
            if (AUsernameNew != null && !AUsernameNew.equals(AUsernameOld)) {
                AUsernameNew.getShoppingCartCollection().add(shoppingCart);
                AUsernameNew = em.merge(AUsernameNew);
            }
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    photoCollectionOldphoto.getShoppingCartCollection().remove(shoppingCart);
                    photoCollectionOldphoto = em.merge(photoCollectionOldphoto);
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    photoCollectionNewphoto.getShoppingCartCollection().add(shoppingCart);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                }
            }
            for (purchase purchaseCollectionNewpurchase : purchaseCollectionNew) {
                if (!purchaseCollectionOld.contains(purchaseCollectionNewpurchase)) {
                    shoppingCart oldScIdOfPurchaseCollectionNewpurchase = purchaseCollectionNewpurchase.getScId();
                    purchaseCollectionNewpurchase.setScId(shoppingCart);
                    purchaseCollectionNewpurchase = em.merge(purchaseCollectionNewpurchase);
                    if (oldScIdOfPurchaseCollectionNewpurchase != null && !oldScIdOfPurchaseCollectionNewpurchase.equals(shoppingCart)) {
                        oldScIdOfPurchaseCollectionNewpurchase.getPurchaseCollection().remove(purchaseCollectionNewpurchase);
                        oldScIdOfPurchaseCollectionNewpurchase = em.merge(oldScIdOfPurchaseCollectionNewpurchase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = shoppingCart.getId();
                if (findshoppingCart(id) == null) {
                    throw new NonexistentEntityException("The shoppingCart with id " + id + " no longer exists.");
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
            shoppingCart shoppingCart;
            try {
                shoppingCart = em.getReference(shoppingCart.class, id);
                shoppingCart.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shoppingCart with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<purchase> purchaseCollectionOrphanCheck = shoppingCart.getPurchaseCollection();
            for (purchase purchaseCollectionOrphanCheckpurchase : purchaseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This shoppingCart (" + shoppingCart + ") cannot be destroyed since the purchase " + purchaseCollectionOrphanCheckpurchase + " in its purchaseCollection field has a non-nullable scId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            account AUsername = shoppingCart.getAUsername();
            if (AUsername != null) {
                AUsername.getShoppingCartCollection().remove(shoppingCart);
                AUsername = em.merge(AUsername);
            }
            Collection<photo> photoCollection = shoppingCart.getPhotoCollection();
            for (photo photoCollectionphoto : photoCollection) {
                photoCollectionphoto.getShoppingCartCollection().remove(shoppingCart);
                photoCollectionphoto = em.merge(photoCollectionphoto);
            }
            em.remove(shoppingCart);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<shoppingCart> findshoppingCartEntities() {
        return findshoppingCartEntities(true, -1, -1);
    }

    public List<shoppingCart> findshoppingCartEntities(int maxResults, int firstResult) {
        return findshoppingCartEntities(false, maxResults, firstResult);
    }

    private List<shoppingCart> findshoppingCartEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(shoppingCart.class));
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

    public shoppingCart findshoppingCart(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(shoppingCart.class, id);
        } finally {
            em.close();
        }
    }

    public int getshoppingCartCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<shoppingCart> rt = cq.from(shoppingCart.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
