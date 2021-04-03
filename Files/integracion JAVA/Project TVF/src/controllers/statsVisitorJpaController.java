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
import entities.phxva;
import entities.statsVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gustavo
 */
public class statsVisitorJpaController implements Serializable {

    public statsVisitorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(statsVisitor statsVisitor) throws PreexistingEntityException, Exception {
        if (statsVisitor.getPhxvaCollection() == null) {
            statsVisitor.setPhxvaCollection(new ArrayList<phxva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<phxva> attachedPhxvaCollection = new ArrayList<phxva>();
            for (phxva phxvaCollectionphxvaToAttach : statsVisitor.getPhxvaCollection()) {
                phxvaCollectionphxvaToAttach = em.getReference(phxvaCollectionphxvaToAttach.getClass(), phxvaCollectionphxvaToAttach.getPhxvaPK());
                attachedPhxvaCollection.add(phxvaCollectionphxvaToAttach);
            }
            statsVisitor.setPhxvaCollection(attachedPhxvaCollection);
            em.persist(statsVisitor);
            for (phxva phxvaCollectionphxva : statsVisitor.getPhxvaCollection()) {
                statsVisitor oldStatsVisitorOfPhxvaCollectionphxva = phxvaCollectionphxva.getStatsVisitor();
                phxvaCollectionphxva.setStatsVisitor(statsVisitor);
                phxvaCollectionphxva = em.merge(phxvaCollectionphxva);
                if (oldStatsVisitorOfPhxvaCollectionphxva != null) {
                    oldStatsVisitorOfPhxvaCollectionphxva.getPhxvaCollection().remove(phxvaCollectionphxva);
                    oldStatsVisitorOfPhxvaCollectionphxva = em.merge(oldStatsVisitorOfPhxvaCollectionphxva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findstatsVisitor(statsVisitor.getId()) != null) {
                throw new PreexistingEntityException("statsVisitor " + statsVisitor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(statsVisitor statsVisitor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            statsVisitor persistentstatsVisitor = em.find(statsVisitor.class, statsVisitor.getId());
            Collection<phxva> phxvaCollectionOld = persistentstatsVisitor.getPhxvaCollection();
            Collection<phxva> phxvaCollectionNew = statsVisitor.getPhxvaCollection();
            List<String> illegalOrphanMessages = null;
            for (phxva phxvaCollectionOldphxva : phxvaCollectionOld) {
                if (!phxvaCollectionNew.contains(phxvaCollectionOldphxva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain phxva " + phxvaCollectionOldphxva + " since its statsVisitor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<phxva> attachedPhxvaCollectionNew = new ArrayList<phxva>();
            for (phxva phxvaCollectionNewphxvaToAttach : phxvaCollectionNew) {
                phxvaCollectionNewphxvaToAttach = em.getReference(phxvaCollectionNewphxvaToAttach.getClass(), phxvaCollectionNewphxvaToAttach.getPhxvaPK());
                attachedPhxvaCollectionNew.add(phxvaCollectionNewphxvaToAttach);
            }
            phxvaCollectionNew = attachedPhxvaCollectionNew;
            statsVisitor.setPhxvaCollection(phxvaCollectionNew);
            statsVisitor = em.merge(statsVisitor);
            for (phxva phxvaCollectionNewphxva : phxvaCollectionNew) {
                if (!phxvaCollectionOld.contains(phxvaCollectionNewphxva)) {
                    statsVisitor oldStatsVisitorOfPhxvaCollectionNewphxva = phxvaCollectionNewphxva.getStatsVisitor();
                    phxvaCollectionNewphxva.setStatsVisitor(statsVisitor);
                    phxvaCollectionNewphxva = em.merge(phxvaCollectionNewphxva);
                    if (oldStatsVisitorOfPhxvaCollectionNewphxva != null && !oldStatsVisitorOfPhxvaCollectionNewphxva.equals(statsVisitor)) {
                        oldStatsVisitorOfPhxvaCollectionNewphxva.getPhxvaCollection().remove(phxvaCollectionNewphxva);
                        oldStatsVisitorOfPhxvaCollectionNewphxva = em.merge(oldStatsVisitorOfPhxvaCollectionNewphxva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = statsVisitor.getId();
                if (findstatsVisitor(id) == null) {
                    throw new NonexistentEntityException("The statsVisitor with id " + id + " no longer exists.");
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
            statsVisitor statsVisitor;
            try {
                statsVisitor = em.getReference(statsVisitor.class, id);
                statsVisitor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statsVisitor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<phxva> phxvaCollectionOrphanCheck = statsVisitor.getPhxvaCollection();
            for (phxva phxvaCollectionOrphanCheckphxva : phxvaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This statsVisitor (" + statsVisitor + ") cannot be destroyed since the phxva " + phxvaCollectionOrphanCheckphxva + " in its phxvaCollection field has a non-nullable statsVisitor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(statsVisitor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<statsVisitor> findstatsVisitorEntities() {
        return findstatsVisitorEntities(true, -1, -1);
    }

    public List<statsVisitor> findstatsVisitorEntities(int maxResults, int firstResult) {
        return findstatsVisitorEntities(false, maxResults, firstResult);
    }

    private List<statsVisitor> findstatsVisitorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(statsVisitor.class));
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

    public statsVisitor findstatsVisitor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(statsVisitor.class, id);
        } finally {
            em.close();
        }
    }

    public int getstatsVisitorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<statsVisitor> rt = cq.from(statsVisitor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
