package uk.ac.gla.seit.ae2.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RequirementServiceTest {

    @Test
    @Disabled("Enable after RequirementService.submitRequirement is implemented")
    void submitRequirement_rejectsInvalidInput() {
        // TODO: blank moduleCode / blank subject / hours <= 0
    }

    @Test
    @Disabled("Enable after RequirementService.listPendingRequirements is implemented")
    void listPendingRequirements_returnsOnlyPendingItems() {
        // TODO
    }
}