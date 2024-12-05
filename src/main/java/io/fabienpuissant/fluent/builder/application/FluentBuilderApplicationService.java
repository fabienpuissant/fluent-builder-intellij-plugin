package io.fabienpuissant.fluent.builder.application;

import io.fabienpuissant.fluent.builder.domain.BuilderGeneratorDomainService;
import io.fabienpuissant.fluent.builder.infrastructure.primary.FluentBuilderParametersMapper;
import io.fabienpuissant.fluent.builder.infrastructure.primary.PluginContext;
import io.fabienpuissant.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.intellij.psi.PsiField;

import java.util.List;

public class FluentBuilderApplicationService {

    private final BuilderGeneratorDomainService builder;

    public FluentBuilderApplicationService() {
        this.builder = new BuilderGeneratorDomainService(new FluentBuilderGenerator());
    }

    public void generateBuilder(PluginContext context, List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
        builder.generateBuilder(FluentBuilderParametersMapper.toDomain(context, mandatoryFields, optionalFields));
    }

}
