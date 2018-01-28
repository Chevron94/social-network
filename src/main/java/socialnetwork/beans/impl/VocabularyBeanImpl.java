package socialnetwork.beans.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import socialnetwork.beans.VocabularyBean;
import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.dto.creation.VocabularyCreationDto;
import socialnetwork.dto.creation.VocabularyRecordCreationDto;
import socialnetwork.entities.Language;
import socialnetwork.entities.User;
import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.ObjectsNotFoundException;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.repositories.LanguageRepository;
import socialnetwork.repositories.UserRepository;
import socialnetwork.repositories.VocabularyRecordRepository;
import socialnetwork.repositories.VocabularyRepository;

import java.util.List;

/**
 * Created by Roman on 21.01.2018 11:53.
 */
@Component
public class VocabularyBeanImpl implements VocabularyBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyBeanImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private VocabularyRepository vocabularyRepository;
    @Autowired
    private VocabularyRecordRepository vocabularyRecordRepository;

    @Override
    public Vocabulary createVocabulary(VocabularyCreationDto creationDto) {
        User user = userRepository.findOne(creationDto.getUserId());
        if (user == null) {
            LOGGER.info("User {} not found", creationDto.getUserId());
            throw new ValidationException("User with id " + creationDto.getUserId() + " not found");
        }
        if (creationDto.getFrom().equals(creationDto.getTo())){
            LOGGER.info("From and to languages must be different!");
            throw new ValidationException("From and to languages must be different!");
        }
        Language from = languageRepository.findOne(creationDto.getFrom());
        if (from == null) {
            LOGGER.info("Language {} not found", creationDto.getFrom());
            throw new ValidationException("Language with id " + creationDto.getFrom() + " not found");
        }
        Language to = languageRepository.findOne(creationDto.getTo());
        if (to == null) {
            LOGGER.info("Language {} not found", creationDto.getTo());
            throw new ValidationException("Language with id " + creationDto.getTo() + " not found");
        }
        VocabularySearchDto vocabularySearchDto = new VocabularySearchDto(creationDto.getUserId(), creationDto.getFrom(), creationDto.getTo());
        if (vocabularyRepository.findVocabulariesByCustomFilter(vocabularySearchDto).size() != 0) {
            LOGGER.info("Vocabulary with userID {} fromID {} toID {} already exists", vocabularySearchDto.getUserId(), vocabularySearchDto.getFromId(), vocabularySearchDto.getToId());
            throw new ValidationException("User " + creationDto.getUserId() + " already have vocabulary for this languages");
        }
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setUser(user);
        vocabulary.setFrom(from);
        vocabulary.setTo(to);
        return vocabularyRepository.save(vocabulary);
    }

    @Override
    public List<Vocabulary> getVocabularies(Long userId) {
        return vocabularyRepository.findVocabulariesByUser_Id(userId);
    }

    @Override
    public List<Vocabulary> getVocabularies(VocabularySearchDto vocabularySearchDto) {
        return vocabularyRepository.findVocabulariesByCustomFilter(vocabularySearchDto);
    }

    @Override
    public Vocabulary getVocabulary(Long vocabularyId, Long userId) {
        Vocabulary vocabulary = vocabularyRepository.findVocabularyByIdAndUserId(vocabularyId, userId);
        if (vocabulary == null) {
            LOGGER.info("User {} doesn't create Vocabulary {}", userId, vocabularyId);
            throw new ObjectsNotFoundException("User " + userId + " doesn't create Vocabulary " + vocabularyId);
        }
        List<VocabularyRecord> records = vocabularyRecordRepository.findVocabularyRecordsByVocabulary_Id(vocabularyId);
        vocabulary.setRecords(records);
        return vocabulary;
    }

    @Override
    public void deleteVocabulary(Long userId, Long vocabularyId) {
        Vocabulary vocabulary = vocabularyRepository.findOne(vocabularyId);
        if (vocabulary == null) {
            LOGGER.info("Vocabulary with id {} doesn't exist", vocabularyId);
            throw new ObjectsNotFoundException("Vocabulary with id " + vocabularyId + " doesn't exist");
        }
        if (vocabulary.getUser().getId().equals(userId)) {
            vocabularyRepository.delete(vocabulary);
        } else {
            LOGGER.info("Vocabulary with id {} wasn't created by user with id {}", vocabularyId, userId);
            throw new AccessDeniedException("Vocabulary with id " + vocabularyId + " wasn't created by user with id " + userId);
        }
    }

    @Override
    public VocabularyRecord createVocabularyRecord(VocabularyRecordCreationDto creationDto) {
        Vocabulary vocabulary = vocabularyRepository.findOne(creationDto.getVocabularyId());
        if (vocabulary == null) {
            LOGGER.info("Vocabulary with id {} doesn't exist", creationDto.getVocabularyId());
            throw new ObjectsNotFoundException("Vocabulary with id " + creationDto.getVocabularyId() + " doesn't exist");
        }
        if (!vocabulary.getUser().getId().equals(creationDto.getUserId())) {
            LOGGER.info("Vocabulary with id {} wasn't created by user with id {}", creationDto.getVocabularyId(), creationDto.getUserId());
            throw new AccessDeniedException("Vocabulary with id " + creationDto.getVocabularyId() + " wasn't created by user with id " + creationDto.getUserId());
        }
        VocabularyRecord vocabularyRecord = new VocabularyRecord(creationDto.getWord(), creationDto.getTranslation(), creationDto.getComment(), vocabulary);
        return vocabularyRecordRepository.save(vocabularyRecord);
    }

    @Override
    public VocabularyRecord updateVocabularyRecord(Long vocabularyRecordId, VocabularyRecordCreationDto creationDto) {
        VocabularyRecord vocabularyRecord = vocabularyRecordRepository.findOne(vocabularyRecordId);
        if (vocabularyRecord == null) {
            LOGGER.info("Vocabulary Record with id {} not found", vocabularyRecordId);
            throw new ObjectsNotFoundException("Vocabulary Record with id " + vocabularyRecordId + " not found");
        }
        if (!vocabularyRecord.getVocabulary().getUser().getId().equals(creationDto.getUserId())) {
            LOGGER.info("Vocabulary with id {} wasn't created by user with id {}", vocabularyRecord.getVocabulary().getId(), creationDto.getUserId());
            throw new AccessDeniedException("Vocabulary with id " + vocabularyRecord.getVocabulary().getId() + " wasn't created by user with id " + creationDto.getUserId());
        }
        if (!vocabularyRecord.getVocabulary().getId().equals(creationDto.getVocabularyId())) {
            Vocabulary vocabulary = vocabularyRepository.findOne(creationDto.getVocabularyId());
            if (vocabulary == null) {
                LOGGER.info("Vocabulary with id {} doesn't exist", creationDto.getVocabularyId());
                throw new ValidationException("Vocabulary with id " + creationDto.getVocabularyId() + " doesn't exist");
            }
            if (!vocabulary.getUser().getId().equals(creationDto.getUserId())) {
                LOGGER.info("Vocabulary with id {} wasn't created by user with id {}", creationDto.getVocabularyId(), creationDto.getUserId());
                throw new ValidationException("Vocabulary with id " + creationDto.getVocabularyId() + " wasn't created by user with id " + creationDto.getUserId());
            }
            vocabularyRecord.setVocabulary(vocabulary);
        }
        vocabularyRecord.setComment(creationDto.getComment());
        vocabularyRecord.setTranslation(creationDto.getTranslation());
        vocabularyRecord.setWord(creationDto.getWord());
        return vocabularyRecordRepository.save(vocabularyRecord);
    }

    @Override
    public List<VocabularyRecord> getVocabularyRecords(Long userId, Long vocabularyId, String word, String translation) {
        getVocabulary(userId, vocabularyId);
        if (word != null && !word.isEmpty()) {
            if (translation != null && !translation.isEmpty()) {
                return vocabularyRecordRepository.findVocabularyRecordsByVocabulary_IdAndWordStartsWithAndTranslationStartsWith(vocabularyId, word, translation);
            } else {
                return vocabularyRecordRepository.findVocabularyRecordsByVocabulary_IdAndWordStartsWith(vocabularyId, word);
            }
        } else {
            if (translation != null && !translation.isEmpty()) {
                return vocabularyRecordRepository.findVocabularyRecordsByVocabulary_IdAndTranslationStartsWith(vocabularyId, translation);
            }
        }
        return vocabularyRecordRepository.findVocabularyRecordsByVocabulary_Id(vocabularyId);
    }


    @Override
    public void deleteVocabularyRecord(Long userId, Long vocabularyRecordId) {
        VocabularyRecord vocabularyRecord = vocabularyRecordRepository.findOne(vocabularyRecordId);
        if (vocabularyRecord == null) {
            LOGGER.info("Vocabulary record with id {} doesn't exist", vocabularyRecordId);
            throw new ObjectsNotFoundException("Vocabulary record with id " + vocabularyRecordId + " doesn't exist");
        }
        if (vocabularyRecord.getVocabulary().getUser().getId().equals(userId)) {
            vocabularyRecordRepository.delete(vocabularyRecord);
        } else {
            LOGGER.info("Vocabulary with id {} wasn't created by user with id {}", vocabularyRecord.getVocabulary().getId(), userId);
            throw new AccessDeniedException("Vocabulary with id " + vocabularyRecord.getVocabulary().getId() + " wasn't created by user with id " + userId);
        }
    }
}
