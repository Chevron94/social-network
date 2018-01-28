package socialnetwork.repositories.impl;

import org.springframework.stereotype.Repository;
import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.entities.Vocabulary;
import socialnetwork.repositories.VocabularyRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 21.01.2018 12:01.
 */
@Repository
public class VocabularyRepositoryImpl implements VocabularyRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Vocabulary> findVocabulariesByCustomFilter(VocabularySearchDto vocabularySearchDto) {
        String jpa = "SELECT v FROM Vocabulary v WHERE v.user.id = :userId";
        StringBuilder stringBuilder = new StringBuilder(jpa);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", vocabularySearchDto.getUserId());
        if (vocabularySearchDto.getFromId() != null) {
            stringBuilder.append(" AND v.from.id = :fromId");
            parameters.put("fromId", vocabularySearchDto.getFromId());
        }
        if (vocabularySearchDto.getToId() != null) {
            stringBuilder.append(" AND v.to.id = :toId");
            parameters.put("toId", vocabularySearchDto.getToId());
        }
        Query query = entityManager.createQuery(jpa);
        for(Map.Entry<String,Object> entry : parameters.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        return query.getResultList();
    }
}
