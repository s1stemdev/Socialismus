package me.whereareiam.socialismus.feature.bubblechat.requirement.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.feature.bubblechat.requirement.SendPermissionRequirement;

import java.util.Arrays;

@Singleton
public class SenderRequirementValidator extends RequirementValidator {
    @Inject
    public SenderRequirementValidator(SendPermissionRequirement sendPermissionRequirement) {
        super(Arrays.asList(sendPermissionRequirement));
    }
}
