package ru.clevertec.zabalotcki.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<Tag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT t FROM Tag t",
                    Tag.class).getResultList();
        }
    }

    @Override
    @Transactional
    public Tag findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Tag byId = session.createQuery("select t from Tag t where t.id =:id",
                    Tag.class)
                    .setParameter("id", id)
                    .getSingleResult();
            session.getTransaction().commit();
            return byId;
        }
    }

    @Override
    @Transactional
    public Tag save(Tag tag) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.save(tag);
            session.getTransaction().commit();
            return tag;
        }
    }

    @Override
    @Transactional
    public Tag update(Tag tag) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Tag giftTag = session.get(Tag.class, tag.getId());

            giftTag.setName(tag.getName());
            session.update(giftTag);

            session.getTransaction().commit();
            return giftTag;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM Tag t WHERE t.id=:id")
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        }
    }

    public void deleteAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM Tag")
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
