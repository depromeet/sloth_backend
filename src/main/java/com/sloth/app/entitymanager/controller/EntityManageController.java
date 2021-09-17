package com.sloth.app.entitymanager.controller;


import com.sloth.app.entitymanager.service.EntityManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/entity-manager")
@RequiredArgsConstructor
public class EntityManageController {

    private final EntityManageService emService;

    @GetMapping("/{entityName}")
    public String getEntityList(@PathVariable String entityName,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "") String type,
                                           @RequestParam(defaultValue = "") String keyword,
                                           Model model) {

        Page<Map<String, Object>> entityPageResult = emService.findAll(entityName, page, size, type, keyword);
        List<String> fieldList = emService.getFieldList(entityName);

        model.addAttribute("entityPageResult", entityPageResult);
        model.addAttribute("fieldList", fieldList);
        model.addAttribute("entityName", entityName);

        model.addAttribute("totalPages", entityPageResult.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("maxPage", 5);

        return "entitymanage/entityManage";
    }

    @GetMapping("/{entityName}/{id}")
    @ResponseBody
    public ResponseEntity<?> getEntityDetail(@PathVariable String entityName, @PathVariable Long id) {
        Map<String, Object> entityMap = emService.findById(entityName, id);

        return ResponseEntity.ok(entityMap);
    }

    @PatchMapping("/{entityName}/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@PathVariable String entityName, @PathVariable Long id, @RequestBody Map<String, Object> requestMap) {
        emService.updateEntity(entityName, id, requestMap);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{entityName}")
    @ResponseBody
    public ResponseEntity<?> updateEntities(@PathVariable String entityName, @RequestBody Map<String, List<Map<String, Object>>> body) {

        List<Map<String, Object>> requestMaps = body.get("requestMaps");

        for (Map<String, Object> entityMap : requestMaps) {
            long id = Long.parseLong(entityMap.get("id").toString());
            emService.updateEntity(entityName, id, entityMap);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{entityName}")
    public @ResponseBody ResponseEntity<?> createEntity(@PathVariable String entityName, @RequestBody Map<String, Object> requestMap) {

        Object entityId = emService.createEntity(entityName, requestMap);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(entityId).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{entityName}/{ids}")
    public @ResponseBody ResponseEntity<?> deleteEntity(@PathVariable String entityName, @PathVariable String ids) {
        emService.deleteEntities(entityName, ids);

        return ResponseEntity.noContent().build();
    }

}