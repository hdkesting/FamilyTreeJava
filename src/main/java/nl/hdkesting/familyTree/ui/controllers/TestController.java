package nl.hdkesting.familyTree.ui.controllers;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/test")
public class TestController {
    private TreeService treeService;

    public TestController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(path="/get/all")
    public @ResponseBody Iterable<IndividualDto> getAllUsers() {
        return treeService.getAllIndividuals();
    }
}
