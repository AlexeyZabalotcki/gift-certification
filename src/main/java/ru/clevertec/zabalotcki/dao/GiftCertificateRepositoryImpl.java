package ru.clevertec.zabalotcki.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.zabalotcki.model.GiftCertificate;
import ru.clevertec.zabalotcki.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<GiftCertificate> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<GiftCertificate> resultList = session.createQuery("SELECT g FROM GiftCertificate g",
                    GiftCertificate.class).getResultList();
            return resultList;
        }
    }

    @Override
    @Transactional
    public GiftCertificate findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            GiftCertificate byId = session.createQuery("select g from GiftCertificate g where g.id =:id",
                    GiftCertificate.class)
                    .setParameter("id", id)
                    .getSingleResult();
            session.getTransaction().commit();
            return byId;
        }
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            giftCertificate.setCreateDate(LocalDateTime.now());
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
            checkTags(giftCertificate, session);
            session.save(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            GiftCertificate existingGiftCertificate = session.get(GiftCertificate.class, giftCertificate.getId());

            updateFields(giftCertificate, existingGiftCertificate, session);

            session.update(existingGiftCertificate);

            session.getTransaction().commit();
            return existingGiftCertificate;
        }
    }

    private void updateFields(GiftCertificate giftCertificate, GiftCertificate existingGiftCertificate, Session session) {
        if (giftCertificate.getName() != null) {
            existingGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            existingGiftCertificate.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getDuration() != null) {
            existingGiftCertificate.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getPrice() != null) {
            existingGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getTags() != null) {
            rewriteTags(giftCertificate, session, existingGiftCertificate);
        }
        existingGiftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM GiftCertificate g WHERE g.id=:id")
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public List<GiftCertificate> searchByNameOrDescriptionOrTags(String query) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            List result = session.createQuery("SELECT DISTINCT  g FROM GiftCertificate g INNER JOIN g.tags t " +
                    "WHERE g.name LIKE :query " +
                    "OR t.name LIKE :query " +
                    "OR g.description LIKE :query ")
                    .setParameter("query", "%" + query + "%")
                    .getResultList();
            session.getTransaction().commit();
            return result;
        }
    }

    public void deleteAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM GiftCertificate")
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    private void checkTags(GiftCertificate giftCertificate, Session session) {
        List<Tag> tags = giftCertificate.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            Tag existingTag = findTag(session, tag);
            if (existingTag != null) {
                tags.set(i, existingTag);
            } else {
                session.save(tag);
            }
        }
    }

    private Tag findTag(Session session, Tag tag) {
        return session.createQuery("SELECT t FROM Tag t WHERE name = :name", Tag.class)
                .setParameter("name", tag.getName())
                .uniqueResult();
    }

    private void rewriteTags(GiftCertificate giftCertificate, Session session, GiftCertificate existingGiftCertificate) {
        List<Tag> existingTags = existingGiftCertificate.getTags();
        List<Tag> newTags = giftCertificate.getTags();

        existingTags.retainAll(newTags);

        for (Tag newTag : newTags) {
            if (!existingTags.contains(newTag)) {
                Tag existingTag = findTag(session, newTag);
                if (existingTag != null) {
                    existingTags.add(existingTag);
                } else {
                    session.save(newTag);
                    existingTags.add(newTag);
                }
            }
        }
    }
}
