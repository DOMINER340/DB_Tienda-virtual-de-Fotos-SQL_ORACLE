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
import entities.discount;
import entities.shoppingCart;
import entities.taxes;
import java.util.ArrayList;
import java.util.Collection;
import entities.formatSelect;
import entities.link;
import entities.purchase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class purchaseJpaController implements Serializable {

    public purchaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(purchase purchase) throws PreexistingEntityException, Exception {
        if (purchase.getTaxesCollection() == null) {
            purchase.setTaxesCollection(new ArrayList<taxes>());
        }
        if (purchase.getFormatSelectCollection() == null) {
            purchase.setFormatSelectCollection(new ArrayList<formatSelect>());
        }
        if (purchase.getLinkCollection() == null) {
            purchase.setLinkCollection(new ArrayList<link>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            account AUsername = purchase.getAUsername();
            if (AUsername != null) {
                AUsername = em.getReference(AUsername.getClass(), AUsername.getUsername());
                purchase.setAUsername(AUsername);
            }
            discount DId = purchase.getDId();
            if (DId != null) {
                DId = em.getReference(DId.getClass(), DId.getId());
                purchase.setDId(DId);
            }
            shoppingCart scId = purchase.getScId();
            if (scId != null) {
                scId = em.getReference(scId.getClass(), scId.getId());
                purchase.setScId(scId);
            }
            Collection<taxes> attachedTaxesCollection = new ArrayList<taxes>();
            for (taxes taxesCollectiontaxesToAttach : purchase.getTaxesCollection()) {
                taxesCollectiontaxesToAttach = em.getReference(taxesCollectiontaxesToAttach.getClass(), taxesCollectiontaxesToAttach.getName());
                attachedTaxesCollection.add(taxesCollectiontaxesToAttach);
            }
            purchase.setTaxesCollection(attachedTaxesCollection);
            Collection<formatSelect> attachedFormatSelectCollection = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionformatSelectToAttach : purchase.getFormatSelectCollection()) {
                formatSelectCollectionformatSelectToAttach = em.getReference(formatSelectCollectionformatSelectToAttach.getClass(), formatSelectCollectionformatSelectToAttach.getIdfs());
                attachedFormatSelectCollection.add(formatSelectCollectionformatSelectToAttach);
            }
            purchase.setFormatSelectCollection(attachedFormatSelectCollection);
            Collection<link> attachedLinkCollection = new ArrayList<link>();
            for (link linkCollectionlinkToAttach : purchase.getLinkCollection()) {
                linkCollectionlinkToAttach = em.getReference(linkCollectionlinkToAttach.getClass(), linkCollectionlinkToAttach.getLinkPK());
                attachedLinkCollection.add(linkCollectionlinkToAttach);
            }
            purchase.setLinkCollection(attachedLinkCollection);
            em.persist(purchase);
            if (AUsername != null) {
                AUsername.getPurchaseCollection().add(purchase);
                AUsername = em.merge(AUsername);
            }
            if (DId != null) {
                DId.getPurchaseCollection().add(purchase);
                DId = em.merge(DId);
            }
            if (scId != null) {
                scId.getPurchaseCollection().add(purchase);
                scId = em.merge(scId);
            }
            for (taxes taxesCollectiontaxes : purchase.getTaxesCollection()) {
                taxesCollectiontaxes.getPurchaseCollection().add(purchase);
                taxesCollectiontaxes = em.merge(taxesCollectiontaxes);
            }
            for (formatSelect formatSelectCollectionformatSelect : purchase.getFormatSelectCollection()) {
                formatSelectCollectionformatSelect.getPurchaseCollection().add(purchase);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
            }
            for (link linkCollectionlink : purchase.getLinkCollection()) {
                purchase oldPurchaseOfLinkCollectionlink = linkCollectionlink.getPurchase();
                linkCollectionlink.setPurchase(purchase);
                linkCollectionlink = em.merge(linkCollectionlink);
                if (oldPurchaseOfLinkCollectionlink != null) {
                    oldPurchaseOfLinkCollectionlink.getLinkCollection().remove(linkCollectionlink);
                    oldPurchaseOfLinkCollectionlink = em.merge(oldPurchaseOfLinkCollectionlink);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findpurchase(purchase.getId()) != null) {
                throw new PreexistingEntityException("purchase " + purchase + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(purchase purchase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            purchase persistentpurchase = em.find(purchase.class, purchase.getId());
            account AUsernameOld = persistentpurchase.getAUsername();
            account AUsernameNew = purchase.getAUsername();
            discount DIdOld = persistentpurchase.getDId();
            discount DIdNew = purchase.getDId();
            shoppingCart scIdOld = persistentpurchase.getScId();
            shoppingCart scIdNew = purchase.getScId();
            Collection<taxes> taxesCollectionOld = persistentpurchase.getTaxesCollection();
            Collection<taxes> taxesCollectionNew = purchase.getTaxesCollection();
            Collection<formatSelect> formatSelectCollectionOld = persistentpurchase.getFormatSelectCollection();
            Collection<formatSelect> formatSelectCollectionNew = purchase.getFormatSelectCollection();
            Collection<link> linkCollectionOld = persistentpurchase.getLinkCollection();
            Collection<link> linkCollectionNew = purchase.getLinkCollection();
            List<String> illegalOrphanMessages = null;
            for (link linkCollectionOldlink : linkCollectionOld) {
                if (!linkCollectionNew.contains(linkCollectionOldlink)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain link " + linkCollectionOldlink + " since its purchase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (AUsernameNew != null) {
                AUsernameNew = em.getReference(AUsernameNew.getClass(), AUsernameNew.getUsername());
                purchase.setAUsername(AUsernameNew);
            }
            if (DIdNew != null) {
                DIdNew = em.getReference(DIdNew.getClass(), DIdNew.getId());
                purchase.setDId(DIdNew);
            }
            if (scIdNew != null) {
                scIdNew = em.getReference(scIdNew.getClass(), scIdNew.getId());
                purchase.setScId(scIdNew);
            }
            Collection<taxes> attachedTaxesCollectionNew = new ArrayList<taxes>();
            for (taxes taxesCollectionNewtaxesToAttach : taxesCollectionNew) {
                taxesCollectionNewtaxesToAttach = em.getReference(taxesCollectionNewtaxesToAttach.getClass(), taxesCollectionNewtaxesToAttach.getName());
                attachedTaxesCollectionNew.add(taxesCollectionNewtaxesToAttach);
            }
            taxesCollectionNew = attachedTaxesCollectionNew;
            purchase.setTaxesCollection(taxesCollectionNew);
            Collection<formatSelect> attachedFormatSelectCollectionNew = new ArrayList<formatSelect>();
            for (formatSelect formatSelectCollectionNewformatSelectToAttach : formatSelectCollectionNew) {
                formatSelectCollectionNewformatSelectToAttach = em.getReference(formatSelectCollectionNewformatSelectToAttach.getClass(), formatSelectCollectionNewformatSelectToAttach.getIdfs());
                attachedFormatSelectCollectionNew.add(formatSelectCollectionNewformatSelectToAttach);
            }
            formatSelectCollectionNew = attachedFormatSelectCollectionNew;
            purchase.setFormatSelectCollection(formatSelectCollectionNew);
            Collection<link> attachedLinkCollectionNew = new ArrayList<link>();
            for (link linkCollectionNewlinkToAttach : linkCollectionNew) {
                linkCollectionNewlinkToAttach = em.getReference(linkCollectionNewlinkToAttach.getClass(), linkCollectionNewlinkToAttach.getLinkPK());
                attachedLinkCollectionNew.add(linkCollectionNewlinkToAttach);
            }
            linkCollectionNew = attachedLinkCollectionNew;
            purchase.setLinkCollection(linkCollectionNew);
            purchase = em.merge(purchase);
            if (AUsernameOld != null && !AUsernameOld.equals(AUsernameNew)) {
                AUsernameOld.getPurchaseCollection().remove(purchase);
                AUsernameOld = em.merge(AUsernameOld);
            }
            if (AUsernameNew != null && !AUsernameNew.equals(AUsernameOld)) {
                AUsernameNew.getPurchaseCollection().add(purchase);
                AUsernameNew = em.merge(AUsernameNew);
            }
            if (DIdOld != null && !DIdOld.equals(DIdNew)) {
                DIdOld.getPurchaseCollection().remove(purchase);
                DIdOld = em.merge(DIdOld);
            }
            if (DIdNew != null && !DIdNew.equals(DIdOld)) {
                DIdNew.getPurchaseCollection().add(purchase);
                DIdNew = em.merge(DIdNew);
            }
            if (scIdOld != null && !scIdOld.equals(scIdNew)) {
                scIdOld.getPurchaseCollection().remove(purchase);
                scIdOld = em.merge(scIdOld);
            }
            if (scIdNew != null && !scIdNew.equals(scIdOld)) {
                scIdNew.getPurchaseCollection().add(purchase);
                scIdNew = em.merge(scIdNew);
            }
            for (taxes taxesCollectionOldtaxes : taxesCollectionOld) {
                if (!taxesCollectionNew.contains(taxesCollectionOldtaxes)) {
                    taxesCollectionOldtaxes.getPurchaseCollection().remove(purchase);
                    taxesCollectionOldtaxes = em.merge(taxesCollectionOldtaxes);
                }
            }
            for (taxes taxesCollectionNewtaxes : taxesCollectionNew) {
                if (!taxesCollectionOld.contains(taxesCollectionNewtaxes)) {
                    taxesCollectionNewtaxes.getPurchaseCollection().add(purchase);
                    taxesCollectionNewtaxes = em.merge(taxesCollectionNewtaxes);
                }
            }
            for (formatSelect formatSelectCollectionOldformatSelect : formatSelectCollectionOld) {
                if (!formatSelectCollectionNew.contains(formatSelectCollectionOldformatSelect)) {
                    formatSelectCollectionOldformatSelect.getPurchaseCollection().remove(purchase);
                    formatSelectCollectionOldformatSelect = em.merge(formatSelectCollectionOldformatSelect);
                }
            }
            for (formatSelect formatSelectCollectionNewformatSelect : formatSelectCollectionNew) {
                if (!formatSelectCollectionOld.contains(formatSelectCollectionNewformatSelect)) {
                    formatSelectCollectionNewformatSelect.getPurchaseCollection().add(purchase);
                    formatSelectCollectionNewformatSelect = em.merge(formatSelectCollectionNewformatSelect);
                }
            }
            for (link linkCollectionNewlink : linkCollectionNew) {
                if (!linkCollectionOld.contains(linkCollectionNewlink)) {
                    purchase oldPurchaseOfLinkCollectionNewlink = linkCollectionNewlink.getPurchase();
                    linkCollectionNewlink.setPurchase(purchase);
                    linkCollectionNewlink = em.merge(linkCollectionNewlink);
                    if (oldPurchaseOfLinkCollectionNewlink != null && !oldPurchaseOfLinkCollectionNewlink.equals(purchase)) {
                        oldPurchaseOfLinkCollectionNewlink.getLinkCollection().remove(linkCollectionNewlink);
                        oldPurchaseOfLinkCollectionNewlink = em.merge(oldPurchaseOfLinkCollectionNewlink);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = purchase.getId();
                if (findpurchase(id) == null) {
                    throw new NonexistentEntityException("The purchase with id " + id + " no longer exists.");
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
            purchase purchase;
            try {
                purchase = em.getReference(purchase.class, id);
                purchase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The purchase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<link> linkCollectionOrphanCheck = purchase.getLinkCollection();
            for (link linkCollectionOrphanChecklink : linkCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This purchase (" + purchase + ") cannot be destroyed since the link " + linkCollectionOrphanChecklink + " in its linkCollection field has a non-nullable purchase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            account AUsername = purchase.getAUsername();
            if (AUsername != null) {
                AUsername.getPurchaseCollection().remove(purchase);
                AUsername = em.merge(AUsername);
            }
            discount DId = purchase.getDId();
            if (DId != null) {
                DId.getPurchaseCollection().remove(purchase);
                DId = em.merge(DId);
            }
            shoppingCart scId = purchase.getScId();
            if (scId != null) {
                scId.getPurchaseCollection().remove(purchase);
                scId = em.merge(scId);
            }
            Collection<taxes> taxesCollection = purchase.getTaxesCollection();
            for (taxes taxesCollectiontaxes : taxesCollection) {
                taxesCollectiontaxes.getPurchaseCollection().remove(purchase);
                taxesCollectiontaxes = em.merge(taxesCollectiontaxes);
            }
            Collection<formatSelect> formatSelectCollection = purchase.getFormatSelectCollection();
            for (formatSelect formatSelectCollectionformatSelect : formatSelectCollection) {
                formatSelectCollectionformatSelect.getPurchaseCollection().remove(purchase);
                formatSelectCollectionformatSelect = em.merge(formatSelectCollectionformatSelect);
            }
            em.remove(purchase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<purchase> findpurchaseEntities() {
        return findpurchaseEntities(true, -1, -1);
    }

    public List<purchase> findpurchaseEntities(int maxResults, int firstResult) {
        return findpurchaseEntities(false, maxResults, firstResult);
    }

    private List<purchase> findpurchaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(purchase.class));
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

    public purchase findpurchase(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(purchase.class, id);
        } finally {
            em.close();
        }
    }

    public int getpurchaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<purchase> rt = cq.from(purchase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
