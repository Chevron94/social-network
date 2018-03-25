package socialnetwork.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.VocabularyBean;
import socialnetwork.dto.VocabularyDto;
import socialnetwork.dto.VocabularyRecordDto;
import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.dto.creation.VocabularyCreationDto;
import socialnetwork.dto.creation.VocabularyRecordCreationDto;
import socialnetwork.entities.User;
import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.ObjectsNotFoundException;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.helpers.Converters;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 10.03.2018 12:51.
 */
@RestController
@RequestMapping(value = "/api/v1/vocabulary")
public class VocabularyRestController extends GenericAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyRestController.class);
    @Autowired
    private VocabularyBean vocabularyBean;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createVocabulary(@RequestBody VocabularyCreationDto creationDto, HttpServletRequest request) {
        User user = getUser(request);
        creationDto.setUserId(user.getId());
        try {
            Vocabulary vocabulary = vocabularyBean.createVocabulary(creationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new VocabularyDto(vocabulary));
        } catch (ValidationException ex) {
            LOGGER.info("Can't create vocabulary, ex {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getVocabularies(@RequestParam(value = "from", required = false) Long from, @RequestParam(value = "to", required = false) Long to, HttpServletRequest request) {
        User user = getUser(request);
        VocabularySearchDto vocabularySearchDto = new VocabularySearchDto(user.getId(), from, to);
        return ResponseEntity.ok(Converters.convertToVocabularyDtos(vocabularyBean.getVocabularies(vocabularySearchDto)));
    }

    @RequestMapping(value = "/{vocabularyId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteVocabulary(HttpServletRequest request, @PathVariable("vocabularyId") String vocabularyID) {
        User user = getUser(request);
        try {
            vocabularyBean.deleteVocabulary(user.getId(), Long.valueOf(vocabularyID));
        } catch (ObjectsNotFoundException | AccessDeniedException | NumberFormatException ex) {
            LOGGER.info("User {} haven't vocabulary {}", user.getId(), vocabularyID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User " + user.getId() + " haven't vocabulary " + vocabularyID);
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{vocabularyId}/vocabulary-records", method = RequestMethod.POST)
    public ResponseEntity createRecord(@PathVariable("vocabularyId") Long vocabularyId, @RequestBody VocabularyRecordCreationDto vocabularyRecordCreationDto, HttpServletRequest request) {
        User user = getUser(request);
        try {
            vocabularyRecordCreationDto.setUserId(user.getId());
            vocabularyRecordCreationDto.setVocabularyId(vocabularyId);
            VocabularyRecord vocabularyRecord = vocabularyBean.createVocabularyRecord(vocabularyRecordCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new VocabularyRecordDto(vocabularyRecord));
        } catch (ObjectsNotFoundException | AccessDeniedException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    @RequestMapping(value = "/{vocabularyId}/vocabulary-records/{vocabularyRecordId}", method = RequestMethod.PUT)
    public ResponseEntity createRecord(@PathVariable("vocabularyId") Long vocabularyId, @PathVariable("vocabularyRecordId") Long vocabularyRecordId, @RequestBody VocabularyRecordCreationDto vocabularyRecordCreationDto, HttpServletRequest request) {
        User user = getUser(request);
        try {
            vocabularyRecordCreationDto.setUserId(user.getId());
            vocabularyRecordCreationDto.setVocabularyId(vocabularyId);
            VocabularyRecord vocabularyRecord = vocabularyBean.updateVocabularyRecord(vocabularyRecordId, vocabularyRecordCreationDto);
            return ResponseEntity.ok(new VocabularyRecordDto(vocabularyRecord));
        } catch (ObjectsNotFoundException | AccessDeniedException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record with id " + vocabularyRecordId + " not found");
        }

    }

    @RequestMapping(value = "/{vocabularyId}/vocabulary-records", method = RequestMethod.GET)
    public ResponseEntity createRecord(@PathVariable("vocabularyId") Long vocabularyId, @RequestParam(value = "word", required = false) String word, @RequestParam(value = "translation", required = false) String translation, HttpServletRequest request) {
        User user = getUser(request);
        try {
            List<VocabularyRecord> vocabularyRecords = vocabularyBean.getVocabularyRecords(user.getId(), vocabularyId, word, translation);
            List<VocabularyRecordDto> vocabularyRecordDtos = new ArrayList<>();
            for (VocabularyRecord vocabularyRecord : vocabularyRecords) {
                vocabularyRecordDtos.add(new VocabularyRecordDto(vocabularyRecord));
            }
            return ResponseEntity.ok(vocabularyRecordDtos);
        } catch (ObjectsNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{vocabularyId}/vocabulary-records/{vocabularyRecordId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRecord(@PathVariable("vocabularyId") Long vocabularyId, @PathVariable("vocabularyRecordId") Long vocabularyRecordId, HttpServletRequest request) {
        User user = getUser(request);
        try {
            //add validation for vocabulary id
            vocabularyBean.deleteVocabularyRecord(user.getId(),vocabularyRecordId);
            return ResponseEntity.noContent().build();
        } catch (ObjectsNotFoundException | AccessDeniedException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Can't find vocabulary record with id " + vocabularyRecordId);
        }
    }

}
