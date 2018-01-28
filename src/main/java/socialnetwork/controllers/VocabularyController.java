package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.VocabularyBean;
import socialnetwork.dto.VocabularyDto;
import socialnetwork.dto.VocabularyRecordDto;
import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.dto.creation.VocabularyCreationDto;
import socialnetwork.dto.creation.VocabularyRecordCreationDto;
import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.ObjectsNotFoundException;
import socialnetwork.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 21.01.2018 19:34.
 */
@Controller
public class VocabularyController extends GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyController.class);
    @Autowired
    private VocabularyBean vocabularyBean;

    @RequestMapping(value = "/vocabularies", method = RequestMethod.GET)
    public String getAllVocabularies(HttpServletRequest request, Model model) {
        Long userId = getUserId(request);
        List<Vocabulary> vocabularies = vocabularyBean.getVocabularies(userId);
        model.addAttribute("vocabularies", convertToVocabularyDtos(vocabularies));
        return "vocabularies";
    }

    @RequestMapping(value = "/vocabularies/{id}", method = RequestMethod.GET)
    public String getVocabulary(HttpServletRequest request, Model model, @PathVariable("id") String vocabularyId) {
        Long userId = getUserId(request);
        try {
            Vocabulary vocabulary = vocabularyBean.getVocabulary(Long.valueOf(vocabularyId), userId);
            model.addAttribute("vocabulary", new VocabularyDto(vocabulary));
        } catch (Exception ex) {
            LOGGER.warn("user {}, vocabulary {}", userId, vocabularyId);
            return "404";
        }
        return "vocabulary";
    }

    //TODO: move to RestAPI

    @RequestMapping(value = "/vocabularies", method = RequestMethod.POST)
    public ResponseEntity createVocabulary(@RequestBody VocabularyCreationDto creationDto, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId.equals(creationDto.getUserId())) {
            try {
                Vocabulary vocabulary = vocabularyBean.createVocabulary(creationDto);
                return ResponseEntity.ok(new VocabularyDto(vocabulary));
            } catch (ValidationException ex) {
                LOGGER.info("Can't create vocabulary, ex {}", ex.getMessage());
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("You can't create vocabulary for other user! Your id "+userId+" in request "+creationDto.getUserId());
        }
    }

    @RequestMapping(value = "/vocabularies/filtered")
    public ResponseEntity getVocabularies(@RequestParam(value = "from", required = false) Long from, @RequestParam(value = "to", required = false) Long to, HttpServletRequest request){
        Long userId = getUserId(request);
        VocabularySearchDto vocabularySearchDto = new VocabularySearchDto(userId, from, to);
        return ResponseEntity.ok(convertToVocabularyDtos(vocabularyBean.getVocabularies(vocabularySearchDto)));
    }

    @RequestMapping(value = "/vocabulary/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteVocabulary(HttpServletRequest request, @PathVariable("id") String vocabularyID) {
        Long userId = getUserId(request);
        try {
            vocabularyBean.deleteVocabulary(userId, Long.valueOf(vocabularyID));
        } catch (ObjectsNotFoundException | AccessDeniedException | NumberFormatException ex) {
            LOGGER.info("User {} haven't vocabulary {}", userId, vocabularyID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User "+userId+" haven't vocabulary "+vocabularyID);
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/vocabulary-records", method = RequestMethod.POST)
    public ResponseEntity createRecord(@RequestBody VocabularyRecordCreationDto vocabularyRecordCreationDto, HttpServletRequest request){
        Long userId = getUserId(request);
        if (userId.equals(vocabularyRecordCreationDto.getUserId())) {
            try {
                VocabularyRecord vocabularyRecord = vocabularyBean.createVocabularyRecord(vocabularyRecordCreationDto);
                return ResponseEntity.ok(new VocabularyRecordDto(vocabularyRecord));
            } catch (ObjectsNotFoundException | AccessDeniedException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("You can't create vocabulary record for other user! Your id "+userId+" in request "+vocabularyRecordCreationDto.getUserId());
        }
    }

    @RequestMapping(value = "/vocabulary-records/{id}", method = RequestMethod.POST)
    public ResponseEntity createRecord(@PathVariable("id") String vocabularyRecordId, @RequestBody VocabularyRecordCreationDto vocabularyRecordCreationDto, HttpServletRequest request){
        Long userId = getUserId(request);
        if (userId.equals(vocabularyRecordCreationDto.getUserId())) {
            try {
                VocabularyRecord vocabularyRecord = vocabularyBean.updateVocabularyRecord(Long.valueOf(vocabularyRecordId), vocabularyRecordCreationDto);
                return ResponseEntity.ok(new VocabularyRecordDto(vocabularyRecord));
            } catch (ObjectsNotFoundException | AccessDeniedException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            } catch (NumberFormatException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record with id "+vocabularyRecordId+" not found");
            }
        } else {
            return ResponseEntity.badRequest().body("You can't modify vocabulary record for other user! Your id "+userId+" in request "+vocabularyRecordCreationDto.getUserId());
        }
    }

    @RequestMapping(value = "/vocabulary-records", method = RequestMethod.GET)
    public ResponseEntity createRecord(@RequestParam("vocabularyId") String vocabularyId, @RequestParam(value = "word", required = false) String word, @RequestParam(value = "translation", required = false) String translation, HttpServletRequest request){
        Long userId = getUserId(request);
        try {
            List<VocabularyRecord> vocabularyRecords = vocabularyBean.getVocabularyRecords(userId, Long.valueOf(vocabularyId), word, translation);
            List<VocabularyRecordDto> vocabularyRecordDtos = new ArrayList<>();
            for (VocabularyRecord vocabularyRecord: vocabularyRecords){
                vocabularyRecordDtos.add(new VocabularyRecordDto(vocabularyRecord));
            }
            return ResponseEntity.ok(vocabularyRecordDtos);
        } catch (ObjectsNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Can't find vocabulary with id "+vocabularyId);
        }
    }

    @RequestMapping(value = "/vocabulary-records/{id}", method = RequestMethod.DELETE)
    public ResponseEntity createRecord(@PathVariable("id") String vocabularyRecordId, HttpServletRequest request){
        Long userId = getUserId(request);
        try {
            vocabularyBean.deleteVocabularyRecord(userId, Long.valueOf(vocabularyRecordId));
            return ResponseEntity.noContent().build();
        } catch (ObjectsNotFoundException | AccessDeniedException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Can't find vocabulary record with id "+vocabularyRecordId);
        }
    }

    //Create converters helper
    private List<VocabularyDto> convertToVocabularyDtos(List<Vocabulary> vocabularies) {
        List<VocabularyDto> result = new ArrayList<>();
        for (Vocabulary vocabulary: vocabularies) {
            result.add(new VocabularyDto(vocabulary));
        }
        return result;
    }
}
