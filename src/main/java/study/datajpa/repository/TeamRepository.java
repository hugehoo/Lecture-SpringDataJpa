package study.datajpa.repository;


import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public Optional<Team> findById(Long id) {
        Team member = em.find(Team.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count (m) from Team m", Long.class).getSingleResult();
    }

    public List<Team> findAll() {
        return em.createQuery("select m from Team m", Team.class).getResultList();
    }
}
