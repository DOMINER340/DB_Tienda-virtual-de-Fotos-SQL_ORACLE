/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.account;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.ptvf;
import entities.purchase;
import java.util.ArrayList;
import java.util.Collection;
import entities.shoppingCart;
import entities.photo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class accountJpaController implements Serializable {

    public accountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(account account) throws PreexistingEntityException, Exception {
        if (account.getPurchaseCollection() == null) {
            account.setPurchaseCollection(new ArrayList<purchase>());
        }
        if (account.getShoppingCartCollection() == null) {
            account.setShoppingCartCollection(new ArrayList<shoppingCart>());
        }
        if (account.getPhotoCollection() == null) {
            account.setPhotoCollection(new ArrayList<photo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ptvf ptvfName = account.getPtvfName();
            if (ptvfName != null) {
                ptvfName = em.getReference(ptvfName.getClass(), ptvfName.getName());
                account.setPtvfName(ptvfName);
            }
            Collection<purchase> attachedPurchaseCollection = new ArrayList<purchase>();
            for (purchase purchaseCollectionpurchaseToAttach : account.getPurchaseCollection()) {
                purchaseCollectionpurchaseToAttach = em.getReference(purchaseCollectionpurchaseToAttach.getClass(), purchaseCollectionpurchaseToAttach.getId());
                attachedPurchaseCollection.add(purchaseCollectionpurchaseToAttach);
            }
            account.setPurchaseCollection(attachedPurchaseCollection);
            Collection<shoppingCart> attachedShoppingCartCollection = new ArrayList<shoppingCart>();
            for (shoppingCart shoppingCartCollectionshoppingCartToAttach : account.getShoppingCartCollection()) {
                shoppingCartCollectionshoppingCartToAttach = em.getReference(shoppingCartCollectionshoppingCartToAttach.getClass(), shoppingCartCollectionshoppingCartToAttach.getId());
                attachedShoppingCartCollection.add(shoppingCartCollectionshoppingCartToAttach);
            }
            account.setShoppingCartCollection(attachedShoppingCartCollection);
            Collection<photo> attachedPhotoCollection = new ArrayList<photo>();
            for (photo photoCollectionphotoToAttach : account.getPhotoCollection()) {
                photoCollectionphotoToAttach = em.getReference(photoCollectionphotoToAttach.getClass(), photoCollectionphotoToAttach.getName());
                attachedPhotoCollection.add(photoCollectionphotoToAttach);
            }
            account.setPhotoCollection(attachedPhotoCollection);
            em.persist(account);
            if (ptvfName != null) {
                ptvfName.getAccountCollection().add(account);
                ptvfName = em.merge(ptvfName);
            }
            for (purchase purchaseCollectionpurchase : account.getPurchaseCollection()) {
                account oldAUsernameOfPurchaseCollectionpurchase = purchaseCollectionpurchase.getAUsername();
                purchaseCollectionpurchase.setAUsername(account);
                purchaseCollectionpurchase = em.merge(purchaseCollectionpurchase);
                if (oldAUsernameOfPurchaseCollectionpurchase != null) {
                    oldAUsernameOfPurchaseCollectionpurchase.getPurchaseCollection().remove(purchaseCollectionpurchase);
                    oldAUsernameOfPurchaseCollectionpurchase = em.merge(oldAUsernameOfPurchaseCollectionpurchase);
                }
            }
            for (shoppingCart shoppingCartCollectionshoppingCart : account.getShoppingCartCollection()) {
                account oldAUsernameOfShoppingCartCollectionshoppingCart = shoppingCartCollectionshoppingCart.getAUsername();
                shoppingCartCollectionshoppingCart.setAUsername(account);
                shoppingCartCollectionshoppingCart = em.merge(shoppingCartCollectionshoppingCart);
                if (oldAUsernameOfShoppingCartCollectionshoppingCart != null) {
                    oldAUsernameOfShoppingCartCollectionshoppingCart.getShoppingCartCollection().remove(shoppingCartCollectionshoppingCart);
                    oldAUsernameOfShoppingCartCollectionshoppingCart = em.merge(oldAUsernameOfShoppingCartCollectionshoppingCart);
                }
            }
            for (photo photoCollectionphoto : account.getPhotoCollection()) {
                account oldAUsernameOfPhotoCollectionphoto = photoCollectionphoto.getAUsername();
                photoCollectionphoto.setAUsername(account);
                photoCollectionphoto = em.merge(photoCollectionphoto);
                if (oldAUsernameOfPhotoCollectionphoto != null) {
                    oldAUsernameOfPhotoCollectionphoto.getPhotoCollection().remove(photoCollectionphoto);
                    oldAUsernameOfPhotoCollectionphoto = em.merge(oldAUsernameOfPhotoCollectionphoto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findaccount(account.getUsername()) != null) {
                throw new PreexistingEntityException("account " + account + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(account account) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            account persistentaccount = em.find(account.class, account.getUsername());
            ptvf ptvfNameOld = persistentaccount.getPtvfName();
            ptvf ptvfNameNew = account.getPtvfName();
            Collection<purchase> purchaseCollectionOld = persistentaccount.getPurchaseCollection();
            Collection<purchase> purchaseCollectionNew = account.getPurchaseCollection();
            Collection<shoppingCart> shoppingCartCollectionOld = persistentaccount.getShoppingCartCollection();
            Collection<shoppingCart> shoppingCartCollectionNew = account.getShoppingCartCollection();
            Collection<photo> photoCollectionOld = persistentaccount.getPhotoCollection();
            Collection<photo> photoCollectionNew = account.getPhotoCollection();
            List<String> illegalOrphanMessages = null;
            for (purchase purchaseCollectionOldpurchase : purchaseCollectionOld) {
                if (!purchaseCollectionNew.contains(purchaseCollectionOldpurchase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain purchase " + purchaseCollectionOldpurchase + " since its AUsername field is not nullable.");
                }
            }
            for (shoppingCart shoppingCartCollectionOldshoppingCart : shoppingCartCollectionOld) {
                if (!shoppingCartCollectionNew.contains(shoppingCartCollectionOldshoppingCart)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain shoppingCart " + shoppingCartCollectionOldshoppingCart + " since its AUsername field is not nullable.");
                }
            }
            for (photo photoCollectionOldphoto : photoCollectionOld) {
                if (!photoCollectionNew.contains(photoCollectionOldphoto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain photo " + photoCollectionOldphoto + " since its AUsername field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ptvfNameNew != null) {
                ptvfNameNew = em.getReference(ptvfNameNew.getClass(), ptvfNameNew.getName());
                account.setPtvfName(ptvfNameNew);
            }
            Collection<purchase> attachedPurchaseCollectionNew = new ArrayList<purchase>();
            for (purchase purchaseCollectionNewpurchaseToAttach : purchaseCollectionNew) {
                purchaseCollectionNewpurchaseToAttach = em.getReference(purchaseCollectionNewpurchaseToAttach.getClass(), purchaseCollectionNewpurchaseToAttach.getId());
                attachedPurchaseCollectionNew.add(purchaseCollectionNewpurchaseToAttach);
            }
            purchaseCollectionNew = attachedPurchaseCollectionNew;
            account.setPurchaseCollection(purchaseCollectionNew);
            Collection<shoppingCart> attachedShoppingCartCollectionNew = new ArrayList<shoppingCart>();
            for (shoppingCart shoppingCartCollectionNewshoppingCartToAttach : shoppingCartCollectionNew) {
                shoppingCartCollectionNewshoppingCartToAttach = em.getReference(shoppingCartCollectionNewshoppingCartToAttach.getClass(), shoppingCartCollectionNewshoppingCartToAttach.getId());
                attachedShoppingCartCollectionNew.add(shoppingCartCollectionNewshoppingCartToAttach);
            }
            shoppingCartCollectionNew = attachedShoppingCartCollectionNew;
            account.setShoppingCartCollection(shoppingCartCollectionNew);
            Collection<photo> attachedPhotoCollectionNew = new ArrayList<photo>();
            for (photo photoCollectionNewphotoToAttach : photoCollectionNew) {
                photoCollectionNewphotoToAttach = em.getReference(photoCollectionNewphotoToAttach.getClass(), photoCollectionNewphotoToAttach.getName());
                attachedPhotoCollectionNew.add(photoCollectionNewphotoToAttach);
            }
            photoCollectionNew = attachedPhotoCollectionNew;
            account.setPhotoCollection(photoCollectionNew);
            account = em.merge(account);
            if (ptvfNameOld != null && !ptvfNameOld.equals(ptvfNameNew)) {
                ptvfNameOld.getAccountCollection().remove(account);
                ptvfNameOld = em.merge(ptvfNameOld);
            }
            if (ptvfNameNew != null && !ptvfNameNew.equals(ptvfNameOld)) {
                ptvfNameNew.getAccountCollection().add(account);
                ptvfNameNew = em.merge(ptvfNameNew);
            }
            for (purchase purchaseCollectionNewpurchase : purchaseCollectionNew) {
                if (!purchaseCollectionOld.contains(purchaseCollectionNewpurchase)) {
                    account oldAUsernameOfPurchaseCollectionNewpurchase = purchaseCollectionNewpurchase.getAUsername();
                    purchaseCollectionNewpurchase.setAUsername(account);
                    purchaseCollectionNewpurchase = em.merge(purchaseCollectionNewpurchase);
                    if (oldAUsernameOfPurchaseCollectionNewpurchase != null && !oldAUsernameOfPurchaseCollectionNewpurchase.equals(account)) {
                        oldAUsernameOfPurchaseCollectionNewpurchase.getPurchaseCollection().remove(purchaseCollectionNewpurchase);
                        oldAUsernameOfPurchaseCollectionNewpurchase = em.merge(oldAUsernameOfPurchaseCollectionNewpurchase);
                    }
                }
            }
            for (shoppingCart shoppingCartCollectionNewshoppingCart : shoppingCartCollectionNew) {
                if (!shoppingCartCollectionOld.contains(shoppingCartCollectionNewshoppingCart)) {
                    account oldAUsernameOfShoppingCartCollectionNewshoppingCart = shoppingCartCollectionNewshoppingCart.getAUsername();
                    shoppingCartCollectionNewshoppingCart.setAUsername(account);
                    shoppingCartCollectionNewshoppingCart = em.merge(shoppingCartCollectionNewshoppingCart);
                    if (oldAUsernameOfShoppingCartCollectionNewshoppingCart != null && !oldAUsernameOfShoppingCartCollectionNewshoppingCart.equals(account)) {
                        oldAUsernameOfShoppingCartCollectionNewshoppingCart.getShoppingCartCollection().remove(shoppingCartCollectionNewshoppingCart);
                        oldAUsernameOfShoppingCartCollectionNewshoppingCart = em.merge(oldAUsernameOfShoppingCartCollectionNewshoppingCart);
                    }
                }
            }
            for (photo photoCollectionNewphoto : photoCollectionNew) {
                if (!photoCollectionOld.contains(photoCollectionNewphoto)) {
                    account oldAUsernameOfPhotoCollectionNewphoto = photoCollectionNewphoto.getAUsername();
                    photoCollectionNewphoto.setAUsername(account);
                    photoCollectionNewphoto = em.merge(photoCollectionNewphoto);
                    if (oldAUsernameOfPhotoCollectionNewphoto != null && !oldAUsernameOfPhotoCollectionNewphoto.equals(account)) {
                        oldAUsernameOfPhotoCollectionNewphoto.getPhotoCollection().remove(photoCollectionNewphoto);
                        oldAUsernameOfPhotoCollectionNewphoto = em.merge(oldAUsernameOfPhotoCollectionNewphoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = account.getUsername();
                if (findaccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
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
            account account;
            try {
                account = em.getReference(account.class, id);
                account.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<purchase> purchaseCollectionOrphanCheck = account.getPurchaseCollection();
            for (purchase purchaseCollectionOrphanCheckpurchase : purchaseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This account (" + account + ") cannot be destroyed since the purchase " + purchaseCollectionOrphanCheckpurchase + " in its purchaseCollection field has a non-nullable AUsername field.");
            }
            Collection<shoppingCart> shoppingCartCollectionOrphanCheck = account.getShoppingCartCollection();
            for (shoppingCart shoppingCartCollectionOrphanCheckshoppingCart : shoppingCartCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This account (" + account + ") cannot be destroyed since the shoppingCart " + shoppingCartCollectionOrphanCheckshoppingCart + " in its shoppingCartCollection field has a non-nullable AUsername field.");
            }
            Collection<photo> photoCollectionOrphanCheck = account.getPhotoCollection();
            for (photo photoCollectionOrphanCheckphoto : photoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This account (" + account + ") cannot be destroyed since the photo " + photoCollectionOrphanCheckphoto + " in its photoCollection field has a non-nullable AUsername field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ptvf ptvfName = account.getPtvfName();
            if (ptvfName != null) {
                ptvfName.getAccountCollection().remove(account);
                ptvfName = em.merge(ptvfName);
            }
            em.remove(account);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<account> findaccountEntities() {
        return findaccountEntities(true, -1, -1);
    }

    public List<account> findaccountEntities(int maxResults, int firstResult) {
        return findaccountEntities(false, maxResults, firstResult);
    }

    private List<account> findaccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(account.class));
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

    public account findaccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(account.class, id);
        } finally {
            em.close();
        }
    }

    public int getaccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<account> rt = cq.from(account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
